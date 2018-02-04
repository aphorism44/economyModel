package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Agent {

	private static final int startingMoney = 1000;
	private static final int inventorySize = 15;
	private static final int startingGoods = 8;
	private static final double replenishThreshold = 0.2;
	
	private String agentType;
	UUID id;
	private int money;
	private LinkedHashMap<String, PriceBelief> priceBeliefs;
	private LinkedHashMap<String, Integer> inventory;
	private ArrayList<String> consumedItems;
	private ArrayList<String> producedItems;
	private ProductionBehavior productionBehavior;
	private boolean usesTools;
	
	public Agent(String t, ProductionBehavior pb) {
		agentType = t;
		productionBehavior = pb;
		inventory = new LinkedHashMap<String, Integer>();
		money = startingMoney;
		id = UUID.randomUUID();
		priceBeliefs = new LinkedHashMap<String, PriceBelief>();
		consumedItems = pb.getConsumedCommodities();
		producedItems = pb.getProducedCommodities();
		usesTools = pb.getUsesTools();
		//initial inventories and price beliefs
		for (String i: consumedItems) {
			inventory.put(i, startingGoods);
			PriceBelief consB = new PriceBelief(i);
			priceBeliefs.put(i, consB);
		}
		
		for (String i: producedItems) {
			inventory.put(i, 0);
			PriceBelief prodB = new PriceBelief(i);
			priceBeliefs.put(i, prodB);
		}
		if (productionBehavior.getUsesTools()) {
			inventory.put("tools", 1);
			PriceBelief toolB = new PriceBelief("tools");
			priceBeliefs.put("tools", toolB);
		}
		
		
	}
	
	public void produce() {
		inventory = productionBehavior.produce(inventory);
		//handle fines
		if (inventory.containsKey("fine")) {
			money -= inventory.get("fine");
			inventory.remove("fine");
		}
		
		//make sure inventory doesn't exceed max spaces
		inventory.forEach((key, value) -> {
			if (value > inventorySize)
				value = inventorySize;
		});
	}
	
	public UUID getUuid() {
		return id;
	}
	
	public int getInventoryCount(String commmodity) {
		return inventory.get(commmodity);
	}
	
	public String getAgentType() {
		return agentType;
	}
	
	public int getExcessInventory(String commodity) {
		//get the count of matching produced items
		int excess = 0;
		if (producedItems.contains(commodity))
			excess = inventory.get(commodity);
		
		return excess;
	}
	
	public int getExcessSpace(String commodity) {
		//find out how much space is left for needed commodity
		int amount = 0, space = 0;
		if (consumedItems.contains(commodity)) {
			amount = inventory.get(commodity);
			space = inventorySize - amount;
		}
		
		return space;
	}
	
	//check if you're missing a tool
	public boolean needsTools() {
		int toolCount = inventory.get("tools");
		return usesTools && toolCount < 1;
	}
	
	public int getPriceBelief(String commodity) {
		int guessedBid = 0;
		//grab a random number within the price belief bounds
		if (priceBeliefs.containsKey(commodity))
			guessedBid = priceBeliefs.get(commodity).getRandomBid();
		
		return guessedBid;
	}
	
	//should return number between 0.0 and 1.0
	public double getCommodityFavorability(String commodity, double marketMean) {
		double favorability = 0.0;
		
		if (priceBeliefs.containsKey(commodity))
			favorability = priceBeliefs.get(commodity).getRangePenetration(marketMean);
		
		return favorability;
		
	}
	
	
	//n will be positive for buyers and negative for sellers
	public void updateInventory(String c, int n) {
		int inv = inventory.get(c);
		inv += n;
		inv = Math.min(inv, inventorySize);
		inventory.put(c, inv);
	}
	
	public void updateMoney(int n) {
		money += n;
	}
	
	//update price belief
	public void updatePriceBelief(Offer o, int mean) {
		String comm = o.getCommodityType();
		String type = o.getOfferType();
		PriceBelief relevantBelief = priceBeliefs.get(comm);
		if (o.getOfferAccepted() || o.getOfferPartlyAccepted()) {
			relevantBelief.contractBounds();
		} else {
			//if they tried to purchase a required
			//consumed commodity and failed, there
			//will be a _drastic_ belief adjustment
			boolean lowInventory = false;
			int currentInventory = getInventoryCount(comm);
			if (type.equals("bid") && currentInventory < inventorySize * replenishThreshold)
				lowInventory = true;
			relevantBelief.expandBounds(lowInventory, mean);
			
		}
		
		priceBeliefs.put(comm, relevantBelief);
	}
	
	public int getMoney() {
		return money;
	}
	
	
	public LinkedHashMap<String, Integer> getInventory() {
		return inventory;
	}
	
	public ArrayList<String> getConsumedItems() {
		ArrayList<String> cItems = (ArrayList<String>)consumedItems.clone();
		return cItems;
	}
	public ArrayList<String> getProducedItems() {
		ArrayList<String> pItems = (ArrayList<String>)producedItems.clone();
		return pItems;
	}
	public int getMaxInventory() {
		return inventorySize;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(productionBehavior.toString());
		sb.append(", money:" + money);
		sb.append(", inventory:");
		inventory.forEach((key, value) -> {
			sb.append(key + ":" + value + " ");
		});
		
		return sb.toString();
	}
	
	public String getInventoryString() {
		StringBuilder sb = new StringBuilder();
		sb.append("agentType: " + agentType);
		sb.append(", money:" + money);
		sb.append(", inventory:");
		inventory.forEach((key, value) -> {
			sb.append(key + ":" + value + " ");
		});
		
		return sb.toString();
	}
	
}