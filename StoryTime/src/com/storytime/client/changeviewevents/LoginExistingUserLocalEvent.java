package com.storytime.client.changeviewevents;


import com.google.gwt.event.shared.GwtEvent;
import com.storytime.client.changevieweventhandlers.LoginExistingUserLocalEventHandler;

public class LoginExistingUserLocalEvent extends GwtEvent<LoginExistingUserLocalEventHandler> {
	
	private String username;
	private String password;
	public static Type<LoginExistingUserLocalEventHandler> TYPE = new Type<LoginExistingUserLocalEventHandler>();
	
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
	public com.google.gwt.event.shared.GwtEvent.Type<LoginExistingUserLocalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginExistingUserLocalEventHandler handler) {
		handler.onLoginExistingUser();
	}

}
