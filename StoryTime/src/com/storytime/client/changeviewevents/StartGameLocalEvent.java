package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.storytime.client.changevieweventhandlers.LoginExistingUserLocalEventHandler;
import com.storytime.client.changevieweventhandlers.StartGameLocalEventHandler;

public class StartGameLocalEvent extends GwtEvent<StartGameLocalEventHandler> {

	public static Type<StartGameLocalEventHandler> TYPE = new Type<StartGameLocalEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StartGameLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StartGameLocalEventHandler handler) {
		handler.onStartGame();
	}
}
