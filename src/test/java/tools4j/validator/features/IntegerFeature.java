package tools4j.validator.features;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.IntegerValidator;

public class IntegerFeature {
	private IntegerValidator v1;
	private IntegerValidator v2;
	private IntegerValidator v3;

	@Before
	public void before() {
		v1 = new IntegerValidator();
		v2 = new IntegerValidator();
		v3 = new IntegerValidator();
	}
	
	@Test
	public void test_Transform_invalid() {
		v1.validateString("a");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Transform", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("2.34");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Transform", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("-2147483649");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Transform", v3.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("2147483648");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Transform", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Transform_valid() {
		v1.validateString("4");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("-2147483648");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("2147483647");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}	
}
