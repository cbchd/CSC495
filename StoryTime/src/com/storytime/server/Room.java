package com.storytime.server;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    String roomName; 
    String theme;
    String password = "";
    int pointLimit = 5;
    int authorsTime = 10;
    int mastersTime = 15;
    int numberOfPlayers = 0;
    User host;
    boolean inGame;
    HashMap<String, User> users;
    ArrayList<String> roomChat;
    
    public Room(User host, String roomName) {
        this.roomName = roomName;
        this.host = host;
        this.inGame = false;
        this.users = new HashMap<String, User>();
        this.roomChat = new ArrayList<String>();
        this.users.put(host.username, host);
    }

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getPointCap() {
		return pointLimit;
	}

	public void setPointCap(int pointCap) {
		this.pointLimit = pointCap;
	}

	public int getTimer() {
		return authorsTime;
	}

	public void setTimer(int timer) {
		this.authorsTime = timer;
	}

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}

	public ArrayList<String> getRoomChat() {
		return roomChat;
	}

	public void setRoomChat(ArrayList<String> roomChat) {
		this.roomChat = roomChat;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPointLimit() {
		return pointLimit;
	}

	public void setPointLimit(int pointLimit) {
		this.pointLimit = pointLimit;
	}

	public int getAuthorsTime() {
		return authorsTime;
	}

	public void setAuthorsTime(int authorsTime) {
		this.authorsTime = authorsTime;
	}

	public int getMastersTime() {
		return mastersTime;
	}

	public void setMastersTime(int mastersTime) {
		this.mastersTime = mastersTime;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
}
