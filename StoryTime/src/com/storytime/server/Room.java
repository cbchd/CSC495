package com.storytime.server;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    String roomName; 
    String theme;
    int pointCap = 5;
    int submissionTimer = 10;
    int chooserTimer = 15;
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
		return pointCap;
	}

	public void setPointCap(int pointCap) {
		this.pointCap = pointCap;
	}

	public int getTimer() {
		return submissionTimer;
	}

	public void setTimer(int timer) {
		this.submissionTimer = timer;
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
}
