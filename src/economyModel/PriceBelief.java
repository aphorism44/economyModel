package economyModel;

import java.util.Random;

public class PriceBelief {
	
	private static final double initialLowePriceBound = 1;
	private static final double initialHighPriceBound = 50;
	
	private String commodity;
	private double lowerPriceBound;
	private double upperPriceBound;
	
	private static final double boundAdjustment = 0.05;
	
	public PriceBelief(String c) {
		commodity = c;
		lowerPriceBound = initialLowePriceBound;
		upperPriceBound = initialHighPriceBound;
	}
	
	public PriceBelief(String c, double low, double high) {
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
	
	//return random number between the bounds; decided on 0.1 "steps" between the prices; may adjust this
	public double getRandomBid() {
		Random r = new Random();
	    return (r.nextInt((int)((upperPriceBound - lowerPriceBound) * 10 + 1)) + lowerPriceBound * 10) / 10.0;
	}
	
	public double getLowerBound() {
		return lowerPriceBound;
	}
	
	public double getUpperBound() {
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
	public void expandBounds(Boolean lowInventory) {
		double spread = upperPriceBound - lowerPriceBound;
		double adjustment = spread * boundAdjustment;
		if (lowInventory)
			adjustment *= 5;
		lowerPriceBound -= adjustment;
		upperPriceBound += adjustment;
	}
	
	
}
