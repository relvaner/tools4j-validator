package tools4j.validator.features;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.json.JsonArray;

import org.junit.Before;
import org.junit.Test;

import tools4j.validator.PickListValidator;

public class PickListFeature {
	private PickListValidator v1;
	private PickListValidator v2;
	private PickListValidator v3;
	
	private Set<String> list;

	@Before
	public void before() {
		v1 = new PickListValidator();
		v2 = new PickListValidator();
		v3 = new PickListValidator();
		
		list = new HashSet<>();
		list.add("1");
		list.add("2");
		list.add("3");
	}

	@Test
	public void test_resetConstraints() {
		v1.setPickList(new HashSet<String>());
		
		v1.setConstraints("{}");
		assertTrue(((JsonArray)v1.getConstraintsAsJsonObject().get("constraints")).size()==0);
	}
	
	@Test
	public void test_List_invalid() {
		v1.setPickList(list);
		v1.validate("0");
		assertTrue(v1.getViolationMessage()!=null);
		assertEquals("List", v1.getViolationConstraint());
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("ABC");
		assertTrue(v2.getViolationMessage()!=null);
		assertEquals("List", v2.getViolationConstraint());
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("4");
		assertTrue(v3.getViolationMessage()!=null);
		assertEquals("List", v3.getViolationConstraint());
	}
	
	@Test
	public void test_List_valid() {
		v1.setPickList(list);
		v1.validate("1");
		assertTrue(v1.getViolationMessage()==null);
		assertTrue(v1.getViolationConstraint()==null);
		
		v2.setConstraints(v1.getConstraintsAsJsonObject());
		v2.validate("2");
		assertTrue(v2.getViolationMessage()==null);
		assertTrue(v2.getViolationConstraint()==null);
		
		v3.setConstraints(v1.getConstraintsAsJsonObject().toString());
		v3.validate("3");
		assertTrue(v3.getViolationMessage()==null);
		assertTrue(v3.getViolationConstraint()==null);
	}
}
