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

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.StringValidator;
import tools4j.validator.Validator;
import tools4j.validator.ValidatorFactory;

public class DynamicValidationFeature {
	private Validator<?> v;

	@Before
	public void before() {
		StringValidator sv = new StringValidator();
		sv.setPattern("[0-9]+");
		
		String constraints = sv.getConstraintsAsJsonObject().toString();
		v = ValidatorFactory.createValidator(ValidatorFactory.parseType(constraints));
		v.setConstraints(constraints);
	}
	
	@Test
	public void test_Validation_invalid() {
		v.validateString("");
		assertTrue(v.getViolationMessage()!=null);
		assertEquals("Pattern", v.getViolationConstraint());
	}
	
	@Test
	public void test_Validation_valid() {
		v.validateString("12");
		assertTrue(v.getViolationMessage()==null);
		assertTrue(v.getViolationConstraint()==null);
	}
}
