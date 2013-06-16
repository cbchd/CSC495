package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.storytime.client.changevieweventhandlers.HostRoomWindowLocalEventHandler;
import com.storytime.client.changevieweventhandlers.JoinRoomLocalEventHandler;

public class HostRoomWindowLocalEvent extends GwtEvent<HostRoomWindowLocalEventHandler> {
	public static Type<HostRoomWindowLocalEventHandler> TYPE = new Type<HostRoomWindowLocalEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<HostRoomWindowLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HostRoomWindowLocalEventHandler handler) {
		handler.onGoToHostRoomWindow();
	}

}
