package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.storytime.client.changevieweventhandlers.JoinRoomWindowLocalEventHandler;
import com.storytime.client.changevieweventhandlers.LeaveJoinRoomLocalEventHandler;

public class LeaveJoinRoomPageLocalEvent extends GwtEvent<LeaveJoinRoomLocalEventHandler>{
	
	public static Type<LeaveJoinRoomLocalEventHandler> TYPE = new Type<LeaveJoinRoomLocalEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LeaveJoinRoomLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LeaveJoinRoomLocalEventHandler handler) {
		handler.onLeaveJoinRoomPage();
	}

}
