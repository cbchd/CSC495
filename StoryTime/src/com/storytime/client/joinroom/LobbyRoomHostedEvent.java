package com.storytime.client.joinroom;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

public class LobbyRoomHostedEvent implements Event {

    private static final long serialVersionUID = 1L;
    public static Domain domain = DomainFactory.getDomain("Lobby"); 
    public String roomName = "";
    public String theme = "";
    public String password = "";
    public int authorsTime = 0;
    public int mastersTime = 0;
    public int numberOfPlayers = 0;
    public int pointLimit = 0;
    
    public LobbyRoomHostedEvent() {
    }

	public static Domain getDomain() {
		return domain;
	}

	public static void setDomain(Domain domain) {
		LobbyRoomHostedEvent.domain = domain;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public int getPointLimit() {
		return pointLimit;
	}

	public void setPointLimit(int pointLimit) {
		this.pointLimit = pointLimit;
	}
}
