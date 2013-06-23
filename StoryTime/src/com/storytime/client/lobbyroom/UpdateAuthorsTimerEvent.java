package com.storytime.client.lobbyroom;

import de.novanic.eventservice.client.event.Event;

public class UpdateAuthorsTimerEvent implements Event {

    private static final long serialVersionUID = 1L;
    public int authorsTimer = 0;
    public String roomName = "";
    
	public int getAuthorsTimer() {
		return authorsTimer;
	}
	public void setAuthorsTimer(int submissionTimer) {
		this.authorsTimer = submissionTimer;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}
