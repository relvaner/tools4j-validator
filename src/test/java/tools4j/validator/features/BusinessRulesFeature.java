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
package tools4j.validator.features;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.BusinessRule;
import tools4j.validator.BusinessRulesManager;
import tools4j.validator.DateValidator;
import tools4j.validator.DefaultBusinessRuleListener;

public class BusinessRulesFeature {
	private DateValidator v1;
	private DateValidator v2;
	private DateValidator v3;
	
	@Before
	public void before() {
		final BusinessRulesManager manager = new BusinessRulesManager();
		manager.addRule("AdultCheck", new BusinessRule<Calendar>() {
			@Override
			public boolean checkBusinessRule(Calendar value) {
				Calendar now = Calendar.getInstance();
				now.set(Calendar.YEAR, now.get(Calendar.YEAR)-18);
				
				return value.compareTo(now)<=0;
			}
		});
		
		
		v1 = new DateValidator();
		v1.setBusinessRuleID(manager.getRuleID("AdultCheck"));
		v1.setBusinessRuleListener(new DefaultBusinessRuleListener(manager));
		v1.setPattern("yyyy-mm-dd");
		
		v2 = new DateValidator();
		v2.setBusinessRuleListener(new DefaultBusinessRuleListener(manager));
		v3 = new DateValidator();
		v3.setBusinessRuleListener(new DefaultBusinessRuleListener(manager));
	}
	
	@Test
	public void test_Rule_invalid() {
		v1.validateString("2192-12-24");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Rule", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("2192-12-24");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Rule", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("2192-12-24");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Rule", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Rule_valid() {
		v1.validateString("1971-12-21");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("1971-12-21");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("1971-12-21");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
