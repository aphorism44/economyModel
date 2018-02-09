package main.java;

import java.util.Random;

public class PriceBelief {
	
	private static final int initialLowePriceBound = 1;
	private static final int initialHighPriceBound = 50;
	
	private String commodity;
	private int lowerPriceBound;
	private int upperPriceBound;
	
	private static final double boundAdjustment = 0.05;
	
	public PriceBelief(String c) {
		commodity = c;
		lowerPriceBound = initialLowePriceBound;
		upperPriceBound = initialHighPriceBound;
	}
	
	public PriceBelief(String c, int low, int high) {
		commodity = c;
		lowerPriceBound = low;
		upperPriceBound = high;
	}
	
	//returns percentage for "favorability" measure
	public double getRangePenetration(double mean) {
		//if there's no market data, make the mean a random number between bounds
		if (mean == 0)
			mean = getRandomBid();
		
		return (mean - lowerPriceBound) / (upperPriceBound - lowerPriceBound);
	}
	
	public String getCommodity() {
		return commodity;
	}
	
	//return random number between the bounds; money must be round numbers
	public int getRandomBid() {
		Random r = new Random();
		return r.nextInt((upperPriceBound - lowerPriceBound) + 1) + lowerPriceBound;
	}
	
	public int getLowerBound() {
		return lowerPriceBound;
	}
	
	public int getUpperBound() {
		return upperPriceBound;
	}
	
	//below is run with successful offer; contracts bounds around mean
	public void contractBounds() {
		double spread = upperPriceBound - lowerPriceBound;
		double adjustment = spread * boundAdjustment;
		lowerPriceBound += adjustment;
		upperPriceBound -= adjustment;
	}
	
	//below is run with unsuccessful offer; expends bounds around mean
	//while adjusting the mean as well
	public void expandBounds(Boolean lowInventory, int historicPriceMean) {
		int spread = upperPriceBound - lowerPriceBound;
		int adjustment = (int)Math.round(spread * boundAdjustment);
		//if a low inventory, shift the entire mean and drastically expand range
		if (lowInventory) {	
			adjustment *= 5;
			lowerPriceBound = historicPriceMean - spread / 2;
			upperPriceBound = historicPriceMean + spread / 2;
		} 

		lowerPriceBound -= adjustment;
		upperPriceBound += adjustment;
		//prevent negative price bounds
		lowerPriceBound = Math.max(lowerPriceBound, 1);
				
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Price belief for: " + commodity + ", ");
		sb.append("lower: " + lowerPriceBound + ", upper: " + upperPriceBound + "\n");
		return sb.toString();
	}
	
}
