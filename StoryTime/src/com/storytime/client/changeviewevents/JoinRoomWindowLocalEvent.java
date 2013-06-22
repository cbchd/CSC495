package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.storytime.client.changevieweventhandlers.JoinRoomWindowLocalEventHandler;

public class JoinRoomWindowLocalEvent extends GwtEvent<JoinRoomWindowLocalEventHandler> {

	private static final long serialVersionUID = -4928210984755975107L;
	public static Type<JoinRoomWindowLocalEventHandler> TYPE = new Type<JoinRoomWindowLocalEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<JoinRoomWindowLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(JoinRoomWindowLocalEventHandler handler) {
		handler.onGoToJoinRoomWindow();
	}
}
