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

import javax.json.JsonArray;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.BooleanValidator;

public class BooleanFeature {
	private BooleanValidator v1;
	private BooleanValidator v2;
	private BooleanValidator v3;

	@Before
	public void before() {
		v1 = new BooleanValidator();
		v2 = new BooleanValidator();
		v3 = new BooleanValidator();
	}

	@Test
	public void test_resetConstraints() {
		v1.setAssertTrue(true);
		v1.setAssertFalse(true);

		v1.setConstraints("{}");
		assertTrue(((JsonArray)v1.getConstraintsAsJsonObject().get("constraints")).size()==0);
	}
	
	@Test
	public void test_Transform_invalid() {
		v1.validateString("2");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Transform", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Transform", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("ABC");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Transform", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Transform_valid() {
		v1.validateString("1");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.validateString("0");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("true");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("false");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}	
	
	@Test
	public void test_AssertFalse_invalid() {
		v1.setAssertFalse(true);
		v1.validateString("1");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("AssertFalse", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("true");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("AssertFalse", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("true");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("AssertFalse", v3.getViolationConstraint());
	}
	
	@Test
	public void test_AssertFalse_valid() {
		v1.setAssertFalse(false);
		v1.validateString("1");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.setAssertFalse(true);
		v1.validateString("0");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("false");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate(false);
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_AssertTrue_invalid() {
		v1.setAssertTrue(true);
		v1.validateString("0");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("AssertTrue", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("false");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("AssertTrue", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("false");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("AssertTrue", v3.getViolationConstraint());
	}
	
	@Test
	public void test_AssertTrue_valid() {
		v1.setAssertTrue(false);
		v1.validateString("0");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.setAssertTrue(true);
		v1.validateString("1");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("true");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate(true);
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
