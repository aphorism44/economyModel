package test;

import economyModel.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Assert;

public class ClearinghouseTest {

	@Test
	public void createMarket() {

		Market m = new Market(1);
		
		assertEquals(m.getAgentCount() , 5);
		
		m.agentsProduce();
		
		System.out.println("after production agents");
		for (Agent a: m.getAgents())
			System.out.println(a.getInventoryString());
		
		m.agentsCreateOffers();
		
		//unit testing of the offers
		/*
		System.out.println("bid book");
		for (Offer bid: m.getBidBook())
			System.out.println(bid.toString());
		System.out.println("ask book");
		for (Offer ask: m.getAskBook())
			System.out.println(ask.toString());
		*/
		/*
		FORMAT
		offerType: bid, commodity: ore, quantity: 11
			, price: 27.0, agentType: refiner
			, agentId: 3920b763-9947-46f7-847e-e1621734f84c

		offerType: ask, commodity: tools, quantity: 7
			, price: 48.7, agentType: blacksmith
			, agentId: 331b4135-7bb2-4542-9a08-2ef4c9c03755
		*/
		
		
		for (Offer bid: m.getBidBook()) {
			String commodity = bid.getCommodityType();
			String agentType = bid.getCreatorAgentType();
			int quantity = bid.getQuantity();
			assert(quantity > 0);
			switch(commodity) {
			case "wood":
				assertEquals(agentType, "farmer");
				break;
			case "food":
				assert(!agentType.equals("farmer"));
				break;
			case "ore":
				assertEquals(agentType, "refiner");
				break;
			case "metal":
				assertEquals(agentType, "blacksmith");
				break;
			case "tools":
				assertNotEquals(agentType, "blacksmith");
				assert(quantity == 1);
				break;
			default:
				fail();
				break;
				
			}
		}
		
		for (Offer ask: m.getAskBook()) {
			String commodity = ask.getCommodityType();
			String agentType = ask.getCreatorAgentType();
			int quantity = ask.getQuantity();
			assert(quantity > 0);
			switch(commodity) {
			case "wood":
				assertEquals(agentType, "lumberjack");
				break;
			case "food":
				assertEquals(agentType, "farmer");
				break;
			case "ore":
				assertEquals(agentType, "miner");
				break;
			case "metal":
				assertEquals(agentType, "refiner");
				break;
			case "tools":
				assertEquals(agentType, "blacksmith");
				break;
			default:
				fail();
				break;
			}
		}
		
		
		assertEquals(m.getHistoricalRecords().size(), 0);
		
		//unit testing of offer resolution
		//System.out.println("initial bids:" + m.getBidBook().size());
		//System.out.println("initial asks:" + m.getAskBook().size());
		int allOffers = m.getBidBook().size() + m.getAskBook().size();
		//System.out.println("allOffers " + allOffers);
		m.resolveOffers();
		
		assert(allOffers == (m.getHistoricalRecords().size() +  m.getBidBook().size() + m.getAskBook().size()));
		
		/*
		System.out.println("completed offer resolution");
		System.out.println("filled " + m.getHistoricalRecords().size());
		System.out.println("unfilled bids " + m.getBidBook().size());
		System.out.println("unfilled asks " + m.getAskBook().size());
		for (Offer o: m.getHistoricalRecords())
			System.out.println(o.toString());
		
		System.out.println("final agents");
		for (Agent a: m.getAgents())
			System.out.println(a.getInventoryString());
		*/
		
	}
	
}