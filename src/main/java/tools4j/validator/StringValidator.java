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

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class StringValidator extends Validator<String> {
	protected String pattern;
	
	protected Integer min;
	protected Integer max;
	
	protected boolean notEmpty; // NotEmpyty
	protected boolean empty;    // Empty
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}	
	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setSize(int min, int max) {
		setMin(min);
		setMax(max);
	}
	
	public boolean inSize(String value) {
		boolean result = true;
		
		if (min!=null)
			result = value.length()>=min;
		if (max!=null)
			result = result && value.length()<=max;
		
		return result;
	}
	
	public boolean isNotEmpty() {
		return notEmpty;
	}

	public void setNotEmpty(boolean value) {
		notEmpty = value;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean value) {
		empty = value;
	}

	@Override
	protected boolean transform(Window window, String value) {
		transformedValue = value;
		
		return true;
	}

	@Override
	protected boolean validation(Window window, String value) {
		boolean result = true;
		
		if (pattern!=null) {
			result = ((String)value).matches(pattern);
		
			if (!result) {
				violationConstraint = "Pattern";
				violationMessage = String.format(violationMessageResource.getString("STR_PATTERN"), value, pattern);
				showViolationDialog(window);
				
				result = false;
			}
		}
		
		if (result) {
			if (notEmpty && value.length()==0) {
				violationConstraint = "NotEmpty";
				violationMessage = String.format(violationMessageResource.getString("STR_NOTEMPTY"));
				showViolationDialog(window);
				
				result = false;
				
			} else if (empty && value.length()!=0) {
				violationConstraint = "Empty";
				violationMessage = String.format(violationMessageResource.getString("STR_EMPTY"), value);
				showViolationDialog(window);
				
				result = false;
			}
		}
			
		if (result)
			if (min!=null || max!=null)
				if (!inSize(value)) {
					String minStr = String.valueOf(min);
					String maxStr = String.valueOf(max);
					
					if (min!=null && max!=null) {
						violationConstraint = "Size";
						violationMessage = String.format(violationMessageResource.getString("STR_SIZE"), value, OPENED[1]+minStr, maxStr+CLOSED[1]);
					}
					else if (min==null && max!=null) {
						violationConstraint = "Max";
						violationMessage = String.format(violationMessageResource.getString("STR_SIZE2"), value, SMALLER[window==null?1:2]+maxStr);
					}
					else if (min!=null && max==null) {
						violationConstraint = "Min";
						violationMessage = String.format(violationMessageResource.getString("STR_SIZE2"), value, LARGER[window==null?1:2]+minStr);
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
					 if (parser.getString().equals("Pattern")) {
						 parser.next();
						 parser.next();
						 setPattern(parser.getString());
					 }
					 else if (parser.getString().equals("NotEmpty")) {
						 setNotEmpty(true);
					 }
					 else if (parser.getString().equals("Empty")) {
						 setEmpty(true);
					 }
					 else if (parser.getString().equals("Min")) {
						 parser.next();
						 parser.next();
						 setMin(parser.getInt());
					 }
					 else if (parser.getString().equals("Max")) {
						 parser.next();
						 parser.next();
						 setMax(parser.getInt());
					 }
					 else if (parser.getString().equals("Size")) {
						 parser.next();
						 parser.next();
						 setMin(parser.getInt());
						 parser.next();
						 parser.next();
						 setMax(parser.getInt());
					 }
			 }
		}
		parser.close();
	}

	@Override
	public void setConstraints(String constraints) {
		pattern  = null;
		notEmpty = false;
		empty    = false;
		min      = null;
		max      = null;
		
		super.setConstraints(constraints);
	}
	
	@Override
	protected void buildConstraints(JsonArrayBuilder jsonArrayBuilder) {
		if (pattern!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Pattern")
					.add("regex", pattern));
		
		if (notEmpty)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "NotEmpty"));
		if (empty)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Empty"));
		
		if (min!=null && max!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Size")
					.add("min", min)
					.add("max", max));
		else if (min!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Min")
					.add("min", min));
		else if (max!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Max")
					.add("max", max));
	}
}
