package com.storytime.client.skill;


import java.io.Serializable;
import java.util.HashMap;


public class MultiSkill implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name = "";
	public String message = "";
	public int totalNumberOfSkills = 10;
	public HashMap<Integer, Skill> skillList = new HashMap<Integer, Skill>();
	
}
