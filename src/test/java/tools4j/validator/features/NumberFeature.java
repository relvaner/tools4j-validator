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

import tools4j.validator.LongValidator;

public class NumberFeature {
	private LongValidator v1;
	private LongValidator v2;
	private LongValidator v3;

	@Before
	public void before() {
		v1 = new LongValidator();
		v2 = new LongValidator();
		v3 = new LongValidator();
	}

	@Test
	public void test_resetConstraints() {
		v1.setMin(1L);
		v1.setMax(1L);
		
		v1.setConstraints("{}");
		assertTrue(((JsonArray)v1.getConstraintsAsJsonObject().get("constraints")).size()==0);
	}
	
	@Test
	public void test_Min_invalid() {
		v1.setMin((long)4, false);
		v1.validate((long)4);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Min", v1.getViolationConstraint());
		v1.setMin((long)4);
		v1.validate((long)3);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Min", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate((long)2);
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Min", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate((long)1);
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Min", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Min_valid() {
		v1.setMin((long)4, false);
		v1.validateString("5");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.setMin((long)4);
		v1.validateString("4");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("5");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("4");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}	
	
	@Test
	public void test_Max_invalid() {
		v1.setMax((long)4, false);
		v1.validate((long)4);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Max", v1.getViolationConstraint());
		v1.setMax((long)4);
		v1.validate((long)5);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Max", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate((long)6);
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Max", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate((long)7);
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Max", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Max_valid() {
		v1.setMax((long)4, false);
		v1.validateString("3");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.setMax((long)4);
		v1.validateString("4");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("3");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("2");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}	
	
	@Test
	public void test_Range_invalid() {
		v1.setRange((long)1, (long)4, false, false);
		v1.validate((long)4);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Range", v1.getViolationConstraint());
		v1.validate((long)1);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Range", v1.getViolationConstraint());
		v1.setRange((long)1, (long)4);
		v1.validate((long)5);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Range", v1.getViolationConstraint());
		v1.validate((long)0);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Range", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate((long)6);
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Range", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate((long)7);
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Range", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Range_valid() {
		v1.setRange((long)1, (long)4, false, false);
		v1.validateString("3");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.validateString("2");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.setRange((long)1, (long)4);
		v1.validateString("4");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		v1.validateString("1");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("3");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("2");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}	
}
