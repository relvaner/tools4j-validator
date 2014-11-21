package tools4j.validator.features;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.BusinessRule;
import tools4j.validator.BusinessRulesManager;
import tools4j.validator.DateValidator;
import tools4j.validator.DefaultBusinessRuleListener;

public class BusinessRulesFeature {
	private DateValidator v1;
	private DateValidator v2;
	private DateValidator v3;
	
	@Before
	public void before() {
		final BusinessRulesManager manager = new BusinessRulesManager();
		manager.addRule("AdultCheck", new BusinessRule<Calendar>() {
			@Override
			public boolean checkBusinessRule(Calendar value) {
				Calendar now = Calendar.getInstance();
				now.set(Calendar.YEAR, now.get(Calendar.YEAR)-18);
				
				return value.compareTo(now)<=0;
			}
		});
		
		
		v1 = new DateValidator();
		v1.setBusinessRuleID(manager.getRuleID("AdultCheck"));
		v1.setBusinessRuleListener(new DefaultBusinessRuleListener(manager));
		v1.setPattern("yyyy-mm-dd");
		
		v2 = new DateValidator();
		v2.setBusinessRuleListener(new DefaultBusinessRuleListener(manager));
		v3 = new DateValidator();
		v3.setBusinessRuleListener(new DefaultBusinessRuleListener(manager));
	}
	
	@Test
	public void test_Rule_invalid() {
		v1.validateString("2192-12-24");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("Rule", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("2192-12-24");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("Rule", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("2192-12-24");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("Rule", v3.getViolationConstraint());
	}
	
	@Test
	public void test_Rule_valid() {
		v1.validateString("1971-12-21");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validateString("1971-12-21");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validateString("1971-12-21");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
