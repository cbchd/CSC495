package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.storytime.client.changevieweventhandlers.HostRoomLocalEventHandler;
import com.storytime.client.changevieweventhandlers.JoinRoomLocalEventHandler;

public class JoinRoomLocalEvent extends GwtEvent<JoinRoomLocalEventHandler>{
	
	public static Type<JoinRoomLocalEventHandler> TYPE = new Type<JoinRoomLocalEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<JoinRoomLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(JoinRoomLocalEventHandler handler) {
		handler.onJoinRoom();
	}

}
