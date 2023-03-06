package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
//import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;

@Document(collection = "resorts")
public class Resort implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "_id")
	ObjectId id;
	
//	@Indexed(unique = true)
	@Column(name = "resortID")
	Integer resortID;
	
	@Column(name = "resortName")
	String resortName;
	
	@Column(name = "seasonYears")
	List<String> seasonYears;
	
	public Resort(Integer resortID, String resortName) {
		super();
		this.resortID = resortID;
		this.resortName = resortName;
		List<String> i = new ArrayList<String>();
		i.add("");
		this.seasonYears = i;
	}
	
	public Resort(Integer resortID, String resortName, ArrayList<String> seasonYears) {
		super();
		this.resortID = resortID;
		this.resortName = resortName;
		this.seasonYears = seasonYears;
	}

	
	public Resort() {
		// TODO Auto-generated constructor stub
	}

	public int getResortID() {
		return resortID;
	}

	public void setResortID(Integer resortID) {
		this.resortID = resortID;
	}

	public String getResortName() {
		return resortName;
	}

	public void setResortName(String resortName) {
		this.resortName = resortName;
	}

	public List<String> getSeasonYears() {
		return seasonYears;
	}

	public void addSeasonYears(String year) {
		this.seasonYears.add(year);
	}

	public void setSeasonYears(List<String> seasonYears) {
		this.seasonYears = seasonYears;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	
	
}
