package economyModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import enums.ProductionAction;
import enums.ProductionRuleType;

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
		
		this.agentType = rules.get(0).getAgentType();
		this.producedCommodities =  new HashSet<String>(); 
		this.consumedCommodities =  new HashSet<String>(); 
		this.fullProduction = new ArrayList<ProductionRule>();
		this.partialProduction = new ArrayList<ProductionRule>(); 
		this.upkeepCommodities = new HashSet<String>(); 
		
		for (ProductionRule r: rules) {
			if (r.getAgentType().equals(this.agentType)) {
				ProductionAction pAction = r.getProductionAction();
				ProductionRuleType prType = r.getRuleType();
				String commName = r.getCommodity();
				String matchCommodity = r.getMatchedCommodity(); //may be null
				
				switch (prType) {
				case FULL:
					this.fullProduction.add(r);
					break;
				case PARTIAL:
					this.partialProduction.add(r);
					break;
				case FINE:
					this.fine = r.getQuantity();
					break;
				case UPKEEP:
					this.upkeepCommodities.add(commName);
					break;
				case TOOLS:
					int toolCheck = r.getQuantity();
					this.usesTools = toolCheck == 1 ? true: false;
					break;
				}
				
				switch (pAction) {
				case CONSUME:
					this.consumedCommodities.add(commName);
					if (matchCommodity != null)
						this.producedCommodities.add(matchCommodity);
					break;
				case PRODUCE:
					this.producedCommodities.add(commName);
					break;
				case CONSUME_ALL:
					this.consumedCommodities.add(commName);
					if (matchCommodity != null)
						this.producedCommodities.add(matchCommodity);
					break;
				}
				
			}
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
		
		//System.out.println(hasUpkeep);
		//System.out.println(lacksTools);
		
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
		return new ArrayList<String>(this.producedCommodities);
	}
	public ArrayList<String> getConsumedCommodities() {
		return new ArrayList<String>(this.consumedCommodities);
	}
	
	public String toString() {
		StringBuffer sOut = new StringBuffer();
		sOut.append("ProductionBehavior object: ");
		sOut.append("agentType: " + this.agentType + ", ");
		sOut.append("usesTools: " + this.usesTools + ", ");
		sOut.append("fine: " + this.fine + ", ");
		
		sOut.append("\nfull production rules: ");
		for (ProductionRule pr1: this.fullProduction)
			sOut.append(pr1.toString());
		sOut.append("\npartial production rules: ");
		for (ProductionRule pr2: this.partialProduction)
			sOut.append(pr2.toString());
		sOut.append("\nproduced commodities: ");
		for (String ps: this.producedCommodities)
			sOut.append(ps);
		sOut.append("\nconsumed commodities: ");
		for (String cc: this.consumedCommodities)
			sOut.append(cc);
		sOut.append("\nupkeep commidities: ");
		for (String uc: this.upkeepCommodities)
			sOut.append(uc);
		
		return sOut.toString();
	}
	
	public String getAgentType() {
		return this.agentType;
	}
	
	public boolean getUsesTools() {
		return this.usesTools;
	}
	
	//for testing
	public boolean isValid() {
		return this.producedCommodities.size() > 0 
				&& this.consumedCommodities.size() > 0
				&& (this.fullProduction.size() > 0 || this.partialProduction.size() > 0)
				&& this.upkeepCommodities.size() > 0
				&& this.agentType.length() > 0
				&& this.fine > 0;
	}

	
}
