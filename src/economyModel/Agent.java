package economyModel;

import java.util.LinkedHashMap;
import java.util.UUID;

public class Agent {

	private static final double startingMoney = 1000;
	private static final int inventorySize = 15;
	
	private String agentType;
	UUID id;
	private double money;
	private boolean hasTools;
	private double lowerPriceBound;
	private double upperPriceBound;
	private LinkedHashMap<String, Integer> inventory;
	protected ProductionBehavior productionBehavior;
	
	public Agent(String t, ProductionBehavior pb) {
		this.agentType = t;
		this.productionBehavior = pb;
		this.inventory = new LinkedHashMap<String, Integer>();
		this.lowerPriceBound = 0;
		this.upperPriceBound = 0;
		this.money = this.startingMoney;
		//uuid
		this.id = UUID.randomUUID();
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
		this.inventory.put(c, inv);
	}
	
	public void updateMoney(double n) {
		this.money += n;
	}
	
	
	
	
}