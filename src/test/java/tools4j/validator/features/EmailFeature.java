package tools4j.validator.features;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.EmailValidator;

public class EmailFeature {
	private EmailValidator v1;
	private EmailValidator v2;
	private EmailValidator v3;

	@Before
	public void before() {
		v1 = new EmailValidator();
		v2 = new EmailValidator();
		v3 = new EmailValidator();
	}
	
	@Test
	public void test_Email_invalid() {
		v1.validate("test@web");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Pattern", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("testweb.de");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Pattern", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("test.web@de");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Pattern", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Email_valid() {
		v1.validate("test@web.de");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("test-1@web.de");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("test.1@web.de");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
