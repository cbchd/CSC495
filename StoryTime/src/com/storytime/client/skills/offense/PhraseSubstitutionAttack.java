package com.storytime.client.skills.offense;

public class PhraseSubstitutionAttack extends WordAttack {

	private String submissionToLookAt = "";
	private String phraseToLookFor = "";
	private String phraseToSwapFor = "";

	public String getSubmissionToLookAt() {
		return submissionToLookAt;
	}

	public void setSubmissionToLookAt(String submissionToLookAt) {
		this.submissionToLookAt = submissionToLookAt;
	}

	public String getPhraseToLookFor() {
		return phraseToLookFor;
	}

	public void setPhraseToLookFor(String phraseToLookFor) {
		this.phraseToLookFor = phraseToLookFor;
	}

	public String getPhraseToSwapFor() {
		return phraseToSwapFor;
	}

	public void setPhraseToSwapFor(String phraseToSwapFor) {
		this.phraseToSwapFor = phraseToSwapFor;
	}
}
