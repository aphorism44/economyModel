package test;

import economyModel.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Assert;

public class ClearinghouseTest {

	private ArrayList<Agent> agents;
	
	@Test
	public void createMarket() {

		Market m = new Market(5);
		
		assertEquals(m.getAgentCount() , 25);
		
		m.agentsProduce();
		
		
		m.agentsCreateOffers();
		
		
		
		for (Offer bid: m.getBidBook())
			System.out.println(bid.toString());
		
		for (Offer ask: m.getAskBook())
			System.out.println(ask.toString());
		/*
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
		
		
		
	}
	
}