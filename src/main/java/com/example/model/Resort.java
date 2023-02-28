package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Resort implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int resortID;
	String resortName;
	ArrayList<String> seasonYears;
	
	public Resort(int resortID, String resortName, ArrayList<String> seasonYears) {
		super();
		this.resortID = resortID;
		this.resortName = resortName;
		this.seasonYears = seasonYears;
	}

	public int getResortID() {
		return resortID;
	}

	public void setResortID(int resortID) {
		this.resortID = resortID;
	}

	public String getResortName() {
		return resortName;
	}

	public void setResortName(String resortName) {
		this.resortName = resortName;
	}

	public ArrayList<String> getSeasonYears() {
		return seasonYears;
	}

	public void setSeasonYears(ArrayList<String> seasonYears) {
		this.seasonYears = seasonYears;
	}

	
	
}
