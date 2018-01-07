package economyModel;

import java.util.LinkedHashMap;

public class BlacksmithProduction implements ProductionBehavior {
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		int metal = (int)inventory.get("metal");
		int tools = (int)inventory.get("tools");
		int food = (int)inventory.get("food");
		int money = (int)inventory.get("money");
		
		if (food > 0) {
			tools += metal;
			metal = 0;
			food -= 1;
		} else {
			money -= 2;
		}
		
		inventory.put("metal", metal);
		inventory.put("tools", tools);
		inventory.put("food", food);
		inventory.put("money", money);
		
		return inventory;
	}

}
