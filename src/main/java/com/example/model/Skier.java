package com.example.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skiers")
public class Skier implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Integer skierID;
	Integer time;
	Integer liftID;
	Integer resortID;
	String seasonYear;
	Integer dayID;

	public Skier(Integer skierID, Integer time, Integer liftID, Integer resortID, String seasonYear, Integer dayID) {
		super();
		this.skierID = skierID;
		this.time = time;
		this.liftID = liftID;
		this.resortID = resortID;
		this.seasonYear = seasonYear;
		this.dayID = dayID;
	}

	public Skier(Skier skier) {
		// TODO Auto-generated constructor stub
		this.skierID = skier.getSkierID();
		this.time = skier.getTime();
		this.liftID = skier.getLiftID();
		this.resortID = skier.getResortID();
		this.seasonYear = skier.getSeasonYear();
		this.dayID = skier.getDayID();
		
	}

	public Integer getSkierID() {
		return skierID;
	}

	public void setSkierID(Integer skierID) {
		this.skierID = skierID;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getLiftID() {
		return liftID;
	}

	public void setLiftID(Integer liftID) {
		this.liftID = liftID;
	}

	public Integer getResortID() {
		return resortID;
	}

	public void setResortID(Integer resortID) {
		this.resortID = resortID;
	}

	public String getSeasonYear() {
		return seasonYear;
	}

	public void setSeasonYear(String seasonYear) {
		this.seasonYear = seasonYear;
	}

	public Integer getDayID() {
		return dayID;
	}

	public void setDayID(Integer dayID) {
		this.dayID = dayID;
	}	
	
}
