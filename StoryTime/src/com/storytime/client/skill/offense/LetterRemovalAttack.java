package com.storytime.client.skill.offense;

public class LetterRemovalAttack extends WordAttack {
	
	private String submissionToLookAt = "";
	private String phraseToRemove = "";
	private String type = "LETTER_REMOVAL_ATTACK";

	public String getSubmissionToLookAt() {
		return submissionToLookAt;
	}

	public void setSubmissionToLookAt(String submissionToLookAt) {
		this.submissionToLookAt = submissionToLookAt;
	}

	public String getPhraseToRemove() {
		return phraseToRemove;
	}

	public void setPhraseToRemove(String phraseToRemove) {
		this.phraseToRemove = phraseToRemove;
	}

	@Override
	public String getType() {
		return type;
	}
	
}
