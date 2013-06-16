package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.storytime.client.changevieweventhandlers.CustomizeSpellsLocalEventHandler;

public class CustomizeSpellsLocalEvent extends GwtEvent<CustomizeSpellsLocalEventHandler> {

	public static Type<CustomizeSpellsLocalEventHandler> TYPE = new Type<CustomizeSpellsLocalEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CustomizeSpellsLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CustomizeSpellsLocalEventHandler handler) {
		handler.onGoToCustomizeSpellsPage();
	}

}
