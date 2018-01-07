package economyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Clearinghouse {

	private ArrayList<Offer> historicalRecords;
	private ArrayList<Offer> bidBook;
	private ArrayList<Offer> askBook;
	private int turnNumber;
	
	
	public Clearinghouse() {
		this.historicalRecords = new ArrayList<Offer>();
		this.bidBook = new ArrayList<Offer>();
		this.askBook = new ArrayList<Offer>();
		this.turnNumber = 1;
	}
	/*
	public void createBid(Agent a, int type, String comm, int limit) {
		double bid = this.priceOf(comm);
		int idealAmount = this.determineQuantity(a, comm);
		int quant = Math.min(idealAmount, limit);
		
		
		
		Offer newBid = new Offer(a.getUuid(), type, comm, quant, bid);
		switch(type) {
			case 0:
				this.bidBook.add(newBid);
				break;
			case 1:
				this.askBook.add(newBid);
				break;
		}
	}
	
	public int determineQuantity(Agent a, String comm) {
		double mean = this.getHistoricalPrice(comm);
		
		
		return 0;
	}
	
	public double getHistoricalPriceMean(String c) {
		//let's hear it for lambda
		ArrayList<Offer> commodityOffers = this.historicalRecords.stream().filter(o->o.getCommodityType().equals(comm).toArray(Offer[]::new));
		return commodityOffers.stream().mapToDouble(o->o.getPricePerUnit()).average();
	}
	*/
	
	public void resolveOffers(String commodity, LinkedHashMap<UUID, Agent> agents ) {
		//grab all bids for inputed commodity;
		ArrayList<Offer> bids= new ArrayList<Offer>();
		ArrayList<Offer> asks = new ArrayList<Offer>();
		
		for (Offer o: this.bidBook) {
			if (o.isOfCommodity(commodity))
				bids.add(o);
		}
		for (Offer o: this.askBook) {
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
				
				if (bid.getQuantity() < 1)
					bids.remove(0);
				if (ask.getQuantity() < 1)
					asks.remove(0);
				
				//TODO - update the historical record; update Agent price belief (?)
				
			}
			
			
		}
		
		
	}
	

}
