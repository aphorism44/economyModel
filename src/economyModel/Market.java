package economyModel;

import java.util.ArrayList;

public class Market {
	
	Clearinghouse house;
	ArrayList<Agent> agents;
	
	public Market() {
		house = new Clearinghouse();
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
		
	}

}