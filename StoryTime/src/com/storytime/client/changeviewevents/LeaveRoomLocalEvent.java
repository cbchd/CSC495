package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.storytime.client.changevieweventhandlers.LeaveRoomLocalEventHandler;

public class LeaveRoomLocalEvent extends GwtEvent<LeaveRoomLocalEventHandler> {
	public static Type<LeaveRoomLocalEventHandler> TYPE = new Type<LeaveRoomLocalEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LeaveRoomLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LeaveRoomLocalEventHandler handler) {
		handler.onLeaveRoom();
	}
}
