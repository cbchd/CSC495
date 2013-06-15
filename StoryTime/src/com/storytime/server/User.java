package com.storytime.server;

import java.util.HashMap;

import com.storytime.spellrelated.SpellBook;


public class User {
	String username = "";
	String password = "";
	String location = "";
	String phrase = "";
	boolean isReadyToStart = false;
	int score = 0;
	Room room;
	InGameRoom ingameRoom;
	boolean timerElapsed = false;
	HashMap<String, SpellBook> spellBooks = new HashMap<String, SpellBook>();

	StoryTimeEngine storyTimeEngine;

	public User(String username, String password, StoryTimeEngine storyTimeEngine) {
		this.username = username;
		this.password = password;
		this.storyTimeEngine = storyTimeEngine;
		room = null;
	}

	public HashMap<String, SpellBook> getSpellBooks() {
		return spellBooks;
	}

	public void setSpellBooks(HashMap<String, SpellBook> spellBooks) {
		this.spellBooks = spellBooks;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void removeRoomReference() {
		room = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public StoryTimeEngine getStoryModeEngine() {
		return storyTimeEngine;
	}

	public void setStoryModeEngine(StoryTimeEngine storyTimeEngine) {
		this.storyTimeEngine = storyTimeEngine;
	}
}
