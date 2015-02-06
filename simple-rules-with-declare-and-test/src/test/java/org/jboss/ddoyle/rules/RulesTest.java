package org.jboss.ddoyle.rules;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactField;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RulesTest {

	private static KieContainer kieContainer;
	
	@BeforeClass
	public static void setup() {
		KieServices kieServices = KieServices.Factory.get();
		kieContainer = kieServices.newKieClasspathContainer();
	}
	
	
	@Test
	public void test() throws Exception {
		KieSession kieSession = kieContainer.newKieSession();
		RulesFiredListener rulesFiredListener = new RulesFiredListener();
		kieSession.addEventListener(rulesFiredListener);
		
		/*
		FactType simpleFactType = kieSession.getKieBase().getFactType("org.jboss.ddoyle.rules", "SimpleFact");
		Object simpleFact;
		try {
			simpleFact = simpleFactType.newInstance();
		} catch (InstantiationException ie) {
			throw new RuntimeException(ie);
		} catch (IllegalAccessException iae) {
			throw new RuntimeException(iae);
		}
		simpleFactType.set(simpleFact, "id", "1");
		*/
		Class simpleFactType = Class.forName("org.jboss.ddoyle.rules.SimpleFact");
		Object simpleFact = simpleFactType.newInstance();
		Field simpleFactTypeIdField = simpleFactType.getDeclaredField("id");
		//Little bit of private field hacking.
		simpleFactTypeIdField.setAccessible(true);
		simpleFactTypeIdField.set(simpleFact, "1");
		
		
		kieSession.insert(simpleFact);
		kieSession.fireAllRules();
		
		assertEquals("One rule should have fired.", 1, rulesFiredListener.getNrOfRulesFired()); 
	}

}
