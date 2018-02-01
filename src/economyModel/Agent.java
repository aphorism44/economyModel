package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Agent {

	private static final double startingMoney = 1000;
	private static final int inventorySize = 15;
	private static final int startingGoods = 8;
	
	
	private String agentType;
	UUID id;
	private double money;
	private LinkedHashMap<String, PriceBelief> priceBeliefs;
	private LinkedHashMap<String, Integer> inventory;
	private ArrayList<String> consumedItems;
	private ArrayList<String> producedItems;
	private ProductionBehavior productionBehavior;
	
	public Agent(String t, ProductionBehavior pb) {
		this.agentType = t;
		this.productionBehavior = pb;
		this.inventory = new LinkedHashMap<String, Integer>();
		this.money = this.startingMoney;
		this.id = UUID.randomUUID();
		this.priceBeliefs = new LinkedHashMap<String, PriceBelief>();
		this.consumedItems = pb.getConsumedCommodities();
		this.producedItems = pb.getProducedCommodities();
		//initial inventories and price beliefs
		for (String i: this.consumedItems) {
			inventory.put(i, startingGoods);
			PriceBelief consB = new PriceBelief(i);
			this.priceBeliefs.put(i, consB);
		}
		for (String i: this.producedItems) {
			inventory.put(i, 0);
			PriceBelief prodB = new PriceBelief(i);
			this.priceBeliefs.put(i, prodB);
		}
		if (productionBehavior.getUsesTools())
			inventory.put("tools", 1);
		
		
	}
	
	public void produce() {
		this.inventory = this.productionBehavior.produce(this.inventory);
		//make sure inventory doesn't exceed max spaces
		this.inventory.forEach((key, value) -> {
			if (value > this.inventorySize)
				value = this.inventorySize;
		});
	}
	
	public UUID getUuid() {
		return this.id;
	}
	
	public int getInventoryCount(String commmodity) {
		return this.inventory.get(commmodity);
	}
	
	public String getAgentType() {
		return this.agentType;
	}
	
	public int getExcessInventory(String commodity) {
		//get the count of matching produced items
		int excess = 0;
		if (this.producedItems.contains(commodity))
			excess = this.inventory.get(commodity);
		
		return excess;
	}
	
	public int getExcessSpace(String commodity) {
		//find out how much space is left for needed commodity
		int amount = 0, space = 0;
		if (this.consumedItems.contains(commodity)) {
			amount = this.inventory.get(commodity);
			space = this.inventorySize - amount;
		}
		
		return space;
	}
	
	public double getPriceBelief(String commodity) {
		double guessedBid = 0;
		//grab a random number within the price belief bounds
		if (this.priceBeliefs.containsKey(commodity))
			guessedBid = this.priceBeliefs.get(commodity).getRandomBid();
		
		return guessedBid;
	}
	
	//should return number between 0.0 and 1.0
	public double getCommodityFavorability(String commodity, double marketMean) {
		double favorability = 0.0;
		
		if (this.priceBeliefs.containsKey(commodity))
			favorability = this.priceBeliefs.get(commodity).getRangePenetration(marketMean);
		
		return favorability;
		
	}
	
	
	//n will be positive for buyers and negative for sellers
	public void updateInventory(String c, int n) {
		int inv = this.inventory.get(c);
		inv += n;
		inv = Math.min(inv, this.inventorySize);
		this.inventory.put(c, inv);
	}
	
	public void updateMoney(double n) {
		this.money += n;
	}
	
	//update price belief
	public void updatePriceBelief(Offer o) {
		String comm = o.getCommodityType();
		PriceBelief relevantBelief = this.priceBeliefs.get(comm);
		if (o.getOfferAccepted()) {
			relevantBelief.contractBounds();
		} else {
			//if they tried to purchase a required
			//consumed commodity and failed, there
			//will be a _drastic_ belief adjustment
		}
		
		this.priceBeliefs.put(comm, relevantBelief);
	}
	
	public double getMoney() {
		return this.money;
	}
	
	
	public LinkedHashMap<String, Integer> getInventory() {
		return this.inventory;
	}
	
	public ArrayList<String> getConsumedItems() {
		return this.consumedItems;
	}
	public ArrayList<String> getProducedItems() {
		return this.producedItems;
	}
	public int getMaxInventory() {
		return this.inventorySize;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(productionBehavior.toString());
		
		
		return sb.toString();
	}
	
}