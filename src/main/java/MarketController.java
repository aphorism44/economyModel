package main.java;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(value = "/test", produces = "application/json")
	public String getTest() {
		return "let's see if this works now";
	}
	
	@RequestMapping(value = "/testinput", produces = "application/json")
    public String getInputTest(@RequestParam(value="name", defaultValue="user") String name) {
        return "you sent in: " + name;
    }
	
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public String getStatus() {
		return market.toString();
	}
	
	@RequestMapping(value = "/add", produces = {"application/JSON"})
	public String addAgent(@RequestParam(value="agentType", defaultValue="farmer") String agentType) {
		if (market.addAgent(agentType, 1))
			return "added 1 agent of type " + agentType;
		else
			return "there is no agent template for type " + agentType;
	}
	
	@RequestMapping(value = "/iterate", produces = {"application/JSON"})
	public String iterateMarket(@RequestParam("turns") int n) {
		market.iterateTurn(n);
		return "iterated " + n + " turns";
	}
	
	

}
