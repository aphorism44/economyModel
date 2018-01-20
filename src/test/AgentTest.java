package test;

import economyModel.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.Assert;

public class AgentTest {

	private ArrayList<Agent> agents;
	
	@Test
	public void createFarmer() {
		FarmerProduction fp = new FarmerProduction();
		Agent farmer = new Agent("farmer", fp);
		
		assertTrue(farmer.getAgentType().equalsIgnoreCase("farmer") );
		assertEquals(Math.floor(farmer.getMoney()), 1000.0, 5);
		farmer.produce();
		LinkedHashMap<String, Integer> inv = farmer.getInventory();
		assertEquals((int)inv.get("wood"), 7);
		assertEquals((int)inv.get("food"), 4);
	}
	@Test
	public void createSmith() {
		BlacksmithProduction bp = new BlacksmithProduction();
		Agent blacksmith = new Agent("blacksmith", bp);
		
		assertTrue(blacksmith.getAgentType().equalsIgnoreCase("blacksmith"));
		assertEquals(Math.floor(blacksmith.getMoney()), 1000.0, 5);
		blacksmith.produce();
		LinkedHashMap<String, Integer> inv = blacksmith.getInventory();
		assertEquals((int)inv.get("food"), 7);
		assertEquals((int)inv.get("metal"), 0);
		assertEquals((int)inv.get("tools"), 8);
	}
	@Test
	public void createLumberjack() {
		LumberjackProduction lp = new LumberjackProduction();
		Agent lumberjack = new Agent("lumberjack", lp);
		
		assertTrue(lumberjack.getAgentType().equalsIgnoreCase("lumberjack") );
		assertEquals(Math.floor(lumberjack.getMoney()), 1000.0, 5);
		lumberjack.produce();
		LinkedHashMap<String, Integer> inv = lumberjack.getInventory();
		assertEquals((int)inv.get("wood"), 2);
		assertEquals((int)inv.get("food"), 7);
	}
	@Test
	public void createRefiner() {
		RefinerProduction rp = new RefinerProduction();
		Agent refiner = new Agent("refiner", rp);
		
		assertTrue(refiner.getAgentType().equalsIgnoreCase("refiner") );
		assertEquals(Math.floor(refiner.getMoney()), 1000.0, 5);
		refiner.produce();
		LinkedHashMap<String, Integer> inv = refiner.getInventory();
		assertEquals((int)inv.get("ore"), 0);
		assertEquals((int)inv.get("metal"), 8);
		assertEquals((int)inv.get("food"), 7);
	}
	@Test
	public void createMiner() {
		MinerProduction mp = new MinerProduction();
		Agent miner = new Agent("miner", mp);
		
		assertTrue(miner.getAgentType().equalsIgnoreCase("miner") );
		assertEquals(Math.floor(miner.getMoney()), 1000.0, 5);
		miner.produce();
		LinkedHashMap<String, Integer> inv = miner.getInventory();
		assertEquals((int)inv.get("food"), 7);
		assertEquals((int)inv.get("ore"), 4);
	}
	@Test
	public void testCreateMultiples() {
		ArrayList<Agent> agents = new ArrayList<Agent>();
		
		for (int i = 0; i < 5; i++) {
			FarmerProduction fp = new FarmerProduction();
			Agent farmer = new Agent("farmer", fp);
			agents.add(farmer);
			
			BlacksmithProduction bp = new BlacksmithProduction();
			Agent blacksmith = new Agent("blacksmith", bp);
			agents.add(blacksmith);
			
			LumberjackProduction lp = new LumberjackProduction();
			Agent lumberjack = new Agent("lumberjack", lp);
			agents.add(lumberjack);
			
			RefinerProduction rp = new RefinerProduction();
			Agent refiner = new Agent("refiner", rp);
			agents.add(refiner);
			
			MinerProduction mp = new MinerProduction();
			Agent miner = new Agent("miner", mp);
			agents.add(miner);
		}
		
		assertEquals(agents.size(), 25);
		
		for (Agent a : agents)
			a.produce();
		
	}
	
}
//assertEquals
//assertThat