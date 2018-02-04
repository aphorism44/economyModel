package economyModel;

import enums.ProductionAction;
import enums.ProductionRuleType;

public class ProductionRule {
	
	private String agentType;
	private String commodity;
	private int quantity;
	private ProductionRuleType productionType;
	private ProductionAction produceAction;
	private String matchedCommodity = null;
	
	
	public ProductionRule(String atype, String comm, int q, String type, String produce, String match) {
		agentType = atype.toLowerCase().trim();
		commodity = comm.toLowerCase().trim();
		quantity = q;
		matchedCommodity = match != null ? match.toLowerCase().trim() : null;
		
		switch (type.toLowerCase().trim()) {
		case "full":
			productionType = ProductionRuleType.FULL;
			break;
		case "partial":
			productionType = ProductionRuleType.PARTIAL;
			break;
		case "fine":
			productionType = ProductionRuleType.FINE;
			break;
		case "upkeep":
			productionType = ProductionRuleType.UPKEEP;
			break;
		case "tools":
			productionType = ProductionRuleType.TOOLS;
			break;
		default:
			productionType = ProductionRuleType.ERROR;
			break;
		}
		
		switch (produce.toLowerCase().trim()) {
		case "consume":
			produceAction = ProductionAction.CONSUME;
			break;
		case "produce":
			produceAction = ProductionAction.PRODUCE;
			break;
		case "check":
			quantity = 1;
			produceAction = ProductionAction.CHECK;
			break;
		case "fine":
			quantity = q;
			produceAction = ProductionAction.FINE;
			break;
		case "consume_all":
			quantity = 0;
			produceAction = ProductionAction.CONSUME_ALL;
			break;
		default:
			produceAction = ProductionAction.ERROR;
			break;
		}
		
	}
	
	
	public boolean isValidRule() {
		
		return !productionType.equals(ProductionRuleType.ERROR) && !produceAction.equals(ProductionAction.ERROR);
	}
	
	public String getCommodity() {
		return commodity;
	}
	public String getAgentType() {
		return agentType;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public String getMatchedCommodity() {
		return matchedCommodity;
	}
	public ProductionRuleType getRuleType() {
		return productionType;
	}
	public ProductionAction getProductionAction() {
		return produceAction;
	}
	
	public String toString() {
		StringBuffer sOut = new StringBuffer();
		sOut.append("ProductionRule object: ");
		sOut.append("agentType: " + agentType + ", ");
		sOut.append("commodity: " + commodity + ", ");
		sOut.append("productionType: " + productionType + ", ");
		sOut.append("produceAction: " + produceAction + ", ");
		sOut.append("quantity: " + quantity);
		if (matchedCommodity != null)
			sOut.append(", matchedCommodity: " + matchedCommodity);
		
		return sOut.toString();
	}
	

}
