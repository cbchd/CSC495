package com.storytime.client.skills.offense;

public class PhraseRemovalAttack extends WordAttack {
	
	private String submissionToLookAt = "";
	private String phraseToRemove = "";

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
	
}
