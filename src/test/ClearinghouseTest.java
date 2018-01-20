package test;

import economyModel.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Assert;

public class ClearinghouseTest {

	private ArrayList<Agent> agents;
	
	@Test
	public void populateClearinghouse() {
		ArrayList<Agent> agents = new ArrayList<Agent>();
		Clearinghouse house = new Clearinghouse();
		
		for (int i = 0; i < 5; i++) {
			FarmerProduction fp = new FarmerProduction();
			Agent farmer = new Agent("farmer", fp);
			agents.add(farmer);
			
			BlacksmithProduction bp = new BlacksmithProduction();
			Agent blacksmith = new Agent("blacksmith", bp);
			agents.add(blacksmith);
			
			LumberjackProduction lp = new LumberjackProduction();
			Agent lumberjack = new Agent("lumberjack", lp);
			agents.add(lumberjack);
			
			RefinerProduction rp = new RefinerProduction();
			Agent refiner = new Agent("refiner", rp);
			agents.add(refiner);
			
			MinerProduction mp = new MinerProduction();
			Agent miner = new Agent("miner", mp);
			agents.add(miner);
		}
		
		assertEquals(agents.size(), 25);
		
		for (Agent a: agents)
			a.produce();
		
		house.createOffers(agents);
		
		/*
		for (Offer bid: house.getBidBook())
			System.out.println(bid.toString());
		
		for (Offer ask: house.getAskBook())
			System.out.println(ask.toString());
		*/
		
		for (Offer bid: house.getBidBook()) {
			String commodity = bid.getCommodityType();
			String agentType = bid.getCreatorAgentType();
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
			}
		}
		
		for (Offer ask: house.getAskBook()) {
			String commodity = ask.getCommodityType();
			String agentType = ask.getCreatorAgentType();
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
			}
		}
		
		
	}
	
}
//assertEquals
//assertThat