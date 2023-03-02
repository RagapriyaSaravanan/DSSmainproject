package com.example.repo;

import java.util.List;

//import org.bson.types.ObjectId;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Resort;


@Repository
public interface ResortRepo extends MongoRepository<Resort, Integer>{

	
	 @Query("{resortID:?0}") 
	 List<Resort> getResortByID(Integer resortID); 
	
}
