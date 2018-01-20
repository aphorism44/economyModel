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
		this.commodity = c;
		this.lowerPriceBound = this.initialLowePriceBound;
		this.upperPriceBound = this.initialHighPriceBound;
	}
	
	public PriceBelief(String c, double low, double high) {
		this.commodity = c;
		this.lowerPriceBound = low;
		this.upperPriceBound = high;
	}
	
	//returns percentage for "favorability" measure
	public double getRangePenetration(double mean) {
		//if there's no market data, make the mean a random number between bounds
		if (mean == 0)
			mean = this.getRandomBid();
		
		return (mean - this.lowerPriceBound) / (this.upperPriceBound - this.lowerPriceBound);
	}
	
	public String getCommodity() {
		return this.commodity;
	}
	
	//return random number between the bounds; decided on 0.1 "steps" between the prices; may adjust this
	public double getRandomBid() {
		Random r = new Random();
	    return (r.nextInt((int)((this.upperPriceBound - this.lowerPriceBound) * 10 + 1)) + this.lowerPriceBound * 10) / 10.0;
	}
	
	public double getLowerBound() {
		return this.lowerPriceBound;
	}
	
	public double getUpperBound() {
		return this.upperPriceBound;
	}
	
	//below is run with successful offer; contracts bounds around mean
	public void contractBounds() {
		double spread = this.upperPriceBound - this.lowerPriceBound;
		double adjustment = spread * this.boundAdjustment;
		this.lowerPriceBound += adjustment;
		this.upperPriceBound -= adjustment;
	}
	
	//below is run with unsuccessful offer; expends bounds around mean
	public void expandBounds(Boolean lowInventory) {
		double spread = this.upperPriceBound - this.lowerPriceBound;
		double adjustment = spread * this.boundAdjustment;
		if (lowInventory)
			adjustment *= 5;
		this.lowerPriceBound -= adjustment;
		this.upperPriceBound += adjustment;
	}
	
	
}
