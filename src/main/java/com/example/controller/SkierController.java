package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.SkierServiceImp;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/skiers")
public class SkierController {
	@Autowired
	SkierServiceImp service;
	
	@PostMapping(path = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", produces = "application/json")
	public Object addSkier(@PathVariable("resortID") Integer resortID, @PathVariable("seasonID") String seasonID, 
			@PathVariable("dayID") Integer dayID, @PathVariable("skierID") Integer skierID,
			@RequestBody Map<String, Integer> requestBody) {
		HttpStatus statusCode = null;
		Integer time = requestBody.get("time");
		Integer liftID = requestBody.get("liftID");
		if(time == null || liftID == null) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else {
			String message = (String)service.addSkierInLift(resortID, seasonID, dayID, skierID, time, liftID);
			if(message.equalsIgnoreCase("successful")) {
    			statusCode = HttpStatus.OK;
    		} else {
    			statusCode = HttpStatus.NOT_FOUND;
    		}
			
		}		
		return statusCode;
	}

}
