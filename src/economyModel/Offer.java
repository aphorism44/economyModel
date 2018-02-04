package economyModel;

import java.util.UUID;

public class Offer {
	
	private String offerType; //bid or ask
	private String commodityType;
	private UUID agentId; //either the seller or the buyer
	private int quantity; //bid = amount offered; ask = amount desired
	private int turnNumber; //keep track of when offer was made
	private double offer;
	private boolean offerAccepted;
	private String creatorAgentType;
	
	public Offer(UUID aId, String type, String commodity, int quant, double bid, int turnNo, String agentType) {
		offerType = type;
		commodityType = commodity.toLowerCase();
		quantity = quant;
		offer = bid;
		offerAccepted = false;
		agentId = aId;
		turnNumber = turnNo;
		creatorAgentType = agentType;
	}
	
	public boolean isOfCommodity(String c) {
		if (c.toLowerCase().equals(commodityType))
			return true;
		return false;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getCommodityType() {
		return commodityType;
	}
	
	public String getCreatorAgentType() {
		return creatorAgentType;
	}
	
	public double getOffer() {
		return offer;
	}
	
	public UUID getId() {
		return agentId;
	}
	
	public void acceptOffer() {
		offerAccepted = true;
	}
	
	public double getPricePerUnit() {
		return offer / quantity;
	}
	
	//should only take negative values; keep as is in case changes are needed
	public void updateQuantity(int n) {
		quantity += n;
	}
	
	public boolean getOfferAccepted() {
		return offerAccepted;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("offerType: " + offerType);
		s.append(", commodity: " + commodityType);
		s.append(", quantity: " + quantity);
		s.append(", price: " + offer);
		s.append(", agentType: " + creatorAgentType);
		s.append(", agentId: " + agentId + "\n");
		
		return s.toString();
	}
	
}
