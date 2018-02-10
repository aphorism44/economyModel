package main.java.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.OptionalDouble;

public class Clearinghouse {
	
	private static final int defaultAveragePrice = 25;
	private static final int defaultMaxPrice = 50;
	private static final int defaultMinPrice = 50;

	private ArrayList<SaleRecord> historicalRecords;
	private ArrayList<Offer> bidBook;
	private ArrayList<Offer> askBook;
	private int turnNumber;
	
	
	public Clearinghouse() {
		historicalRecords = new ArrayList<SaleRecord>();
		bidBook = new ArrayList<Offer>();
		askBook = new ArrayList<Offer>();
		turnNumber = 1;
	}
	
	//looks at agents to find ones who should make an offer
	public void createOffers(ArrayList<Agent> agents) {
		//clear the bid/ask books
		bidBook.clear();
		askBook.clear();
				
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
		int bid = a.getPriceBelief(comm);
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
	
	public int getHistoricalPriceMean(String c) {
		
		if (historicalRecords.size() > 0) {
			//let's hear it for lambda!
			ArrayList<SaleRecord> commodityOffers = (ArrayList<SaleRecord>)historicalRecords.stream().filter(o -> o.getCommodityType().equals(c)).collect(Collectors.toList());
			int q = 0, total = 0;
			for (SaleRecord sr: commodityOffers) {
				q += sr.getQuantity();
				total += sr.getOffer();
			}
			
			
			return q > 0 ? Math.round(total / q) : defaultAveragePrice;
		}
		//returns below when there's no returned bids yet
		return defaultAveragePrice;
	}
	
	public int getHistoricalMaxPrice(String c) {
		if (historicalRecords.size() > 0) {
			ArrayList<SaleRecord> commodityOffers = (ArrayList<SaleRecord>) historicalRecords.stream().filter(o->o.getCommodityType().equals(c));
			int max = 0;
			for (SaleRecord sr: commodityOffers)
				max = Math.max(max, sr.getOffer());
			
			return max > 0 ? max : defaultMaxPrice;
		}
		//returns below when there's no returned bids yet
		return defaultMaxPrice;
	}
	
	public void offerRound(LinkedHashMap<UUID, Agent> agents, ArrayList<String> commodities) {
		for (String c: commodities)
			resolveOffers(c, agents);
		turnNumber++;
	}
	
	public void resolveOffers(String commodity, LinkedHashMap<UUID, Agent> agents ) {
		
		//grab all bids for inputed commodity;
		ArrayList<Offer> bids= new ArrayList<Offer>();
		ArrayList<Offer> asks = new ArrayList<Offer>();
		
		//note that the offers in books and here are seperate
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
			int clearingPrice = (bid.getOffer() + ask.getOffer()) / 2;
			if (clearingPrice < 1)
				clearingPrice = 1; //never 0 or less
			
			if (tradeQuantity > 0) {
				Agent buyer = agents.get(bid.getAgentId());
				Agent seller = agents.get(ask.getAgentId());
				int cost = tradeQuantity * clearingPrice;
				seller.updateInventory(commodity, -tradeQuantity);
				seller.updateMoney(cost);
				buyer.updateInventory(commodity, tradeQuantity);
				buyer.updateMoney(-cost);
				bid.updateQuantity(-tradeQuantity);
				ask.updateQuantity(-tradeQuantity);
				
				//make sure to positively update price belief on sales
				if (bid.getQuantity() < 1) {
					bid.acceptOffer();
					buyer.updatePriceBelief(bid, 0);
					removeFromBidBook(bid.getId());
					Offer finishedBid = bids.remove(0);
					historicalRecords.add(new SaleRecord(finishedBid.getOfferType(), finishedBid.getCommodityType(), tradeQuantity, finishedBid.getOffer(), finishedBid.getTurnNumber(), seller.getAgentType(), buyer.getAgentType()   ));
				} else {
					bid.acceptPartialOffer();
				}
				
				if (ask.getQuantity() < 1) {
					ask.acceptOffer();
					seller.updatePriceBelief(ask, 0);
					removeFromAskBook(ask.getId());
					Offer finishedAsk = asks.remove(0);
					historicalRecords.add(new SaleRecord(finishedAsk.getOfferType(), finishedAsk.getCommodityType(), tradeQuantity, finishedAsk.getOffer(), finishedAsk.getTurnNumber(), seller.getAgentType(), buyer.getAgentType() ));
				} else {
					ask.acceptPartialOffer();
				}
				
			}
			
			//the accepted offers are now in historicalRecords, tied to this turn number
			//rejected (partially or whole) are in bid and ask
			
			
		}
		//change price beliefs for rejected offers
		for (Offer bid: bids) {
			int mean = getHistoricalPriceMean(bid.getCommodityType());
			Agent ab = agents.get(bid.getAgentId());
			ab.updatePriceBelief(bid, mean);
		}
		for (Offer ask: asks) {
			int mean = getHistoricalPriceMean(ask.getCommodityType());
			Agent ab = agents.get(ask.getAgentId());
			ab.updatePriceBelief(ask, mean);
		}
		
	}
	
	public ArrayList<Offer> getBidBook() {
		return bidBook;
	}
	public ArrayList<Offer> getAskBook() {
		return askBook;
	}
	public ArrayList<SaleRecord> getHistoricalRecords() {
		return historicalRecords;
	}
	public void removeFromBidBook(UUID id) {
		
		Iterator<Offer> bi = bidBook.iterator();
		while (bi.hasNext()) {
		    UUID bId = bi.next().getId();
		    if (bId.equals(id)) {
		    	bi.remove();
		    	break;
		    }
		}
				
	}
	public void removeFromAskBook(UUID id) {
		Iterator<Offer> ai = askBook.iterator();
		while (ai.hasNext()) {
		    UUID aId = ai.next().getId();
		    if (aId.equals(id)) {
		    	ai.remove();
		    	break;
		    }
		}
	}
	
	public int getTurnNumber() {
		return turnNumber;
	}
	

}
