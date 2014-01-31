package com.storytime.client.changeviewevents;

import com.google.gwt.event.shared.GwtEvent;
import com.storytime.client.changevieweventhandlers.LoginNewUserLocalEventHandler;

public class LoginNewUserLocalEvent extends GwtEvent<LoginNewUserLocalEventHandler> {

	public static Type<LoginNewUserLocalEventHandler> TYPE = new Type<LoginNewUserLocalEventHandler>();
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginNewUserLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginNewUserLocalEventHandler handler) {
		handler.onLoginNewUser();
	}
	
}
