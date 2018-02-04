package economyModel;

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
	//eventually, create enums for commodities and define from imported data
	private static ArrayList<String> tradeableCommodities = new ArrayList<String>() {{
		add("food");
		add("wood");
		add("ore");
		add("metal");
		add("tools");
	}};
	
	public Market(int agentMultiplier) {
		agents = new ArrayList<Agent>();
		house = new Clearinghouse();
		createAgents(agentMultiplier);
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
		for (String c: tradeableCommodities)
			house.resolveOffers(c, agentMap);
	}
	
	
	
	public void createAgents(int n) {
		
		String txtFileName = "C:\\Users\\Jesse\\eclipse-workspace\\economyModel\\src\\productionData.csv";
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
		
	    //finally, add the production behaviors into agents
	    for (int i = 0; i < n; i++) {
	    	for (ProductionBehavior pb: behaviors) {
		    	Agent a = new Agent(pb.getAgentType(), pb);
		    	agents.add(a);	
		    }
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
	public ArrayList<Offer> getHistoricalRecords() {
		return house.getHistoricalRecords();
	}
	

}