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

public class BooleanValidator extends Validator<Boolean> {
	protected StringValidator transformValidator;
	
	protected boolean assertTrue;
	protected boolean assertFalse;

	public BooleanValidator() {
		transformValidator = new StringValidator();
		transformValidator.setPattern("TRUE|FALSE|1|0");
	}
	
	public boolean isAssertTrue() {
		return assertTrue;
	}

	public void setAssertTrue(boolean value) {
		assertTrue = value;
	}

	public boolean isAssertFalse() {
		return assertFalse;
	}

	public void setAssertFalse(boolean value) {
		assertFalse = value;
	}

	@Override
	protected boolean transform(Window window, String value) {
		boolean result = true;
		
		value = value.toUpperCase();
		
		if (!transformValidator.validate(value)) {
			violationConstraint = "Transform";
			violationMessage = String.format(violationMessageResource.getString("STR_TRANSFORM"), value, "Boolean");
			
			showViolationDialog(window);
			
			result = false;
		}
		else {
			switch (value) {
				case "TRUE" : transformedValue = true; break;
				case "FALSE": transformedValue = false; break;
				case "1"    : transformedValue = true; break;
				case "0"    : transformedValue = false; break;
			}
		}
		
		return result;
	}

	@Override
	protected boolean validation(Window window, Boolean value) {
		boolean result = true;
		
		if (assertTrue && !value) {
			violationConstraint = "AssertTrue";
			violationMessage = String.format(violationMessageResource.getString("STR_ASSERTTRUE"));
			showViolationDialog(window);
			
			result = false;
		} else if (assertFalse && value) {
			violationConstraint = "AssertFalse";
			violationMessage = String.format(violationMessageResource.getString("STR_ASSERTFALSE"));
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
					if (parser.getString().equals("AssertTrue")) {
						setAssertTrue(true); 
						break;
					}
					else if (parser.getString().equals("AssertFalse")) {
						setAssertFalse(true);
						break;
					}
				}
		}
	}

	@Override
	public void setConstraints(String constraints) {
		assertTrue  = false;
		assertFalse = false;
		
		super.setConstraints(constraints);
	}
	
	@Override
	protected void buildConstraints(JsonArrayBuilder jsonArrayBuilder) {
		if (assertTrue)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "AssertTrue"));
		if (assertFalse)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "AssertFalse"));
	}
}
