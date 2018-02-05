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
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class PickListValidator extends StringValidator {
	protected Set<String> pickList;

	public Set<String> getPickList() {
		return pickList;
	}

	public void setPickList(Set<String> pickList) {
		this.pickList = pickList;
	}

	protected boolean validation(Window window, String value) {
		boolean result = super.validation(window, value);
		
		if (result)
			if (pickList!=null) 
				if (!pickList.contains(value)) {
					violationConstraint = "List";
					violationMessage = String.format(violationMessageResource.getString("STR_PICKLIST"), value, pickList.toString());
					showViolationDialog(window);
					
					result = false;
				}
		
		return result;
	}
	
	@Override
	protected void interpretConstraints(String constraints) {
		super.interpretConstraints(constraints);
		
		JsonParser parser = Json.createParser(new StringReader(constraints));
		while (parser.hasNext()) {
			 Event e = parser.next();
			 if (e == Event.KEY_NAME)
				 if (parser.getString().equalsIgnoreCase("constraint")) {
					 parser.next();
					 if (parser.getString().equalsIgnoreCase("List")) {
						 parser.next();
						 
						 pickList = new HashSet<>();
						 while (parser.hasNext()) {
							 Event item = parser.next();
							 if (item == Event.VALUE_STRING)
								 pickList.add(parser.getString());
						 }
					 }
			 }
		}
		parser.close();
	}
	
	@Override
	public void setConstraints(String constraints) {
		pickList = null;
		
		super.setConstraints(constraints);
	}
	
	@Override
	protected void buildConstraints(JsonArrayBuilder jsonArrayBuilder) {
		super.buildConstraints(jsonArrayBuilder);
		
		if (pickList!=null) {
			JsonArrayBuilder list = Json.createArrayBuilder();
			for (String s : pickList)
				list.add(s);
			jsonArrayBuilder.add(Json.createObjectBuilder()
					.add("constraint", "List")
					.add("list", list));
		}
	}
}
