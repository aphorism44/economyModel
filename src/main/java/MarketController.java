package main.java;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {

	private Market market;
	
	@Autowired
	public MarketController(Market m) {
		market = m;
	}
	
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public LinkedHashMap<String, Integer> getStatus() {
		return market.getStatus();
	}
	
	
	@RequestMapping(value = "/prices", method = RequestMethod.GET, produces = "application/json")
	public LinkedHashMap getCommodityValue(@RequestParam(value="commodity", defaultValue="all") String commodity) {
		if (commodity.equals("all"))
			return market.getMarketPrices();
		else
			return convertToLinkedHashMap(commodity, Integer.toString(market.getMarketPrice(commodity)));
	}
	
	@RequestMapping(value = "/add", produces = {"application/JSON"})
	public LinkedHashMap addAgent(@RequestParam(value="agentType", defaultValue="farmer") String agentType) {
		if (market.addAgent(agentType, 1))
			return convertToLinkedHashMap("response", "added 1 agent of type " + agentType);
		else
			return convertToLinkedHashMap("response","there is no agent template for type " + agentType);
	}
	
	@RequestMapping(value = "/iterate", produces = {"application/JSON"})
	public LinkedHashMap iterateMarket(@RequestParam("turns") int n) {
		market.iterateTurn(n);
		return convertToLinkedHashMap("response", "iterated " + n + " turns");
	}
	
	public LinkedHashMap convertToLinkedHashMap(String a, String b) {
		LinkedHashMap<String, String> response = new LinkedHashMap<String, String>();
		response.put(a, b);
		return response;
	}
	
	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
