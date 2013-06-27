package com.storytime.client.skills.offense;

public class PhraseAdditionAttack extends WordAttack {

	private String submissionToLookAt = "";
	private String phraseToAddToWords = "";

	public String getSubmissionToLookAt() {
		return submissionToLookAt;
	}

	public void setSubmissionToLookAt(String submissionToLookAt) {
		this.submissionToLookAt = submissionToLookAt;
	}

	public String getPhraseToAddToWords() {
		return phraseToAddToWords;
	}

	public void setPhraseToAddToWords(String phraseToAddToWords) {
		this.phraseToAddToWords = phraseToAddToWords;
	}
}
