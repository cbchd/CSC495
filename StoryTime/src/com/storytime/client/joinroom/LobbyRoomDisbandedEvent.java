package com.storytime.client.joinroom;

import de.novanic.eventservice.client.event.Event;

public class LobbyRoomDisbandedEvent implements Event {

	private static final long serialVersionUID = 7422811164212880823L;
	public String roomName = "";
	
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

}
