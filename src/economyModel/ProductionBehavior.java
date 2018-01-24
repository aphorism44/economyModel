package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import enums.ProductionAction;
import enums.ProductionRuleType;

public class ProductionBehavior {
	
	private static final double toolBreakagePercentage = 0.1;

	private ArrayList<String> producedCommodities;
	private ArrayList<String> consumedCommodities;
	
	private ArrayList<ProductionRule> fullProduction;
	private ArrayList<ProductionRule> partialProduction;
	
	private ArrayList<String> upkeepCommodities;
	
	private String agentType;
	private boolean usesTools;
	private int fine;
	
	
	public ProductionBehavior(ArrayList<ProductionRule> rules) {
		
		this.agentType = rules.get(0).getAgentType();
		
		for (ProductionRule r: rules) {
			if (r.getAgentType().equals(this.agentType)) {
				ProductionAction pa = r.getProductionAction();
				ProductionRuleType pt = r.getRuleType();
				
				
				
			}
			//System.out.println(r.toString());
			
			
		}
			
		
		
		
	}
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory) {
		
		boolean hasUpkeep = true;
		boolean lacksTools = false;
		
		//upkeep is usually food
		for (String up: this.upkeepCommodities) {
			if (!inventory.containsKey(up) || inventory.get(up) < 1) {
				hasUpkeep = false;
				break;
			}
		}
		
		//not all agents use tools, so only update as needed
		if (this.usesTools)
			if (!inventory.containsKey("tools") || inventory.get("tools") < 1)
				lacksTools = true;
		
		//full production occurs when agent has basic upkeep and don't lack tools
		if (hasUpkeep && !lacksTools) {
			inventory = this.updateInventory(this.fullProduction, inventory);
			//if the agent uses tools, full production has a chance of breaking the tool
			if (this.usesTools) {
				double breakRandom = Math.random();
				if (breakRandom < this.toolBreakagePercentage)
					inventory.put("tools", 0);
			}
		//partial production occurs when agent has basic upkeep but lack tools
		} else if (hasUpkeep && lacksTools) {
			inventory = this.updateInventory(this.partialProduction, inventory);
		//without basic upkeep, agent is fined
		} else {
			int money = inventory.get("money");
			money -= this.fine;
			inventory.put("money", money);
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
				match = pr.getMatchedCommodity();
				int otherInventory = inventory.get(match);
				otherInventory += amount;
				inventory.put(commodity, 0);
				inventory.put(match, otherInventory);
				break;
			}
		}
		
		
		return inventory;
	}
	
	public ArrayList<String> getProducedCommodities() {
		return this.producedCommodities;
	}
	public ArrayList<String> getConsumedCommodities() {
		return this.consumedCommodities;
	}

	
}
