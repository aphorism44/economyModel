package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RefinerProduction implements ProductionBehavior {
	
	private static ArrayList<String> producedCommodities;
	static {
		producedCommodities = new ArrayList<String>();
		producedCommodities.add("metal");
	}
	private static ArrayList<String> consumedCommodities;
	static {
		consumedCommodities = new ArrayList<String>();
		consumedCommodities.add("food");
		consumedCommodities.add("ore");
	}
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		int ore = (int)inventory.get("ore");
		int metal = (int)inventory.get("metal");
		int food = (int)inventory.get("food");
		int money = (int)inventory.get("money");
		
		if (hasTools && food > 0) {
			metal += ore;
			ore = 0;
			food -= 1;
		} else if (!hasTools && food > 0) {
			int oreUsed = Math.min(ore, 2);
			metal += oreUsed;
			ore -= oreUsed;
		} else {
			money -= 2;
		}
		
		inventory.put("ore", ore);
		inventory.put("metal", metal);
		inventory.put("food", food);
		inventory.put("money", money);
		
		return inventory;
	}
	
	public ArrayList<String> getProducedCommodities() {
		return this.producedCommodities;
	}
	
	public ArrayList<String> getConsumedCommodities() {
		return this.consumedCommodities;
	}

}
