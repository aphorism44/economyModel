package economyModel;

import java.util.LinkedHashMap;

public interface ProductionBehavior {

	abstract LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools);
	
}
