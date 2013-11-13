package com.storytime.server;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	String username = "";
	String password = "";
	String location = "";
	
	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	boolean isReadyToStart = false;
	int score = 0;
	int place = 0;
	Room room;
	InGameRoom ingameRoom;
	ArrayList<String> phraseHistory = new ArrayList<String>();

	boolean timerElapsed = false;

	StoryTimeEngine storyTimeEngine;

	public User(String username, String password, StoryTimeEngine storyTimeEngine) {
		this.username = username;
		this.password = password;
		this.storyTimeEngine = storyTimeEngine;
		room = null;
	}

	public ArrayList<String> getPhrasesPerRound() {
		return phraseHistory;
	}

	public void setPhrasesPerRound(ArrayList<String> phrasesPerRound) {
		this.phraseHistory = phrasesPerRound;
	}

	public boolean isReadyToStart() {
		return isReadyToStart;
	}

	public void setReadyToStart(boolean isReadyToStart) {
		this.isReadyToStart = isReadyToStart;
	}

	public InGameRoom getIngameRoom() {
		return ingameRoom;
	}

	public void setIngameRoom(InGameRoom ingameRoom) {
		this.ingameRoom = ingameRoom;
	}

	public boolean isTimerElapsed() {
		return timerElapsed;
	}

	public void setTimerElapsed(boolean timerElapsed) {
		this.timerElapsed = timerElapsed;
	}

	public StoryTimeEngine getStoryTimeEngine() {
		return storyTimeEngine;
	}

	public void setStoryTimeEngine(StoryTimeEngine storyTimeEngine) {
		this.storyTimeEngine = storyTimeEngine;
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
