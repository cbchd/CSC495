package com.storytime.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.storytime.client.changevieweventhandlers.CustomizeSpellsLocalEventHandler;
import com.storytime.client.changevieweventhandlers.HostRoomLocalEventHandler;
import com.storytime.client.changevieweventhandlers.HostRoomWindowLocalEventHandler;
import com.storytime.client.changevieweventhandlers.JoinRoomLocalEventHandler;
import com.storytime.client.changevieweventhandlers.JoinRoomWindowLocalEventHandler;
import com.storytime.client.changevieweventhandlers.LeaveRoomLocalEventHandler;
import com.storytime.client.changevieweventhandlers.LoginExistingUserLocalEventHandler;
import com.storytime.client.changevieweventhandlers.StartGameLocalEventHandler;
import com.storytime.client.changeviewevents.CustomizeSpellsLocalEvent;
import com.storytime.client.changeviewevents.HostRoomLocalEvent;
import com.storytime.client.changeviewevents.HostRoomWindowLocalEvent;
import com.storytime.client.changeviewevents.JoinRoomLocalEvent;
import com.storytime.client.changeviewevents.JoinRoomWindowLocalEvent;
import com.storytime.client.changeviewevents.LeaveRoomLocalEvent;
import com.storytime.client.changeviewevents.LoginExistingUserLocalEvent;
import com.storytime.client.changeviewevents.StartGameLocalEvent;
import com.storytime.client.presenters.GameInProgressRoomPresenter;
import com.storytime.client.presenters.HostRoomPresenter;
import com.storytime.client.presenters.LobbyPresenter;
import com.storytime.client.presenters.LobbyRoomPresenter;
import com.storytime.client.presenters.LoginPresenter;
import com.storytime.client.presenters.Presenter;
import com.storytime.client.presenters.SpellCustomizationPresenter;
import com.storytime.client.view.GameInProgressRoomView;
import com.storytime.client.view.HostRoomView;
import com.storytime.client.view.JoinRoomView;
import com.storytime.client.view.LobbyRoomView;
import com.storytime.client.view.LobbyView;
import com.storytime.client.view.LoginView;
import com.storytime.client.view.SpellCustomizationView;

public class StoryTimeController implements Presenter,
		ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final StoryTimeServiceAsync rpcService;
	private HasWidgets container;
	private boolean DEBUG = true;
	public static String location;

	public StoryTimeController(HandlerManager eventBus,
			StoryTimeServiceAsync rpcService) {
		History.addValueChangeHandler(this);
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		bindEventBusHandlers();
		if (DEBUG)
			System.out
					.println("Client: Bound the event bus view change handlers");
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// Deal with history updates
		String token = event.getValue();
		if (token != null) {
			Presenter presenter = null;
			if (token.equalsIgnoreCase("Login")) {
				if (DEBUG)
					System.out
							.println("Client: The history token was 'login' so the presenter associated with the login window was loaded");
				presenter = new LoginPresenter(rpcService, eventBus,
						new LoginView());
				presenter.go(container);
			} else if (token.equalsIgnoreCase("Lobby")) {
				if (DEBUG)
					System.out
							.println("Client: The history token was 'Lobby' so the presenter associated with the lobby window was loaded");
				presenter = new LobbyPresenter(rpcService, eventBus,
						new LobbyView());
				presenter.go(container);
			} else if (token.equalsIgnoreCase("HostRoom")) {
				if (DEBUG)
					System.out
							.println("Client: The history token was 'HostRoom' so the presenter associated with the host room was loaded");
				// Window.open("http://192.168.1.2:8888/StoryTime.html?gwt.codesvr=192.168.1.2:9997#HostRoom",
				// "RoomSetup", "");
				presenter = new HostRoomPresenter(rpcService, eventBus,
						new HostRoomView());
				presenter.go(container);
			} else if (token.equalsIgnoreCase("JoinRoom")) {
				presenter = new HostRoomPresenter(rpcService, eventBus,
						new JoinRoomView());
				presenter.go(container);
			} else if (token.equalsIgnoreCase("Room")) {
				if (DEBUG)
					System.out
							.println("Client: The history token was 'Room' so the presenter associated with the lobby room was loaded");
				presenter = new LobbyRoomPresenter(rpcService, eventBus,
						new LobbyRoomView());
				presenter.go(container);
			} else if (token.equalsIgnoreCase("GameRoom")) {
				if (DEBUG)
					System.out
							.println("Client: The history token was 'GameRoom' so the presenter associated with the game room was loaded");
				presenter = new GameInProgressRoomPresenter(rpcService,
						eventBus, new GameInProgressRoomView());
				presenter.go(container);
			} else if (token.equalsIgnoreCase("Spells")) {
				if (DEBUG)
					System.out
							.println("Client: The history token was 'Spells' so the presenter associated with the spell customization page was loaded");
				presenter = new SpellCustomizationPresenter(rpcService,
						eventBus, new SpellCustomizationView());
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
				System.out
						.println("Client: The current history was empty, so it was set to 'Login'");
		} else {
			if (DEBUG)
				System.out.println("Client: Fired the current history");
			History.fireCurrentHistoryState();
		}
	}

	public void bindEventBusHandlers() {
		eventBus.addHandler(LoginExistingUserLocalEvent.TYPE,
				new LoginExistingUserLocalEventHandler() {

					@Override
					public void onLoginExistingUser() {
						if (DEBUG)
							System.out
									.println("Client: Set the handler for 'login existing user button clicks'");
						if (DEBUG)
							System.out
									.println("Client: Set the new history token to be 'Lobby'");
						// Send the user to the lobby
						History.newItem("Lobby");
					}
				});
		eventBus.addHandler(HostRoomLocalEvent.TYPE,
				new HostRoomLocalEventHandler() {

					@Override
					public void onHostRoom() {
						History.newItem("Room");
						if (DEBUG)
							System.out
									.println("Client: Set history to 'Room' after recieving a host room local event");
					}
				});
		eventBus.addHandler(HostRoomWindowLocalEvent.TYPE,
				new HostRoomWindowLocalEventHandler() {

					@Override
					public void onGoToHostRoomWindow() {
						if (DEBUG)
							System.out
									.println("Client: Set history to 'HostRoom' after receiving a host room window local event");
						History.newItem("HostRoom");
					}

				});

		eventBus.addHandler(JoinRoomWindowLocalEvent.TYPE,
				new JoinRoomWindowLocalEventHandler() {

					@Override
					public void onGoToJoinRoomWindow() {
						History.newItem("JoinRoom");
					}

				});

		eventBus.addHandler(StartGameLocalEvent.TYPE,
				new StartGameLocalEventHandler() {

					@Override
					public void onStartGame() {
						if (DEBUG)
							System.out
									.println("Client: Set history to 'GameRoom' after recieving a start game local event");
						History.newItem("GameRoom");
					}

				});
		eventBus.addHandler(JoinRoomLocalEvent.TYPE,
				new JoinRoomLocalEventHandler() {

					@Override
					public void onJoinRoom() {
						History.newItem("Room");
					}

				});
		eventBus.addHandler(LeaveRoomLocalEvent.TYPE,
				new LeaveRoomLocalEventHandler() {

					@Override
					public void onLeaveRoom() {
						if (DEBUG)
							System.out
									.println("Client: Set history to 'Lobby' after recieving a leave room local event");
						History.newItem("Lobby");
					}
				});
		eventBus.addHandler(CustomizeSpellsLocalEvent.TYPE,
				new CustomizeSpellsLocalEventHandler() {

					@Override
					public void onGoToCustomizeSpellsPage() {
						History.newItem("Spells");
					}

				});
	}
}
