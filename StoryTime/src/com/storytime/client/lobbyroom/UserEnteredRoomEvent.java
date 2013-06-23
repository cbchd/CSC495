package com.storytime.client.lobbyroom;

import de.novanic.eventservice.client.event.Event;

public class UserEnteredRoomEvent implements Event {

	private static final long serialVersionUID = -2028161836126246533L;
	public String username = "";
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
