package test.java;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import main.java.models.Agent;
import main.java.models.ProductionBehavior;
import main.java.models.ProductionRule;

public class AgentTest {

	
	@Test
	public void CreateAgents() {
		
		String txtFileName = "src/main/resources/productionData.csv";
		boolean fileOpened = false; 
		ArrayList<String> csvLines = null;
		//below are the rules to be turned into behaviors, then agents
		ArrayList<ProductionRule> rules = new ArrayList<ProductionRule>();
		LinkedHashMap<String, ArrayList<ProductionRule>> ruleByAgent = new LinkedHashMap<String, ArrayList<ProductionRule>>();
		//the behaviors
		ArrayList<ProductionBehavior> behaviors = new ArrayList<ProductionBehavior>();
		//the agents
		ArrayList<Agent> agents = new ArrayList<Agent>();
		
		File file;
		Scanner csvFile;
		
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
	        behaviors.add(rb);
	    }
	    
	    
	    //confirm all the rules are working
	    for (ProductionBehavior pb: behaviors) {
	    	//System.out.println(pb.toString());
	    	assertTrue(pb.isValid());
	    }
		
	    //finally, add the production behaviors into agents
	    for (ProductionBehavior pb: behaviors) {
	    	Agent a = new Agent(pb.getAgentType(), pb);
	    	agents.add(a);
	    }
	    
	    //checks the production behavior
	    for (Agent a: agents) {
	    	LinkedHashMap<String, Integer> inv;
	    	//System.out.println(a.getAgentType());
	    	//System.out.println(a.getInventory());
	    	a.produce();
	    	//System.out.println(a.getInventory());
	    	
	    	switch (a.getAgentType()) {
	    	case "farmer":
	    		assertEquals(Math.floor(a.getMoney()), 1000.0, 5);
	    		inv = a.getInventory();
	    		assertEquals((int)inv.get("wood"), 7);
	    		assertEquals((int)inv.get("food"), 4);
	    		break;
	    	case "blacksmith":
	    		assertEquals(Math.floor(a.getMoney()), 1000.0, 5);
	    		inv = a.getInventory();
	    		assertEquals((int)inv.get("food"), 7);
	    		assertEquals((int)inv.get("metal"), 0);
	    		assertEquals((int)inv.get("tools"), 8);
	    		break;
	    	case "lumberjack":
	    		assertEquals(Math.floor(a.getMoney()), 1000.0, 5);
	    		inv = a.getInventory();
	    		assertEquals((int)inv.get("wood"), 2);
	    		assertEquals((int)inv.get("food"), 7);
	    		break;
	    	case "refiner":
	    		assertEquals(Math.floor(a.getMoney()), 1000.0, 5);
	    		inv = a.getInventory();
	    		assertEquals((int)inv.get("ore"), 0);
	    		assertEquals((int)inv.get("metal"), 8);
	    		assertEquals((int)inv.get("food"), 7);
	    		break;
	    	case "miner":
	    		assertEquals(Math.floor(a.getMoney()), 1000.0, 5);
	    		inv = a.getInventory();
	    		assertEquals((int)inv.get("food"), 7);
	    		assertEquals((int)inv.get("ore"), 4);
	    		break;
	    	
	    	}
	    	
	    	
	    	
	    }
	    
	}
}