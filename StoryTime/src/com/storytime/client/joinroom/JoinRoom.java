package com.storytime.client.joinroom;

import java.io.Serializable;

public class JoinRoom implements Serializable {

	private static final long serialVersionUID = 1641648764300990204L;
    public String roomName = "";
    public String theme = "";
    public String password = "";
    public int authorsTime = 0;
    public int mastersTime = 0;
    public int numberOfPlayers = 0;
    public int pointLimit = 0;
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getPointLimit() {
		return pointLimit;
	}
	public void setPointLimit(int pointLimit) {
		this.pointLimit = pointLimit;
	}
	public int getMastersTime() {
		return mastersTime;
	}
	public void setMastersTime(int mastersTime) {
		this.mastersTime = mastersTime;
	}
	public int getAuthorsTime() {
		return authorsTime;
	}
	public void setAuthorsTime(int authorsTime) {
		this.authorsTime = authorsTime;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}

}
