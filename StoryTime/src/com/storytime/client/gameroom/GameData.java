package com.storytime.client.gameroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import de.novanic.eventservice.client.event.domain.Domain;

public class GameData implements Serializable {
	private static final long serialVersionUID = 1L;
	public String theme;
	public int pointCap = 0;
	public int submissionTimer = 0;
	public int mastersTimer = 0;
	public boolean inGame;
	public ArrayList<String> users;
	public String message;
	public Domain domain;
	public ArrayList<String> messages;
	public HashMap<String, Integer> scoreList;
	public String thisUser = "";
	public String currentChooser = "";
	int phrasesSubmitted = 0;
	public String story = "";
	public String winner = "";

	public GameData() {
		users = new ArrayList<String>();
		messages = new ArrayList<String>();
		scoreList = new HashMap<String, Integer>();
	}
}