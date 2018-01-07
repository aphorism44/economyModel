package economyModel;

import java.util.LinkedHashMap;

public class LumberjackProduction implements ProductionBehavior {
	
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

}
