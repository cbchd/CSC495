package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.storytime.client.changevieweventhandlers.HostRoomLocalEventHandler;

public class HostRoomLocalEvent extends GwtEvent<HostRoomLocalEventHandler> {
	public static Type<HostRoomLocalEventHandler> TYPE = new Type<HostRoomLocalEventHandler>();
	private String roomName;
	private String theme;
	
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

	

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<HostRoomLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HostRoomLocalEventHandler handler) {
		handler.onHostRoom();
	}
}
