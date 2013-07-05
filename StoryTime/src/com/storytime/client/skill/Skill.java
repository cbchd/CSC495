package com.storytime.client.skill;

import java.io.Serializable;

public abstract class Skill implements Serializable {

	private static final long serialVersionUID = 1L;
	String name = "";
	String message = "";

	abstract public String getType();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int cost = 0;

}
