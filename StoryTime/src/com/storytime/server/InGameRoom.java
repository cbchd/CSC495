package com.storytime.server;

import java.util.ArrayList;
import java.util.HashMap;

import de.novanic.eventservice.client.event.domain.Domain;

public class InGameRoom {
    private static final long serialVersionUID = 1L;
    public String theme;
    public int pointCap = 0;
    public int authorTimer = 0;
    public int mastersTimer = 0;
    public boolean inGame;
    public ArrayList<User> users;
    public String message;
    public Domain domain;
    public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getPointCap() {
		return pointCap;
	}

	public void setPointCap(int pointCap) {
		this.pointCap = pointCap;
	}

	public int getAuthorTimer() {
		return authorTimer;
	}

	public void setAuthorTimer(int authorTimer) {
		this.authorTimer = authorTimer;
	}

	public int getMastersTimer() {
		return mastersTimer;
	}

	public void setMastersTimer(int mastersTimer) {
		this.mastersTimer = mastersTimer;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public HashMap<User, Integer> getScoreList() {
		return scoreList;
	}

	public void setScoreList(HashMap<User, Integer> scoreList) {
		this.scoreList = scoreList;
	}

	public int getTurnCounter() {
		return turnCounter;
	}

	public void setTurnCounter(int turnCounter) {
		this.turnCounter = turnCounter;
	}

	public int getPhrasesSubmitted() {
		return phrasesSubmitted;
	}

	public void setPhrasesSubmitted(int phrasesSubmitted) {
		this.phrasesSubmitted = phrasesSubmitted;
	}

	public ArrayList<String> getStory() {
		return story;
	}

	public void setStory(ArrayList<String> story) {
		this.story = story;
	}

	public boolean isGameEnded() {
		return gameEnded;
	}

	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}

	public User getChooser() {
		return chooser;
	}

	public void setChooser(User chooser) {
		this.chooser = chooser;
	}

	public boolean isChoosersTimerElapsed() {
		return choosersTimerElapsed;
	}

	public void setChoosersTimerElapsed(boolean choosersTimerElapsed) {
		this.choosersTimerElapsed = choosersTimerElapsed;
	}

	public int getNumberOfUsersWhoseTimersHaveElapsed() {
		return numberOfUsersWhoseTimersHaveElapsed;
	}

	public void setNumberOfUsersWhoseTimersHaveElapsed(int numberOfUsersWhoseTimersHaveElapsed) {
		this.numberOfUsersWhoseTimersHaveElapsed = numberOfUsersWhoseTimersHaveElapsed;
	}

	public ArrayList<String> messages;
    public HashMap<User, Integer> scoreList;
    public int turnCounter = 0;
    public int phrasesSubmitted = 0;
    public ArrayList<String> story;
    public boolean gameEnded = false;
    public User chooser;
    boolean choosersTimerElapsed = false;
    public int numberOfUsersWhoseTimersHaveElapsed = 0;
	
    public InGameRoom() {
	users = new ArrayList<User>();
	messages = new ArrayList<String>();
	scoreList = new HashMap<User, Integer>();
	story = new ArrayList<String>();
    }

}
