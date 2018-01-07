package economyModel;

import java.util.LinkedHashMap;

public class FarmerProduction implements ProductionBehavior {
	
	public LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools) {
		int wood = (int)inventory.get("wood");
		int food = (int)inventory.get("food");
		int money = (int)inventory.get("money");
		
		if (hasTools && wood > 0) {
			food += 4;
			wood -= 1;
		} else if (!hasTools && wood > 0) {
			food += 2;
			wood -=1;
		} else {
			money -= 2;
		}
		
		inventory.put("wood", wood);
		inventory.put("food", food);
		inventory.put("money", money);
		
		return inventory;
	}

}
