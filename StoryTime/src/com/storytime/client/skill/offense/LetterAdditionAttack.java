package com.storytime.client.skill.offense;

public class LetterAdditionAttack extends WordAttack {

	private static final long serialVersionUID = 1L;
	private String addThis = "";
	private String afterThis = "";
	private String type = "LETTER_ADDITION_ATTACK";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddThis() {
		return addThis;
	}

	public void setAddThis(String addThis) {
		this.addThis = addThis;
	}

	public String getAfterThis() {
		return afterThis;
	}

	public void setAfterThis(String afterThis) {
		this.afterThis = afterThis;
	}

}
