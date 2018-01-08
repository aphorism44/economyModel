package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Agent {

	private static final double startingMoney = 1000;
	private static final int inventorySize = 15;
	
	private String agentType;
	UUID id;
	private double money;
	private boolean hasTools;
	private LinkedHashMap<String, PriceBelief> priceBeliefs;
	private LinkedHashMap<String, Integer> inventory;
	private ArrayList<String> consumedItems;
	private ArrayList<String> producedItems;
	protected ProductionBehavior productionBehavior;
	
	public Agent(String t, ProductionBehavior pb) {
		this.agentType = t;
		this.productionBehavior = pb;
		this.inventory = new LinkedHashMap<String, Integer>();
		this.money = this.startingMoney;
		this.id = UUID.randomUUID();
		this.priceBeliefs = new LinkedHashMap<String, PriceBelief>();
		this.consumedItems = pb.getConsumedCommodities();
		this.producedItems = pb.getProducedCommodities();
		for (String i: this.consumedItems) {
			
		}
		
	}
	
	public UUID getUuid() {
		return this.id;
	}
	
	public int getInventoryCount(String commmodity) {
		return this.inventory.get(commmodity);
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
	
	
}