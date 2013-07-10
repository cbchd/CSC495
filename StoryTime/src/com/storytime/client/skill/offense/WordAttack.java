package com.storytime.client.skill.offense;

import com.storytime.client.skillrelated.Skill;

public abstract class WordAttack extends Skill {

	private static final long serialVersionUID = 1L;
	abstract public String getType();
	private String phraseToBeAttacked = "";
	
	public String getPhraseToBeAttacked() {
		return phraseToBeAttacked;
	}
	public void setPhraseToBeAttacked(String phraseToBeAttacked) {
		this.phraseToBeAttacked = phraseToBeAttacked;
	}
	
}
