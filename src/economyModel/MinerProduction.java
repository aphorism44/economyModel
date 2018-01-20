package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MinerProduction implements ProductionBehavior {
	
	private static ArrayList<String> producedCommodities;
	static {
		producedCommodities = new ArrayList<String>();
		producedCommodities.add("ore");
	}
	private static ArrayList<String> consumedCommodities;
	static {
		consumedCommodities = new ArrayList<String>();
		consumedCommodities.add("food");
	}
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		
		int ore = inventory.containsKey("ore") ? (int)inventory.get("ore") : 0;
		int food = inventory.containsKey("food") ? (int)inventory.get("food") : 0;;
		int taxes = 0;
		
		if (hasTools && food > 0) {
			ore += 4;
			food -= 1;
		} else if (!hasTools && food > 0) {
			ore += 2;
			food -=1;
		} else {
			taxes -= 2;
		}
		
		inventory.put("ore", ore);
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
