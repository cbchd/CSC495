package com.storytime.client.lobbyroom;

import java.io.Serializable;
import java.util.ArrayList;

import de.novanic.eventservice.client.event.domain.Domain;

public class LobbyRoomData implements Serializable {

	private static final long serialVersionUID = 1L;
	public String roomName = "";
	public String theme = "";
	public int pointCap = 5;
	public int authorsTimer = 10;
	
	public int getAuthorsTimer() {
		return authorsTimer;
	}


	public void setAuthorsTimer(int authorsTimer) {
		this.authorsTimer = authorsTimer;
	}


	public int getMastersTimer() {
		return mastersTimer;
	}


	public void setMastersTimer(int mastersTimer) {
		this.mastersTimer = mastersTimer;
	}


	public int mastersTimer = 15;
	private String password = "";
	
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
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

	public boolean isInGame() {
		return inGame;
	}


	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}


	public ArrayList<String> getUsers() {
		return users;
	}


	public void setUsers(ArrayList<String> users) {
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


	public String getHostsName() {
		return hostsName;
	}


	public void setHostsName(String hostsName) {
		this.hostsName = hostsName;
	}


	public boolean inGame;
	public ArrayList<String> users = new ArrayList<String>();
	public String message = "";
	public Domain domain;
	public ArrayList<String> messages = new ArrayList<String>();
	public String hostsName = "";
	

	public LobbyRoomData() {
	}
}
