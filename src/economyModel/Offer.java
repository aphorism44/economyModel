package economyModel;

import java.util.UUID;

public class Offer {
	
	private int offerType; //0 = bid, 1 = ask
	private String commodityType;
	private UUID agentId; //either the seller or the buyer
	private int quantity; //bid = amount offered; ask = amount desired
	private int turnNumber; //keep track of when offer was made
	private double offer;
	private boolean offerAccepted;
	
	public Offer(UUID aId, int type, String commodity, int quant, double bid, int turnNo) {
		this.offerType = type;
		this.commodityType = commodity.toLowerCase();
		this.quantity = quant;
		this.offer = bid;
		this.offerAccepted = false;
		this.agentId = aId;
		this.turnNumber = turnNo;
	}
	
	public boolean isOfCommodity(String c) {
		if (c.toLowerCase().equals(this.commodityType))
			return true;
		return false;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public String getCommodityType() {
		return this.commodityType;
	}
	
	public double getOffer() {
		return this.offer;
	}
	
	public UUID getId() {
		return this.agentId;
	}
	
	public void acceptOffer() {
		this.offerAccepted = true;
	}
	
	public double getPricePerUnit() {
		return this.offer / this.quantity;
	}
	
	//should only take negative values; keep as is in case changes are needed
	public void updateQuantity(int n) {
		this.quantity += n;
	}
	
	public boolean getOfferAccepted() {
		return this.offerAccepted;
	}
	
}
