package com.storytime.client.lobbyroom;

import de.novanic.eventservice.client.event.Event;

public class UpdatePointLimitEvent implements Event {

    private static final long serialVersionUID = 1L;
    public int pointLimit = 0;
    
    public int getPointLimit() {
		return pointLimit;
	}
	public void setPointLimit(int pointLimit) {
		this.pointLimit = pointLimit;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String roomName = "";

}
