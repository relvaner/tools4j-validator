package tools4j.validator.features;

import static org.junit.Assert.*;

import javax.json.JsonArray;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.StringValidator;

public class BasicFeature {
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
	public void test_Nothing() {
		v1.validate(null);
		assertTrue(v1.getViolationMessage()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate(null);
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate(null);
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_resetConstraints() {
		v1.setNotNull(true);
		v1.setNull(true);
		v1.setBusinessRuleID("1");
		
		v1.setConstraints("{}");
		assertTrue(((JsonArray)v1.getConstraintsAsJsonObject().get("constraints")).size()==0);
	}
	
	@Test
	public void test_NotNull_invalid() {
		v1.setNotNull(true);
		v1.validate(null);
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("NotNull", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate(null);
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("NotNull", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate(null);
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("NotNull", v3.getViolationConstraint());
	}
	
	@Test
	public void test_NotNull_valid() {
		v1.setNotNull(false);
		v1.validate(null);
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate(null);
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate(null);
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
	
	@Test
	public void test_Null_invalid() {
		v1.setNull(true);
		v1.validate("");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Null", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Null", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Null", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Null_valid() {
		v1.setNull(false);
		v1.validate(null);
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate(null);
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate(null);
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
