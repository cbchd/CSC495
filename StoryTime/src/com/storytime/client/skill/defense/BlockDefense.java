package com.storytime.client.skill.defense;

public class BlockDefense extends WordDefense {

	public String submissionToProtect = "";
	private String type = "BLOCK_DEFENSE";

	@Override
	public String getType() {
		return type;
	}
}
