package com.storytime.client.skill.offense;

public class LetterSubstitutionAttack extends WordAttack {

	private static final long serialVersionUID = 1L;
	private String submissionToLookAt = "";
	private String phraseToLookFor = "";
	private String phraseToSwapFor = "";
	private String type = "LETTER_SUBSTITUTION_ATTACK";
	
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

	@Override
	public String getType() {
		return type;
	}
}
