package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.model.Resort;
import com.example.repo.ResortRepo;

@Service
public class ResortServiceImp implements ResortService{
	
	@Autowired
	private ResortRepo repo;
	
	@Autowired
	 MongoOperations mongoOperations;

	@Override
	public Object addResort(int resortID, String resortName) {
		// TODO Auto-generated method stub
		Resort resort = new Resort(resortID, resortName);
		repo.insert(resort);
		return resort;
	}
	
	@Override
	public Object addSeason(int resortID, String year) {
		// TODO Auto-generated method stub
//		ObjectId objectId = ObjectId.valueOf(resortID);
		List<Resort> resort = repo.getResortByID(resortID);
		System.out.println(resort.size() + "---");
		
		try {
		if(resort != null && resort.size() > 0) {
			resort.get(0).addSeasonYears(year);
			System.out.println(resort.get(0).getResortName());
			repo.save(resort.get(0));
			
			return "Successful";
			}else return "Unsuccessful";
		} catch(Exception e) {
			System.out.println(e);
			return "Unsuccessful";
		}
	}	

}
