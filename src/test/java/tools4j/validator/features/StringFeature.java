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

import tools4j.validator.StringValidator;

public class StringFeature {
	private StringValidator v1;
	private StringValidator v2;
	private StringValidator v3;

	@Before
	public void before() {
		v1 = new StringValidator();
		v2 = new StringValidator();
		v3 = new StringValidator();
	}
	
	@Test
	public void test_resetMessageAndConstraint() {
		v1.setPattern("[0-9]*");
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Pattern", v1.getViolationConstraint());
		v1.validate("123");
		assertNull(v1.getViolationMessage());
		assertNull(v1.getViolationConstraint());
	}
	
	@Test
	public void test_resetConstraints() {
		v1.setNotEmpty(true);
		v1.setEmpty(true);
		v1.setPattern("");
		v1.setMin(1);
		v1.setMax(1);
		
		v1.setConstraints("{}");
		assertTrue(((JsonArray)v1.getConstraintsAsJsonObject().get("constraints")).size()==0);
	}
	
	@Test
	public void test_Pattern_invalid() {
		v1.setPattern("[0-9]*");
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Pattern", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Pattern", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Pattern", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Pattern_valid() {
		v1.setPattern("[A-Z]*");
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_NotEmpty_invalid() {
		v1.setNotEmpty(true);
		v1.validate("");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("NotEmpty", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("NotEmpty", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("NotEmpty", v3.getViolationConstraint());
	}
	
	@Test
	public void test_NotEmpty_valid() {
		v1.setNotEmpty(false);
		v1.validate("");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v1.setNotEmpty(true);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_Empty_invalid() {
		v1.setEmpty(true);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Empty", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Empty", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Empty", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Empty_valid() {
		v1.setEmpty(false);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v1.setEmpty(true);
		v1.validate("");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_Min_invalid() {
		v1.setMin(4);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Min", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Min", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Min", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Min_valid() {
		v1.setMin(3);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_Max_invalid() {
		v1.setMax(2);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Max", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Max", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Max", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Max_valid() {
		v1.setMax(3);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_Size_invalid() {
		v1.setSize(1, 2);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Size", v1.getViolationConstraint());
		v1.setSize(4, 4);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Size", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Size", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Size", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Size_valid() {
		v1.setSize(1, 3);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.setSize(2, 5);
		v1.validate("ABC");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("ABC");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
