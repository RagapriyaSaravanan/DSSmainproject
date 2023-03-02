package com.example.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Skier;


@Repository
public interface SkierRepo extends MongoRepository<Skier, Integer>{

	
}
