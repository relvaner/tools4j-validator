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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class DateValidator extends Validator<Calendar> {
	protected String pattern;
	
	protected boolean valid;
	
	protected boolean past;
	protected boolean future;
	
	public DateValidator() {
		super();
		
		pattern = "dd.MM.yy HH:mm";
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void setValid(boolean value) {
		valid = value;
	}
	
	public boolean isPast() {
		return past;
	}

	public void setPast(boolean value) {
		past = value;
	}

	public boolean isFuture() {
		return future;
	}

	public void setFuture(boolean value) {
		future = value;
	}

	@Override
	protected boolean transform(Window window, String value) {
		boolean result = true;
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			if (valid) 
				simpleDateFormat.setLenient(false);
			
			Date date = simpleDateFormat.parse(value);
			
			transformedValue = Calendar.getInstance();
			transformedValue.setTime(date);
		} catch (ParseException e) {
			if (valid)
				violationConstraint = "Pattern|Valid";
			else
				violationConstraint = "Pattern";
			violationMessage = String.format(violationMessageResource.getString("STR_DATE"), value, pattern);
			
			showViolationDialog(window);
			
			result = false;
		}
		
		return result;
	}

	@Override
	protected boolean validation(Window window, Calendar value) {
		boolean result = true;

		if (valid) {
			value.setLenient(false);
			try {
				value.getTime();
			}
			catch(IllegalArgumentException e) {
				violationConstraint = "Valid";
				violationMessage = String.format(violationMessageResource.getString("STR_VALID"), e.getMessage());
				
				showViolationDialog(window);
				
				result = false;
			}
		}
		
		if (result) {
			int c = value.compareTo(Calendar.getInstance());
			if (past && c>0) {
				violationConstraint = "Past";
				violationMessage = String.format(violationMessageResource.getString("STR_PAST"), new SimpleDateFormat(pattern).format(value.getTime()));
			
				showViolationDialog(window);
			
				result = false;
			} else if (future && c<0) {
				violationConstraint = "Future";
				violationMessage = String.format(violationMessageResource.getString("STR_FUTURE"), new SimpleDateFormat(pattern).format(value.getTime()));
			
				showViolationDialog(window);
			
				result = false;	
			}
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
					 } else if (parser.getString().equals("Valid")) {
						 setValid(true);
					 } else if (parser.getString().equals("Past")) {
						 setPast(true);
					 } else if (parser.getString().equals("Future")) {
						 setFuture(true);
					 }
				 }
		}
		parser.close();
	}
	
	@Override
	public void setConstraints(String constraints) {
		pattern  = null;
		valid    = false;
		past     = false;
		future   = false;
		
		super.setConstraints(constraints);
	}
	
	@Override
	protected void buildConstraints(JsonArrayBuilder jsonArrayBuilder) {
		if (pattern!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Pattern")
					.add("regex", pattern));
		
		if (valid)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Valid"));
		
		if (past)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Past"));
		if (future)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Future"));
	}
}
