package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LumberjackProduction implements ProductionBehavior {
	
	private static ArrayList<String> producedCommodities;
	static {
		producedCommodities = new ArrayList<String>();
		producedCommodities.add("wood");
	}
	private static ArrayList<String> consumedCommodities;
	static {
		consumedCommodities = new ArrayList<String>();
		consumedCommodities.add("food");
	}
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		int wood = (int)inventory.get("wood");
		int food = (int)inventory.get("food");
		int money = (int)inventory.get("money");
		
		if (hasTools && food > 0) {
			wood += 2;
			food -= 1;
		} else if (!hasTools && food > 0) {
			wood += 1;
			food -=1;
		} else {
			money -= 2;
		}
		
		inventory.put("wood", wood);
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
