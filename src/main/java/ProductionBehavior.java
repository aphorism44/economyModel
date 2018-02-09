package main.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import main.java.enums.ProductionAction;
import main.java.enums.ProductionRuleType;

public class ProductionBehavior {
	
	private static final double toolBreakagePercentage = 0.1;

	private HashSet<String> producedCommodities;
	private HashSet<String> consumedCommodities;
	
	private ArrayList<ProductionRule> fullProduction;
	private ArrayList<ProductionRule> partialProduction;
	
	private HashSet<String> upkeepCommodities;
	
	private String agentType;
	private boolean usesTools;
	private int fine;
	
	
	public ProductionBehavior(ArrayList<ProductionRule> rules) {
		
		agentType = rules.get(0).getAgentType();
		producedCommodities =  new HashSet<String>(); 
		consumedCommodities =  new HashSet<String>(); 
		fullProduction = new ArrayList<ProductionRule>();
		partialProduction = new ArrayList<ProductionRule>(); 
		upkeepCommodities = new HashSet<String>(); 
		
		for (ProductionRule r: rules) {
			if (r.getAgentType().equals(agentType)) {
				ProductionAction pAction = r.getProductionAction();
				ProductionRuleType prType = r.getRuleType();
				String commName = r.getCommodity();
				String matchCommodity = r.getMatchedCommodity(); //may be null
				
				switch (prType) {
				case FULL:
					fullProduction.add(r);
					break;
				case PARTIAL:
					partialProduction.add(r);
					break;
				case FINE:
					fine = r.getQuantity();
					break;
				case UPKEEP:
					upkeepCommodities.add(commName);
					break;
				case TOOLS:
					int toolCheck = r.getQuantity();
					usesTools = toolCheck == 1 ? true: false;
					break;
				}
				
				switch (pAction) {
				case CONSUME:
					consumedCommodities.add(commName);
					if (matchCommodity != null)
						producedCommodities.add(matchCommodity);
					break;
				case PRODUCE:
					producedCommodities.add(commName);
					break;
				case CONSUME_ALL:
					consumedCommodities.add(commName);
					if (matchCommodity != null)
						producedCommodities.add(matchCommodity);
					break;
				}
				
			}
		}
	}
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory) {
		
		boolean hasUpkeep = true;
		boolean lacksTools = false;
		
		//upkeep is usually food
		for (String up: upkeepCommodities) {
			if (!inventory.containsKey(up) || inventory.get(up) < 1) {
				hasUpkeep = false;
				break;
			}
		}
		
		//not all agents use tools, so only update as needed
		if (usesTools)
			if (!inventory.containsKey("tools") || inventory.get("tools") < 1)
				lacksTools = true;
		
		//System.out.println(hasUpkeep);
		//System.out.println(lacksTools);
		
		//full production occurs when agent has basic upkeep and don't lack tools
		if (hasUpkeep && !lacksTools) {
			inventory = updateInventory(fullProduction, inventory);
			//if the agent uses tools, full production has a chance of breaking the tool
			if (usesTools) {
				double breakRandom = Math.random();
				if (breakRandom < toolBreakagePercentage)
					inventory.put("tools", 0);
			}
		//partial production occurs when agent has basic upkeep but lack tools
		} else if (hasUpkeep && lacksTools) {
			inventory = updateInventory(partialProduction, inventory);
		//without basic upkeep, agent is fined
		} else {
			inventory.put("fine", fine);
		}
		
		return inventory;
		
	}
	
	public LinkedHashMap<String, Integer> updateInventory(ArrayList<ProductionRule> production, LinkedHashMap<String, Integer> inventory) {
		for (ProductionRule pr: production) {
			int amount = pr.getQuantity();
			String commodity = pr.getCommodity();
			ProductionAction action = pr.getProductionAction();
			int currentInventory = inventory.get(commodity);
			String match = null;
			switch(action) {
			case CONSUME:
				currentInventory -= amount;
				inventory.put(commodity, currentInventory);
				break;
			case PRODUCE:
				currentInventory += amount;
				inventory.put(commodity, currentInventory);
				break;
			case CONSUME_ALL:
				//System.out.println("consume all: " + commodity);
				match = pr.getMatchedCommodity();
				//System.out.println("to make: " + match);
				int otherInventory = inventory.get(match);
				//System.out.println("otherInventory: " + otherInventory);
				//System.out.println("currentInventory: " + currentInventory);
				otherInventory += currentInventory;
				inventory.put(commodity, 0);
				inventory.put(match, otherInventory);
				break;
			}
		}
		
		
		return inventory;
	}
	
	public ArrayList<String> getProducedCommodities() {
		return new ArrayList<String>(producedCommodities);
	}
	public ArrayList<String> getConsumedCommodities() {
		return new ArrayList<String>(consumedCommodities);
	}
	
	public String toString() {
		StringBuffer sOut = new StringBuffer();
		sOut.append("ProductionBehavior object: ");
		sOut.append("agentType: " + agentType + ", ");
		sOut.append("usesTools: " + usesTools + ", ");
		sOut.append("fine: " + fine + ", ");
		
		sOut.append("\nfull production rules: ");
		for (ProductionRule pr1: fullProduction)
			sOut.append(pr1.toString());
		sOut.append("\npartial production rules: ");
		for (ProductionRule pr2: partialProduction)
			sOut.append(pr2.toString());
		sOut.append("\nproduced commodities: ");
		for (String ps: producedCommodities)
			sOut.append(ps);
		sOut.append("\nconsumed commodities: ");
		for (String cc: consumedCommodities)
			sOut.append(cc);
		sOut.append("\nupkeep commidities: ");
		for (String uc: upkeepCommodities)
			sOut.append(uc);
		
		return sOut.toString();
	}
	
	public String getAgentType() {
		return agentType;
	}
	
	public boolean getUsesTools() {
		return usesTools;
	}
	
	public int getFine() {
		return fine;
	}
	
	//for testing
	public boolean isValid() {
		return producedCommodities.size() > 0 
				&& consumedCommodities.size() > 0
				&& (fullProduction.size() > 0 || partialProduction.size() > 0)
				&& upkeepCommodities.size() > 0
				&& agentType.length() > 0
				&& fine > 0;
	}

	
}
