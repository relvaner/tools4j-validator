/*
 * tools4j-validator - Framework for Validation
 * Copyright (c) 2014, David A. Bauer
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tools4j.validator;

import java.awt.Window;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public abstract class NumberValidator<T extends Number & Comparable<T>> extends Validator<T> {
	protected T min;
	protected T max;
	protected boolean minIncluded; 
	protected boolean maxIncluded;
	
	public NumberValidator() {
		super();
		
		minIncluded = true;
		maxIncluded = true;
	}
	
	public T getMin() {
		return min;
	}

	public void setMin(T min) {
		this.min = min;
		
		minIncluded = true;
	}

	public T getMax() {
		return max;
	}

	public void setMax(T max) {
		this.max = max;
		
		maxIncluded = true;
	}

	public boolean isMinIncluded() {
		return minIncluded;
	}

	public void setMinIncluded(boolean minIncluded) {
		this.minIncluded = minIncluded;
	}

	public boolean isMaxIncluded() {
		return maxIncluded;
	}

	public void setMaxIncluded(boolean maxIncluded) {
		this.maxIncluded = maxIncluded;
	}

	public void setMin(T min, boolean minIncluded) {
		setMin(min);
		setMinIncluded(minIncluded);
	}
	
	public void setMax(T max, boolean maxIncluded) {
		setMax(max);
		setMaxIncluded(maxIncluded);
	}
	
	public void setRange(T min, T max) {
		setMin(min);
		setMax(max);
	}
	
	public void setRange(T min, T max, boolean minIncluded, boolean maxIncluded) {
		setMin(min, minIncluded);
		setMax(max, maxIncluded);
	}
	
	protected boolean inRange(T value) {
		boolean result = true;
		
		if (min!=null) {
			result = value.compareTo(min)>0;
			if (minIncluded) result = result || value.compareTo(min)==0;
		}
		if (max!=null) {
			boolean buffer = result;
			result = value.compareTo(max)<0;
			if (maxIncluded) result = result || value.compareTo(max)==0;
			result = buffer && result;
		}	
		
		return result;
	}
	
	@Override
	protected boolean validation(Window window, T value) {
		boolean result = true;
		
		if (min!=null || max!=null)
			if (!inRange(value)) {
				String minStr = String.valueOf(min);
				String maxStr = String.valueOf(max);
		
				if (min!=null && max!=null) {
					violationConstraint = "Range";
					violationMessage = String.format(violationMessageResource.getString("STR_RANGE"), value, OPENED[minIncluded?1:0]+minStr, maxStr+CLOSED[maxIncluded?1:0]);
				}
				else if (min==null && max!=null) {
					violationConstraint = "Max";
					violationMessage = String.format(violationMessageResource.getString("STR_RANGE2"), value, SMALLER[maxIncluded?(window==null?1:2):0]+maxStr);
				}
				else if (min!=null && max==null) {
					violationConstraint = "Min";
					violationMessage = String.format(violationMessageResource.getString("STR_RANGE2"), value, LARGER[minIncluded?(window==null?1:2):0]+minStr);
				}
		
				showViolationDialog(window);
				
				result = false;
			}
		
		return result;
	}
	
	@Override
	protected void interpretConstraints(String constraints) {
		JsonParser parser = Json.createParser(new StringReader(constraints));
		while (parser.hasNext()) {
			 Event e = parser.next();
			 if (e == Event.KEY_NAME)
				 if (parser.getString().equals("constraint")) {
					 parser.next();
					 if (parser.getString().equals("Min")) {
						 parser.next();
						 parser.next();
						 setMin(getNumber(parser.getBigDecimal()));
						 e = parser.next();
						 if (e != Event.END_OBJECT && parser.getString().equals("minIncluded")) {
							 parser.next();
							 setMinIncluded(parser.getInt()==1);
						 }
					 }
					 else if (parser.getString().equals("Max")) {
						 parser.next();
						 parser.next();
						 setMax(getNumber(parser.getBigDecimal()));
						 e = parser.next();
						 if (e != Event.END_OBJECT && parser.getString().equals("maxIncluded")) {
							 parser.next();
							 setMaxIncluded(parser.getInt()==1);
						 }
					 }
					 else if (parser.getString().equals("Range")) {
						 parser.next();
						 parser.next();
						 setMin(getNumber(parser.getBigDecimal()));
						 e = parser.next();
						 if (e != Event.END_OBJECT && parser.getString().equals("minIncluded")) {
							 parser.next();
							 setMinIncluded(parser.getInt()==1);
							 parser.next();
						 }
						 parser.next();
						 setMax(getNumber(parser.getBigDecimal()));
						 e = parser.next();
						 if (e != Event.END_OBJECT && parser.getString().equals("maxIncluded")) {
							 parser.next();
							 setMaxIncluded(parser.getInt()==1);
						 }
					 }
			 }
		}
		parser.close();
	}
	
	@Override
	public void setConstraints(String constraints) {
		min = null;
		max = null;
		
		minIncluded = true;
		maxIncluded = true;
		
		super.setConstraints(constraints);
	}
	
	protected abstract T getNumber(BigDecimal value);
	
	@Override
	protected void buildConstraints(JsonArrayBuilder jsonArrayBuilder) {
		if (min!=null && max!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Range")
					.add("min", putNumber(min))
					.add("minIncluded", new BigDecimal(minIncluded?1:0))
					.add("max", putNumber(max))
					.add("maxIncluded", new BigDecimal(maxIncluded?1:0)));
		else if (min!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Min")
					.add("min", putNumber(min))
					.add("minIncluded", new BigDecimal(minIncluded?1:0)));
		else if (max!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Max")
					.add("max", putNumber(max))
					.add("maxIncluded", new BigDecimal(maxIncluded?1:0)));
	}
	
	protected BigDecimal putNumber(T value) {
		if (value instanceof BigDecimal)
			return (BigDecimal)value;
		else if (value instanceof BigInteger)
			return new BigDecimal((BigInteger)value);
		else if (value instanceof Double)
			return new BigDecimal((Double)value);
		else if (value instanceof Long)
			return new BigDecimal((Long)value);
		else if (value instanceof Integer)
			return new BigDecimal((Integer)value);
		else if (value instanceof Float)
			return new BigDecimal((Double)value);
		else if (value instanceof Short)
			return new BigDecimal((Integer)value);
		else if (value instanceof Byte)
			return new BigDecimal((Integer)value);
			
		return null;
	}
}
