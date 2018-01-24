package test;

import economyModel.*;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import org.junit.Test;
import org.junit.Assert;

public class AgentTest {

	
	@Test
	public void CreateAgents() {
		
		String txtFileName = "C:\\Users\\Jesse\\eclipse-workspace\\economyModel\\src\\productionData.csv";
		boolean fileOpened = false; 
		ArrayList<String> csvLines = null;
		//below are the rules to be turned into behaviors, then agents
		ArrayList<ProductionRule> rules = new ArrayList<ProductionRule>();
		LinkedHashMap<String, ArrayList<ProductionRule>> ruleByAgent = new LinkedHashMap<String, ArrayList<ProductionRule>>();
		//the behaviors
		ArrayList<ProductionBehavior> behaviors = new ArrayList<ProductionBehavior>();
		//the agents
		ArrayList<Agent> agents = new ArrayList<Agent>();
		
		
		//using Java 8 streams
		try (Stream<String> stream = Files.lines(Paths.get(txtFileName))) {
			csvLines = (ArrayList<String>) stream.collect(Collectors.toList());
			fileOpened = true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		//role_name,transaction_type,commodity_name,quantity,action,matched_commodity
		//parse the lines
		if (fileOpened) {
			
			
			for (String s: csvLines) {
				String[] line = s.split(",");
				String roleName = line[0];
				String transactionType = line[1];
				String comm = line[2];
				String qStr = line[3];
				int q = 0;
				try {
					q = Integer.parseInt(qStr);
				} catch (NumberFormatException nfe) {
					System.out.println("hit number exception");
					break;
				}
				String action = line[4];
				String matchComm = line[5];
				if (matchComm.trim().length() < 1)
					matchComm = null;
				
				ProductionRule pr = new ProductionRule(roleName, comm, q, transactionType, action, matchComm);
				rules.add(pr);
				ruleByAgent.put(roleName, new ArrayList<ProductionRule>());
				
			}
			
		}
		
		assertEquals(rules.size(), 32);
		
		//sort rules by agent and do check
		for (ProductionRule rule: rules) {
			assertTrue(rule.isValidRule());
			ArrayList agentBehaviors = ruleByAgent.get(rule.getAgentType());
			agentBehaviors.add(rule);
			ruleByAgent.put(rule.getAgentType(), agentBehaviors);
			
		}
		
		//turn rules into behaviors
		Iterator rba = ruleByAgent.entrySet().iterator();
	    while (rba.hasNext()) {
	        Map.Entry pair = (Map.Entry)rba.next();
	        ArrayList<ProductionRule> prs = (ArrayList<ProductionRule>) pair.getValue();
	        ProductionBehavior rb = new ProductionBehavior(prs);
	        //behaviors.add(rb);
	    }
		
		
		/*
		ProductionBehavior fp = new ProductionBehavior();
		Agent farmer = new Agent("farmer", fp);
		
		assertTrue(farmer.getAgentType().equalsIgnoreCase("farmer") );
		assertEquals(Math.floor(farmer.getMoney()), 1000.0, 5);
		farmer.produce();
		LinkedHashMap<String, Integer> inv = farmer.getInventory();
		assertEquals((int)inv.get("wood"), 7);
		assertEquals((int)inv.get("food"), 4);*/
	}
	/*
	@Test
	public void createSmith() {
		ProductionBehavior bp = new ProductionBehavior();
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
		ProductionBehavior lp = new ProductionBehavior();
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
		ProductionBehavior rp = new ProductionBehavior();
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
		ProductionBehavior mp = new ProductionBehavior();
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
			ProductionBehavior fp = new ProductionBehavior();
			Agent farmer = new Agent("farmer", fp);
			agents.add(farmer);
			
			ProductionBehavior bp = new ProductionBehavior();
			Agent blacksmith = new Agent("blacksmith", bp);
			agents.add(blacksmith);
			
			ProductionBehavior lp = new ProductionBehavior();
			Agent lumberjack = new Agent("lumberjack", lp);
			agents.add(lumberjack);
			
			ProductionBehavior rp = new ProductionBehavior();
			Agent refiner = new Agent("refiner", rp);
			agents.add(refiner);
			
			ProductionBehavior mp = new ProductionBehavior();
			Agent miner = new Agent("miner", mp);
			agents.add(miner);
		}
		
		assertEquals(agents.size(), 25);
		
		for (Agent a : agents)
			a.produce();
		
	}
	*/
}
//assertEquals
//assertThat