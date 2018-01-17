package test;

import economyModel.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AgentTest {

	private ArrayList<Agent> agents;
	
	@Test
	public void createFarmer() {
		FarmerProduction fp = new FarmerProduction();
		Agent farmer = new Agent("farmer", fp);
		//assertEquals(testClass.getMissingLetters(testFalse[0]), "abc");
		
		
	}
	
}
