package com.storytime.client.lobbyroom;

import de.novanic.eventservice.client.event.Event;

public class UpdateMastersTimerEvent implements Event {

	private static final long serialVersionUID = -5778049048712855028L;
	public int mastersTime = 0;
	public String roomName = "";
	
	public int getMastersTime() {
		return mastersTime;
	}
	public void setMastersTime(int chooseTime) {
		this.mastersTime = chooseTime;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}
