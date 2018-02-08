package test;

import java.util.ArrayList;

import org.junit.Test;

import economyModel.Agent;
import economyModel.Market;

public class LoopTest {
	
	@Test
	public void createMarket() {

		Market m = new Market();
		
		m.addAgent("farmer", 7);
		m.addAgent("blacksmith", 1);
		m.addAgent("refiner", 1);
		m.addAgent("miner", 2);
		m.addAgent("lumberjack", 2);
		
		
		System.out.println(m.toString());
		m.iterateTurn(1);
		System.out.println(m.toString());
		m.iterateTurn(1);
		System.out.println(m.toString());
		m.iterateTurn(1);
		System.out.println(m.toString());
		m.iterateTurn(1);
		System.out.println(m.toString());
		m.iterateTurn(3);
		System.out.println(m.toString());
		m.iterateTurn(3);
		System.out.println(m.toString());
		m.iterateTurn(3);
		System.out.println(m.toString());
		m.iterateTurn(3);
		System.out.println(m.toString());
		
		ArrayList<Agent> agents = m.getAgents();
		
		for (Agent a: agents)
			System.out.println(a.toString());
		
		
	}
	
}
