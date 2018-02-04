package economyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.OptionalDouble;

public class Clearinghouse {

	private ArrayList<Offer> historicalRecords;
	private ArrayList<Offer> bidBook;
	private ArrayList<Offer> askBook;
	private int turnNumber;
	
	
	public Clearinghouse() {
		historicalRecords = new ArrayList<Offer>();
		bidBook = new ArrayList<Offer>();
		askBook = new ArrayList<Offer>();
		turnNumber = 1;
	}
	
	//looks at agents to find ones who should make an offer
	public void createOffers(ArrayList<Agent> agents) {
		
		for (Agent a: agents) {
			ArrayList<String> consumedItems = a.getConsumedItems();
			ArrayList<String> producedItems = a.getProducedItems();
			
			for (String c: consumedItems) {
				createBid(a, "bid", c,  a.getMaxInventory());
			}
			for (String p: producedItems) {
				createBid(a, "ask", p,  a.getMaxInventory());
			}
			
			//we need to handle consumed tools differently
			if (a.needsTools())
				createBid(a, "bid", "tools", 1);
			
			
		}
		
	}
	
	
	public void createBid(Agent a, String type, String comm, int limit) {
		double bid = a.getPriceBelief(comm);
		int idealAmount = 0;
		
		//you can only buy one tool at a time
		if (comm.equals("tools") && type.equals("bid"))
			idealAmount = 1;
		else
			idealAmount =    determineQuantity(a, comm, type);
		
		int quant = Math.min(idealAmount, limit);
		if (quant > 0) {
			Offer newBid = new Offer(a.getUuid(), type, comm, quant, bid, turnNumber, a.getAgentType());
			switch(type) {
				case "bid":
					bidBook.add(newBid);
					break;
				case "ask":
					askBook.add(newBid);
					break;
			}
		}
	}
	
	
	public int determineQuantity(Agent a, String comm, String offerType) {
		double mean = getHistoricalPriceMean(comm);
		double favorability = a.getCommodityFavorability(comm, mean);
		int excessInventory = a.getExcessInventory(comm);
		int availableSpace = a.getExcessSpace(comm);
		int quantity = 0;
		
		if (offerType.equalsIgnoreCase("ask"))
			quantity = (int)Math.floor(favorability * excessInventory);
		else if (offerType.equalsIgnoreCase("bid"))
			quantity = (int)Math.floor(favorability * availableSpace);
		
		return quantity;
	}
	
	public double getHistoricalPriceMean(String c) {
		//let's hear it for lambda!
		if (historicalRecords.size() > 0) {
			ArrayList<Offer> commodityOffers = (ArrayList<Offer>) historicalRecords.stream().filter(o->o.getCommodityType().equals(c));
			OptionalDouble avg = commodityOffers.stream().mapToDouble(o->o.getPricePerUnit()).average();
			if (avg.isPresent())
				return avg.getAsDouble();
		}
		//returns below when there's no returned bids yet
		return 0;
	}
	
	public double getHistoricalMaxPrice(String c) {
		if (historicalRecords.size() > 0) {
			ArrayList<Offer> commodityOffers = (ArrayList<Offer>) historicalRecords.stream().filter(o->o.getCommodityType().equals(c));
			OptionalDouble max = commodityOffers.stream().mapToDouble(o->o.getPricePerUnit()).max();
			if (max.isPresent())
				return max.getAsDouble();
		}
		//returns below when there's no returned bids yet
		return 0;
	}
	
	
	public void resolveOffers(String commodity, LinkedHashMap<UUID, Agent> agents ) {
		//grab all bids for inputed commodity;
		ArrayList<Offer> bids= new ArrayList<Offer>();
		ArrayList<Offer> asks = new ArrayList<Offer>();
		
		for (Offer o: bidBook) {
			if (o.isOfCommodity(commodity))
				bids.add(o);
		}
		for (Offer o: askBook) {
			if (o.isOfCommodity(commodity))
				asks.add(o);
		}
		//shuffle then order by amount (decreasing for bid, increasing for ask)
		Collections.shuffle(bids);
		Collections.shuffle(asks);
		
		bids.sort((Offer a, Offer b)->a.getQuantity() - b.getQuantity());
		asks.sort((Offer a, Offer b)->b.getQuantity() - a.getQuantity());
		
		//trade loop
		while (!bids.isEmpty() && !asks.isEmpty()) {
			Offer bid = bids.get(0);
			Offer ask = asks.get(0);
			int tradeQuantity = Math.min(bid.getQuantity(), ask.getQuantity());
			double clearingPrice = (bid.getOffer() + ask.getOffer()) / 2.0;
			
			if (tradeQuantity > 0) {
				Agent buyer = agents.get(bid.getId());
				Agent seller = agents.get(ask.getId());
				double cost = tradeQuantity * clearingPrice;
				seller.updateInventory(commodity, -tradeQuantity);
				seller.updateMoney(clearingPrice);
				buyer.updateInventory(commodity, tradeQuantity);
				buyer.updateMoney(-clearingPrice);
				bid.updateQuantity(-tradeQuantity);
				ask.updateQuantity(-tradeQuantity);
				
				if (bid.getQuantity() < 1) {
					bid.acceptOffer();
					
					
					historicalRecords.add(bids.remove(0));
				}
				
				if (ask.getQuantity() < 1) {
					ask.acceptOffer();
					historicalRecords.add(asks.remove(0));
				}
				
			}
			
			
		}
		//negatively change price beliefs for remaining offers
		
		turnNumber++;
	}
	
	public ArrayList<Offer> getBidBook() {
		return bidBook;
	}
	public ArrayList<Offer> getAskBook() {
		return askBook;
	}
	

}
