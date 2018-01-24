package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class BlacksmithProduction implements ProductionBehavior {
	
	private static ArrayList<String> producedCommodities;
	static {
		producedCommodities = new ArrayList<String>();
		producedCommodities.add("tools");
	}
	private static ArrayList<String> consumedCommodities;
	static {
		consumedCommodities = new ArrayList<String>();
		consumedCommodities.add("food");
		consumedCommodities.add("metal");
	}
	
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		
		int metal = inventory.containsKey("metal") ? (int)inventory.get("metal") : 0;
		int tools = inventory.containsKey("tools") ? (int)inventory.get("tools") : 0;;
		int food = inventory.containsKey("food") ? (int)inventory.get("food") : 0;
		int taxes = 0;
		
		if (food > 0) {
			tools += metal;
			metal = 0;
			food -= 1;
		} else {
			taxes -= 2;
		}
		
		inventory.put("metal", metal);
		inventory.put("tools", tools);
		inventory.put("food", food);
		inventory.put("taxMoney", taxes);
		
		return inventory;
	}
	
	public ArrayList<String> getProducedCommodities() {
		return this.producedCommodities;
	}
	
	public ArrayList<String> getConsumedCommodities() {
		return this.consumedCommodities;
	}

}
