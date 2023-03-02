package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Resort;
import com.example.model.Skier;
import com.example.repo.ResortRepo;
import com.example.repo.SkierRepo;

@Service
public class SkierServiceImp {
	
	@Autowired
	ResortRepo resortRepo;
	
	@Autowired
	SkierRepo repo;
	
	public Object addSkierInLift(Integer resortID, String seasonID, Integer dayID, Integer skierID, Integer time, Integer liftID) {
		
		List<Resort> resort = resortRepo.getResortByID(resortID);
		try {
			if(resort != null && resort.size() > 0) {				
				System.out.println(resort.get(0).getResortID());
				if(resort.get(0).getSeasonYears().contains(seasonID)) {
					System.out.println(resort.get(0).getSeasonYears().contains(seasonID));
					Skier skier = new Skier(skierID, time, liftID, resortID, seasonID, dayID);
					repo.insert(skier);
					return "Successful";					
				} else return "Unsuccessful";
				
			} else return "Unsuccessful";
			
		}catch(Exception e) {
			System.out.println(e);
			return "Unsuccessful";
			
		}
	}

}
