package com.storytime.client.skill;

import java.io.Serializable;
import java.util.ArrayList;

public class MultiSkill implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name = "";
	public String message = "";
	public ArrayList<Skill> skillList = new ArrayList<Skill>();
	
}
