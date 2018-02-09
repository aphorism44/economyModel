package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Market {
	
	private Clearinghouse house;
	private ArrayList<Agent> agents;
	private ArrayList<Agent> agentTemplates; //these are imported data; user can add to agents as needed
	private static final int defaultAveragePrice = 25;
	
	private static String agentDataCsvFileName = "src/main/resources/productionData.csv";
	
	//eventually, create enums for commodities and define from imported data
	private static ArrayList<String> tradeableCommodities = new ArrayList<String>() {{
		add("food");
		add("wood");
		add("ore");
		add("metal");
		add("tools");
	}};
	
	public Market() {
		agentTemplates = new ArrayList<Agent>();
		agents = new ArrayList<Agent>();
		house = new Clearinghouse();
		loadAgentTemplates();
	}
	
	public void addAgent(String agentType, int n) {
		for (Agent a: agentTemplates) {
			if (a.getAgentType().equals(agentType)) {
				for (int i = 0; i < n; i++) {
					Agent newA = new Agent(a);
					agents.add(newA);
				}
				break;
			}
		}
	}
	
	public void iterateTurn(int n) {
		for (int i = 0; i < n; i++) {
			agentsProduce();
			agentsCreateOffers();
			resolveOffers();
			removeBrokeAgents();
		}
	}
	
	
	public void agentsProduce() {
		for (Agent a: agents)
			a.produce();
	}
	
	public void agentsCreateOffers() {
		house.createOffers(agents);
	}
	
	
	public int getAgentCount() {
		return agents.size();
	}
	
	
	public void resolveOffers() {
		
		LinkedHashMap<UUID, Agent> agentMap = new LinkedHashMap<UUID, Agent>();
		for (Agent a: agents)
			agentMap.put(a.getUuid(), a);
		house.offerRound(agentMap, tradeableCommodities);
	}
	
	public boolean removeBrokeAgents() {
		Iterator agentIt = agents.iterator();
		while (agentIt.hasNext()) {
			Agent a = (Agent)agentIt.next();
			if (a.getMoney() < 1)
				agentIt.remove();
		}
		
		
		return agents.size() > 0;
	}
	
	//the TRUE market value should be the average of what agents are willing to pay for it
	public int getAveragePriceBelief(String c) {
		int totalCost = 0, noAgent = 0;
		
		
		return noAgent > 0 ? Math.round(totalCost / noAgent) : defaultAveragePrice;
	}
	
	//if no agents have the commodity, we can't give a true market value
	public boolean isCommodityAvailable(String c) {
		for (Agent a: agents) {
			if (a.getInventoryCount(c) > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	public void loadAgentTemplates() {
		
		String txtFileName = agentDataCsvFileName;
		boolean fileOpened = false; 
		ArrayList<String> csvLines = null;
		//below are the rules to be turned into behaviors, then agents
		ArrayList<ProductionRule> rules = new ArrayList<ProductionRule>();
		LinkedHashMap<String, ArrayList<ProductionRule>> ruleByAgent = new LinkedHashMap<String, ArrayList<ProductionRule>>();
		//the behaviors
		ArrayList<ProductionBehavior> behaviors = new ArrayList<ProductionBehavior>();
		
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
		
		//sort rules by agen
		for (ProductionRule rule: rules) {
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
		
	    //finally, add the production behaviors into agent templates
    	for (ProductionBehavior pb: behaviors) {
	    	Agent a = new Agent(pb.getAgentType(), pb);
	    	agentTemplates.add(a);	
	    }
	}
	
	
	//using below for unit testing
	public ArrayList<Agent> getAgents() {
		return agents;
	}
	
	public ArrayList<Offer> getBidBook() {
		return house.getBidBook();
	}
	public ArrayList<Offer> getAskBook() {
		return house.getAskBook();
	}
	public ArrayList<SaleRecord> getHistoricalRecords() {
		return house.getHistoricalRecords();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Market status:\n");
		sb.append("Turn number: " + house.getTurnNumber() + "\n");
		sb.append("Active agents:" + agents.size() + "\n");
		sb.append("Average prices:\n");
		for (String comm: tradeableCommodities) {
			sb.append(comm + ": " + house.getHistoricalPriceMean(comm) + "\n");
		}
		
		return sb.toString();
	}
	

}