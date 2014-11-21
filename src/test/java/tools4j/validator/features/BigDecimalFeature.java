package tools4j.validator.features;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.BigDecimalValidator;

public class BigDecimalFeature {
	private BigDecimalValidator v1;
	private BigDecimalValidator v2;
	private BigDecimalValidator v3;

	@Before
	public void before() {
		v1 = new BigDecimalValidator();
		v2 = new BigDecimalValidator();
		v3 = new BigDecimalValidator();
	}
	
	@Test
	public void test_Transform_invalid() {
		v1.validateString("a");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Transform", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("b");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Transform", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("b");
		assertTrue(v3.getViolationMessage()!=null);
		assertTrue(v3.getViolationConstraint()!=null);
	}
	
	@Test
	public void test_Transform_valid() {
		v1.validateString("4");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("1.7976931348623157345345345345E+1030832434");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("-1.79769313486231573453453453E+1030899975");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}	
}
