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
