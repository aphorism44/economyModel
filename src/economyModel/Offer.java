package economyModel;

import java.util.UUID;

public class Offer {
	
	private UUID offerId; //identifier
	private String offerType; //bid or ask
	private String commodityType;
	private UUID agentId; //either the seller or the buyer
	private int quantity; //bid = amount offered; ask = amount desired
	private int turnNumber; //keep track of when offer was made
	private int offer;
	private boolean offerAccepted;
	private boolean partlyAccepted;
	private String creatorAgentType;
	
	public Offer(UUID aId, String type, String commodity, int quant, int bid, int turnNo, String agentType) {
		offerId = UUID.randomUUID();
		offerType = type;
		commodityType = commodity.toLowerCase();
		quantity = quant;
		offer = bid;
		offerAccepted = false;
		partlyAccepted = false;
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
	
	public int getOffer() {
		return offer;
	}
	
	public String getOfferType() {
		return offerType;
	}
	
	public UUID getAgentId() {
		return agentId;
	}
	
	public UUID getId() {
		return offerId;
	}
	
	public void acceptOffer() {
		partlyAccepted = false;
		offerAccepted = true;
	}
	
	public void acceptPartialOffer() {
		partlyAccepted = true;
	}
	
	public int getPricePerUnit() {
		if (quantity > 0)
			return offer / quantity;
		else
			return offer;
	}
	
	//should only take negative values; keep as is in case changes are needed
	public void updateQuantity(int n) {
		quantity += n;
	}
	
	public boolean getOfferAccepted() {
		return offerAccepted;
	}
	
	public boolean getOfferPartlyAccepted() {
		return partlyAccepted;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("offerType: " + offerType);
		s.append(", commodity: " + commodityType);
		s.append(", quantity: " + quantity);
		s.append(", price: " + offer);
		s.append(", Id: " + offerId);
		s.append(", accepted fully: " + offerAccepted);
		s.append(", accepted partly: " + partlyAccepted);
		s.append(", agentType: " + creatorAgentType);
		s.append(", agentId: " + agentId + "\n");
		
		return s.toString();
	}
	
}
