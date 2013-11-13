package com.storytime.client.lobby;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

public class LobbyUserLeftEvent implements Event{

	private static final long serialVersionUID = 1L;
	private String usernameOfUserWhoLeft = "";
	 public static Domain domain = DomainFactory.getDomain("Lobby");
	
	public String getUsernameOfUserWhoLeft() {
		return usernameOfUserWhoLeft;
	}
	public void setUsernameOfUserWhoLeft(String usernameOfUserWhoLeft) {
		this.usernameOfUserWhoLeft = usernameOfUserWhoLeft;
	}

}
