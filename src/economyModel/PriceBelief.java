package economyModel;

public class PriceBelief {
	
	private String commodity;
	private double lowerPriceBound;
	private double upperPriceBound;
	
	
	public PriceBelief(String c) {
		this.commodity = c;
		this.lowerPriceBound = 0;
		this.upperPriceBound = 0;
		
	}
	
	//returns percentage for "favorability" measure
	public double getRangePenetration(double mean) {
		return (mean - this.lowerPriceBound) / (this.upperPriceBound - this.lowerPriceBound);
	}
	
	public String getCommodity() {
		return this.commodity;
	}
	
	
}
