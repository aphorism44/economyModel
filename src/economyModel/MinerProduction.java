package economyModel;

import java.util.LinkedHashMap;

public class MinerProduction implements ProductionBehavior {
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		int ore = (int)inventory.get("ore");
		int food = (int)inventory.get("food");
		int money = (int)inventory.get("money");
		if (hasTools && food > 0) {
			ore += 4;
			food -= 1;
		} else if (!hasTools && food > 0) {
			ore += 2;
			food -=1;
		} else {
			money -= 2;
		}
		
		inventory.put("ore", ore);
		inventory.put("food", food);
		inventory.put("money", money);
		
		return inventory;
	}

}
