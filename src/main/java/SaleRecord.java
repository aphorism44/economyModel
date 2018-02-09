package main.java;

public class SaleRecord {
	
	private String offerType; //bid or ask
	private String commodityType;
	private int quantity; //bid = amount offered; ask = amount desired
	private int turnNumber; //keep track of when offer was made
	private int offer; //individual price
	private String buyerAgentType;
	private String sellerAgentType;
	
	public SaleRecord(String type, String commodity, int quant, int bid, int turnNo, String seller, String buyer) {
		offerType = type;
		commodityType = commodity.toLowerCase();
		quantity = quant;
		offer = bid;
		turnNumber = turnNo;
		sellerAgentType = seller;
		buyerAgentType = buyer;
	}
	
	public String getCommodityType() {
		return commodityType;
	}
	public int getOffer() {
		return offer;
	}
	public String getOfferType() {
		return offerType;
	}
	public int getTurnNumber() {
		return turnNumber;
	}
	public int getQuantity() {
		return quantity;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("offerType: " + offerType);
		s.append(", buyerAgentType: " + buyerAgentType);
		s.append(", sellerAgentType: " + sellerAgentType);
		s.append(", turn: " + turnNumber);
		s.append(", commodity: " + commodityType);
		s.append(", quantity: " + quantity);
		s.append(", price: " + offer);
		
		return s.toString();
	}
	
}
