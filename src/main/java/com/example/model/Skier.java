package com.example.model;

import java.io.Serializable;

class LiftRide {
	
	int time;
	int liftID;
	
	public LiftRide(int time, int liftID) {
		super();
		this.time = time;
		this.liftID = liftID;
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getLiftID() {
		return liftID;
	}
	public void setLiftID(int liftID) {
		this.liftID = liftID;
	}
	
}

public class Skier implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int skierID;
	LiftRide lift;

	public Skier(int skierID, LiftRide lift) {
		super();
		this.skierID = skierID;
		this.lift = lift;
	}

	public int getSkierID() {
		return skierID;
	}

	public void setSkierID(int skierID) {
		this.skierID = skierID;
	}

	public LiftRide getLift() {
		return lift;
	}

	public void setLift(LiftRide lift) {
		this.lift = lift;
	}

	
}
