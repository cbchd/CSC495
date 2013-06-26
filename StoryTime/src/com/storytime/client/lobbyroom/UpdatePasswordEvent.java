package com.storytime.client.lobbyroom;

import de.novanic.eventservice.client.event.Event;

public class UpdatePasswordEvent implements Event {

	private static final long serialVersionUID = -8119665003245885727L;
	private String newPassword = "";
	private String roomName = "";
	
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
