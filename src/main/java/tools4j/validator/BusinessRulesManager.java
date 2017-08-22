/*
 * tools4j-validator - Framework for Validation
 * Copyright (c) 2014-2017, David A. Bauer
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BusinessRulesManager {
	protected Map<String, BusinessRuleTuple> rules;
	protected Map<String, String> namesOfRules;
	
	public BusinessRulesManager() {
		rules = new HashMap<>();
		namesOfRules = new HashMap<>();
	}

	public Map<String, BusinessRuleTuple> getRules() {
		return rules;
	}

	public void setRules(Map<String, BusinessRuleTuple> rules) {
		this.rules = rules;
	}

	public String addRule(String name, BusinessRule<?> rule) {
		String result = UUID.randomUUID().toString();
		
		if (rules.get(result)==null) {
			rules.put(result, new BusinessRuleTuple(name, rule));
			if (name!=null)
				namesOfRules.put(name, result);
		}
		else
			return null; // uuid duplicate
		
		return result;
	}
	
	public String addRule(BusinessRule<?> rule) {
		return addRule(null, rule);
	}
	
	public String getRuleID(String name) {
		return namesOfRules.get(name);
	}
	
	public String getRuleName(String id) {
		return rules.get(id).getName();
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkBusinessRule(String id, Object value) {
		return ((BusinessRule<Object>)rules.get(id).getRule()).checkBusinessRule(value);
	}
}
