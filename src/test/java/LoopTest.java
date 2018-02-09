package test.java;

import java.util.ArrayList;

import org.junit.Test;

import main.java.Agent;
import main.java.Market;
import main.java.SaleRecord;

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
		
		m.iterateTurn(100);
		System.out.println(m.toString());
		
		ArrayList<Agent> agents = m.getAgents();
		
		
		System.out.println("\nThe agent statuses:");
		for (Agent a: agents)
			System.out.println(a.toString());
		
		System.out.println("\nThe sales made:");
		for (SaleRecord sr: m.getHistoricalRecords())
			System.out.println(sr.toString());
		
		
	}
	
}
