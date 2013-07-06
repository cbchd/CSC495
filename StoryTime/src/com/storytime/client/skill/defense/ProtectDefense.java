package com.storytime.client.skill.defense;

public class ProtectDefense extends WordDefense {

	public String submissionToProtect = "";
	private String type = "BLOCK_DEFENSE";

	public String getSubmissionToProtect() {
		return submissionToProtect;
	}

	public void setSubmissionToProtect(String submissionToProtect) {
		this.submissionToProtect = submissionToProtect;
	}

	@Override
	public String getType() {
		return type;
	}
}
