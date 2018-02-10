package main.java.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class MarketErrorController implements ErrorController {

    private static final String PATH = "/error";

    private boolean debug = false;

    @Autowired
    private ErrorAttributes errorAttributes;
    
    @RequestMapping(value = PATH)
    LinkedHashMap error(HttpServletRequest request, HttpServletResponse response) {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring. 
        // Here we just define response body.
        return convertToLinkedHashMap(Integer.toString(response.getStatus()), getErrorAttributes(request, debug).toString());
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
    
    public LinkedHashMap convertToLinkedHashMap(String a, String b) {
		LinkedHashMap<String, String> response = new LinkedHashMap<String, String>();
		response.put(a, b);
		return response;
	}

}
