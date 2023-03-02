package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.ResortServiceImp;

@RestController
@RequestMapping("/resorts")
public class ResortController {

	@Autowired
	private ResortServiceImp resortService;

	@PostMapping(path = "/new", produces = "application/json")
	public Object addResort(@RequestParam("resortID") Integer resortID, @RequestParam("resortName") String name) {

		return resortService.addResort(resortID, name);
	}

	@PostMapping(path = "/{resortID}/season", produces = "application/json")
	public Object addResortSeason(@PathVariable(value = "resortID") Integer resortID, @RequestBody Map<String, String> requestBody) {
		HttpStatus statusCode = null;
		String year = requestBody.get("year");
       System.out.println(year);
       System.out.println(resortID);
       
		if (year == null) {
        	statusCode = HttpStatus.BAD_REQUEST;            
        } else {
        	
        	String message = (String) resortService.addSeason(resortID,year);
    		if(message.equalsIgnoreCase("successful")) {
    			statusCode = HttpStatus.OK;
    		} else {
    			statusCode = HttpStatus.NOT_FOUND;
    		}
        		
        }
		return statusCode;	
		
	}	
}
