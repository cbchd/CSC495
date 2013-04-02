package com.storytime.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.storytime.client.changevieweventhandlers.HostRoomLocalEventHandler;
import com.storytime.client.changevieweventhandlers.LoginExistingUserLocalEventHandler;
import com.storytime.client.changeviewevents.HostRoomLocalEvent;
import com.storytime.client.changeviewevents.LoginExistingUserLocalEvent;
import com.storytime.client.presenters.LobbyPresenter;
import com.storytime.client.presenters.LobbyRoomPresenter;
import com.storytime.client.presenters.LoginPresenter;
import com.storytime.client.presenters.Presenter;
import com.storytime.client.view.LobbyRoomView;
import com.storytime.client.view.LobbyView;
import com.storytime.client.view.LoginView;

public class StoryTimeController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final StoryTimeServiceAsync rpcService;
	private HasWidgets container;
	private boolean DEBUG = true;

	public StoryTimeController(HandlerManager eventBus, StoryTimeServiceAsync rpcService) {
		History.addValueChangeHandler(this);
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		bindEventBusHandlers();
		if (DEBUG)
			System.out.println("Client: Bound the event bus view change handlers");
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// Deal with history updates
		String token = event.getValue();
		if (token != null) {
			Presenter presenter = null;
			if (token.equals("Login")) {
				if (DEBUG)System.out.println("Client: The history token was 'login' so the presenter associated with the login window was loaded!");
				presenter = new LoginPresenter(rpcService, eventBus, new LoginView());
				presenter.go(container);
			} else if (token.equals("Lobby")) {
				if (DEBUG)System.out.println("Client: The history token was 'Lobby' so the presenter associated with the lobby window was loaded!");
				presenter = new LobbyPresenter(rpcService, eventBus, new LobbyView());
				presenter.go(container);
			} else if (token.equals("Room")) {
				if (DEBUG) System.out.println("Client: The history token was 'Room' so the presenter associated with the lobby room was loaded");
				presenter = new LobbyRoomPresenter(rpcService, eventBus, new LobbyRoomView());
				presenter.go(container);
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		// Start the program at the login window
		this.container = container;
		if ("".equals(History.getToken())) {
			History.newItem("Login");
			if (DEBUG)
				System.out.println("Client: The current history was empty, so it was set to 'Login'");
		} else {
			if (DEBUG)
				System.out.println("Client: Fired the current history");
			History.fireCurrentHistoryState();
		}
	}

	public void bindEventBusHandlers() {
		eventBus.addHandler(LoginExistingUserLocalEvent.TYPE, new LoginExistingUserLocalEventHandler() {

			@Override
			public void onLoginExistingUser() {
				if (DEBUG)
					System.out.println("Client: Set the handler for 'login existing user button clicks'");
				if (DEBUG)
					System.out.println("Client: Set the new history token to be 'Lobby'");
				// Send the user to the lobby
				History.newItem("Lobby");
			}
		});
		eventBus.addHandler(HostRoomLocalEvent.TYPE, new HostRoomLocalEventHandler() {

			@Override
			public void onHostRoom() {
				if (DEBUG) System.out.println("Set history to 'Room' after recieving a host room event");
				History.newItem("Room");
			}
		});
	}
}
