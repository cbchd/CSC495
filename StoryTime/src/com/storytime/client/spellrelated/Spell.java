package com.storytime.client.spellrelated;

public class Spell {

	String name = "";
	String magicWords = "";
	int cost;
	String type = "";
	SpellBook spellBook;

	public Spell(String name, String magicWords, int cost) {
		this.name = name;
		this.magicWords = magicWords;
		this.cost = cost;
		setType();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMagicWords() {
		return magicWords;
	}

	public void setMagicWords(String magicWords) {
		this.magicWords = magicWords;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public SpellBook getSpellBook() {
		return spellBook;
	}

	public void setSpellBook(SpellBook spellBook) {
		this.spellBook = spellBook;
	}

	public void setType() {
		if (this instanceof ColorChangeSpell) {
			type = "ColorChangeSpell";
		} else if (this instanceof FontChangeSpell) {
			type = "FontChangeSpell";
		} else if (this instanceof PhraseAdditionSpell) {
			type = "PhraseAdditionSpell";
		} else if (this instanceof PhraseRemovalSpell) {
			type = "PhraseRemovalSpell";
		} else if (this instanceof PhraseSubstitutionSpell) {
			type = "PhraseSubstitutionSpell";
		} else if (this instanceof SubmissionCleansingSpell) {
			type = "SubmissionCleansingSpell";
		} else if (this instanceof SubmissionProtectionSpell) {
			type = "SubmissionProtectionSpell";
		}
	}

	public String getType() {
		return type;
	}
}
