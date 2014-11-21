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
