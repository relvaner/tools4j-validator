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
import java.util.Locale;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.swing.JOptionPane;

public abstract class Validator<T> {
	private static String violationMessageLanguage = Locale.getDefault().getLanguage();
	protected static ResourceBundle violationMessageResource = ResourceBundle.getBundle("tools4j.resources/Validator", new Locale(violationMessageLanguage));

	protected static final String[] OPENED  = {"(", "["};
	protected static final String[] CLOSED  = {")", "]"};
	protected static final String[] SMALLER = {"<", "<=", "\u2264"};
	protected static final String[] LARGER  = {">", ">=", "\u2265"};
	
	protected String violationMessage;
	protected String violationConstraint;
	
		
	protected TransformationListener transformationListener;
	protected ValidationListener validationListener;
	protected BusinessRuleListener businessRuleListener;
	
	protected boolean notNull; // NotNull
	protected boolean Null;    // Null
	
	protected String businessRuleID;
	
	protected T transformedValue;
	
	protected String constraints; 
	
	public static String getViolationMessageLanguage() {
		return violationMessageLanguage;
	}

	public static void setViolationMessageLanguage(String violationMessageLanguage) {
		Validator.violationMessageLanguage = violationMessageLanguage;
		
		violationMessageResource = ResourceBundle.getBundle("tools4j.resources/Validator", new Locale(violationMessageLanguage));
	}

	public String getViolationMessage() {
		return violationMessage;
	}
	
	public String getViolationConstraint() {
		return violationConstraint;
	}
	
	public TransformationListener getTransformationListener() {
		return transformationListener;
	}

	public void setTransformationListener(TransformationListener transformationListener) {
		this.transformationListener = transformationListener;
	}

	public ValidationListener getValidationListener() {
		return validationListener;
	}	
	
	public void setValidationListener(ValidationListener validationListener) {
		this.validationListener = validationListener;
	}

	public BusinessRuleListener getBusinessRuleListener() {
		return businessRuleListener;
	}

	public void setBusinessRuleListener(BusinessRuleListener businessRuleListener) {
		this.businessRuleListener = businessRuleListener;
	}
	
	public boolean isNotNull() {
		return notNull;
	}
	
	public void setNotNull(boolean value) {
		notNull = value;
	}

	public boolean isNull() {
		return Null;
	}

	public void setNull(boolean value) {
		Null = value;
	}
	
	public String getBusinessRuleID() {
		return businessRuleID;
	}

	public void setBusinessRuleID(String value) {
		businessRuleID = value;
	}

	protected abstract boolean transform(Window window, String value);
	
	public boolean validateString(String value) {
		return validateString(null, value);
	}
	
	public boolean validateString(Window window, String value) {
		boolean result = true;
		
		if (transformationListener!=null) transformationListener.before(this, value);
		
		if (value==null) {
			if (notNull) {
				violationConstraint = "NotNull";
				violationMessage = String.format(violationMessageResource.getString("STR_NOTNULL"));
				showViolationDialog(window);
				
				result = false;
			}
		}
		else {
			if (Null) {
				violationConstraint = "Null";
				violationMessage = String.format(violationMessageResource.getString("STR_NULL"));
				showViolationDialog(window);
				
				result = false;
			}
		}
		
		if (result && value!=null)
			result = transform(window, value);
		
		if (transformationListener!=null) transformationListener.after(this, value, result);
		
		if (result)
			return validate(window, transformedValue);
		else
			return false;
	}
	
	protected abstract boolean validation(Window window, T value);
	
	public boolean validate(T value) {
		return validate(null, value);
	}
	
	public boolean validate(Window window, T value) {
		this.transformedValue = value;
		
		boolean result = true;
		
		violationMessage = null;
		violationConstraint = null;
		
		if (validationListener!=null) validationListener.before(this);
		
		if (value==null) {
			if (notNull) {
				violationConstraint = "NotNull";
				violationMessage = String.format(violationMessageResource.getString("STR_NOTNULL"));
				showViolationDialog(window);
				
				result = false;
			}
		}
		else {
			if (Null) {
				violationConstraint = "Null";
				violationMessage = String.format(violationMessageResource.getString("STR_NULL"));
				showViolationDialog(window);
				
				result = false;
			}
		}
		
		if (result && value!=null)
			result = validation(window, value);
		
		if (result)
			if (businessRuleListener!=null) {
				result = businessRuleListener.checkBusinessRule(this);
				if (!result) {
					violationConstraint = "Rule";
					if (businessRuleID!=null) {
						String businessRuleName = null;
						if (businessRuleListener instanceof DefaultBusinessRuleListener)
							businessRuleName = ((DefaultBusinessRuleListener)businessRuleListener).getBusinessRuleName(this);
						violationMessage = String.format(violationMessageResource.getString("STR_RULE2"), businessRuleName!=null ? businessRuleName : businessRuleID);
					}
					else
						violationMessage = String.format(violationMessageResource.getString("STR_RULE"));
					showViolationDialog(window);
				}
			}
		
		if (validationListener!=null) validationListener.after(this, result);
		
		return result;
	}
	
	public T getValue() {
		return transformedValue;
	}

	protected void showViolationDialog(Window window) {
		if (window!=null)
			JOptionPane.showMessageDialog(window, 
				violationMessage, 
				violationMessageResource.getString("STR_TITLE"),  
				JOptionPane.ERROR_MESSAGE);
	}
	
	public String getConstraints() {
		return constraints;
	}

	protected abstract void interpretConstraints(String constraints);
	
	public void setConstraints(String constraints) {
		this.constraints = constraints;

		notNull        = false;
		Null           = false;
		businessRuleID = null;
		
		JsonParser parser = Json.createParser(new StringReader(constraints));
		while (parser.hasNext()) {
			 Event e = parser.next();
			 if (e == Event.KEY_NAME)
				 if (parser.getString().equals("constraint")) {
					 parser.next();
					 if (parser.getString().equals("NotNull")) {
						 setNotNull(true);
					 } else if (parser.getString().equals("Null")) {
						 setNull(true);
					 } else if (parser.getString().equals("Rule")) {
						 parser.next();
						 parser.next();
						 setBusinessRuleID(parser.getString());
					 }
			 }
		}
		parser.close();
		
		interpretConstraints(constraints);
	}
	
	public void setConstraints(JsonObject model) {
		setConstraints(model.toString());
	}
	
	protected abstract void buildConstraints(JsonArrayBuilder jsonArrayBuilder);
	
	public JsonObject getConstraintsAsJsonObject() {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		if (isNotNull())
			jsonArrayBuilder.add(Json.createObjectBuilder().add("constraint", "NotNull"));
		if (isNull())
			jsonArrayBuilder.add(Json.createObjectBuilder().add("constraint", "Null"));
		if (businessRuleID!=null)
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "Rule")
					.add("id", businessRuleID));
		
		buildConstraints(jsonArrayBuilder);
		
		JsonObject result = Json.createObjectBuilder()
				.add("type", getClass().getSimpleName().replace("Validator", ""))
				.add("constraints", jsonArrayBuilder).build();
		
		return result;
	}
}
