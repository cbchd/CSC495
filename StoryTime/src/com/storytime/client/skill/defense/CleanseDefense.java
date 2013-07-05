package com.storytime.client.skill.defense;

public class CleanseDefense extends WordDefense {
	
	private String submissionToCleanse = "";
	private String type = "CLEANSE_DEFENSE";

	public String getSubmissionToCleanse() {
		return submissionToCleanse;
	}

	public void setSubmissionToCleanse(String submissionToCleanse) {
		this.submissionToCleanse = submissionToCleanse;
	}

	@Override
	public String getType() {
		return type;
	}
}
