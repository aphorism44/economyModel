package economyModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface ProductionBehavior {

	abstract ArrayList<String> getProducedCommodities();
	abstract ArrayList<String> getConsumedCommodities();
	abstract LinkedHashMap<String, Integer> produce(LinkedHashMap<String, Integer> inventory, boolean hasTools);
	
}
