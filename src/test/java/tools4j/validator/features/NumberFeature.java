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
