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
	
	
	public ProductionRule(String agentType, String comm, int q, String type, String produce, String match) {
		this.agentType = agentType.toLowerCase().trim();
		this.commodity = comm.toLowerCase().trim();
		this.quantity = q;
		this.matchedCommodity = match != null ? match.toLowerCase().trim() : null;
		
		switch (type.toLowerCase().trim()) {
		case "full":
			this.productionType = ProductionRuleType.FULL;
			break;
		case "partial":
			this.productionType = ProductionRuleType.PARTIAL;
			break;
		case "fine":
			this.productionType = ProductionRuleType.FINE;
			break;
		case "upkeep":
			this.productionType = ProductionRuleType.UPKEEP;
			break;
		case "tools":
			this.productionType = ProductionRuleType.TOOLS;
			break;
		default:
			this.productionType = ProductionRuleType.ERROR;
			break;
		}
		
		switch (produce.toLowerCase().trim()) {
		case "consume":
			this.produceAction = ProductionAction.CONSUME;
			break;
		case "produce":
			this.produceAction = ProductionAction.PRODUCE;
			break;
		case "check":
			this.quantity = 1;
			this.produceAction = ProductionAction.CHECK;
			break;
		case "consume_all":
			this.quantity = 0;
			this.produceAction = ProductionAction.CONSUME_ALL;
			break;
		default:
			this.produceAction = ProductionAction.ERROR;
			break;
		}
		
	}
	
	
	public boolean isValidRule() {
		
		return !this.productionType.equals(ProductionRuleType.ERROR) && !this.produceAction.equals(ProductionAction.ERROR);
	}
	
	public String getCommodity() {
		return this.commodity;
	}
	public String getAgentType() {
		return this.agentType;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	public String getMatchedCommodity() {
		return this.matchedCommodity;
	}
	public ProductionRuleType getRuleType() {
		return this.productionType;
	}
	public ProductionAction getProductionAction() {
		return this.produceAction;
	}
	
	public String toString() {
		StringBuffer sOut = new StringBuffer();
		sOut.append("ProductionRule object: ");
		sOut.append("agentType: " + this.agentType + ", ");
		sOut.append("commodity: " + this.commodity + ", ");
		sOut.append("productionType: " + this.productionType + ", ");
		sOut.append("produceAction: " + this.produceAction + ", ");
		sOut.append("quantity: " + this.quantity);
		if (this.matchedCommodity != null)
			sOut.append(", matchedCommodity: " + this.matchedCommodity);
		
		return sOut.toString();
	}
	

}
