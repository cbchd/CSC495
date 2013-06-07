package com.storytime.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeOldEntryPoint;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.LeaveRoomLocalEvent;
import com.storytime.client.changeviewevents.StartGameLocalEvent;
import com.storytime.client.lobbyroom.GameStartEvent;
import com.storytime.client.lobbyroom.LobbyRoomData;
import com.storytime.client.lobbyroom.RoomDisbandedEvent;
import com.storytime.client.lobbyroom.UpdatePointCapEvent;
import com.storytime.client.lobbyroom.UpdateRoomChatWindowEvent;
import com.storytime.client.lobbyroom.UpdateTimerEvent;
import com.storytime.client.lobbyroom.UserLeftRoomEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class LobbyRoomView extends Composite implements com.storytime.client.presenters.LobbyRoomPresenter.Display {

	boolean DEBUG = true;

	RemoteEventService theRemoteEventService = StoryTimeEntryMVP.theRemoteEventService;
	StoryTimeServiceAsync storyTimeService = StoryTimeEntryMVP.rpcService;
	HandlerManager eventBus = StoryTimeEntryMVP.eventBus;
	public static LobbyRoomData roomData = new LobbyRoomData();
	String total = "";

	HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
	VerticalPanel gameDetailsPanel = new VerticalPanel();
	TabPanel tabPanel = new TabPanel();
	HorizontalPanel horizontalPanel = new HorizontalPanel();
	Grid grid_1 = new Grid(2, 2);
	Label lblPointLimit = new Label("Point Limit:");
	ValueListBox<Integer> pointLimitBox = new ValueListBox<Integer>(IntegerRenderer.instance());
	Label lblTimePerRound = new Label("Time Per Round:");
	ValueListBox<Integer> timePerRoundBox = new ValueListBox<Integer>(IntegerRenderer.instance());
	Grid grid = new Grid(1, 1);
	ListBox comboBox = new ListBox();
	Label lblUsers = new Label("Users");
	ListBox userListBox = new ListBox();
	Button btnLeaveRoom = new Button("Leave Room");
	VerticalPanel chatAndStartGameHolder = new VerticalPanel();
	Label theme = new Label("Theme:");
	Label lblChat = new Label("Chat");
	TextArea chatWindow = new TextArea();
	VerticalPanel textSendAndStartGamePanel = new VerticalPanel();
	HorizontalPanel sendTextBoxAndButtonPanel = new HorizontalPanel();
	TextBox chatTextBox = new TextBox();
	Button btnSendToChat = new Button("Send");
	Button btnStartGame = new Button("Start Game");
	private final Label label_1 = new Label("");

	public LobbyRoomView() {
		initWidget(mainHorizontalPanel);
		if (DEBUG)
			System.out.println("Client: Trying to initialize the lobby room view");
		initialize();
		if (DEBUG)
			System.out.println("Client: The lobby room view has been initialized");
	}

	public void initialize() {
		setPanelOrder();
		setPanelCharacteristics();
		if (DEBUG)
			System.out.println("Client: Set lobby room panel order & characteristics");
		if (DEBUG)
			System.out.println("Client: Trying to get the lobby room information from the server");
		getInitialLobbyRoomInformation();
		if (DEBUG)
			System.out.println("Client: Got the lobby room information from the server");
		populateLobbyRoomView();
		setHandlers();
	}

	public void setPanelOrder() {
		mainHorizontalPanel.add(gameDetailsPanel);
		gameDetailsPanel.add(tabPanel);
		tabPanel.add(horizontalPanel, "Game Settings", false);
		horizontalPanel.add(grid_1);
		tabPanel.add(grid, "My Profession", false);
		gameDetailsPanel.add(lblUsers);
		gameDetailsPanel.add(userListBox);
		gameDetailsPanel.add(btnLeaveRoom);
		mainHorizontalPanel.add(chatAndStartGameHolder);
		chatAndStartGameHolder.add(theme);

		chatAndStartGameHolder.add(label_1);
		label_1.setSize("441px", "1px");
		chatAndStartGameHolder.add(lblChat);
		chatAndStartGameHolder.add(chatWindow);
		textSendAndStartGamePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		chatAndStartGameHolder.add(textSendAndStartGamePanel);
		sendTextBoxAndButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textSendAndStartGamePanel.add(sendTextBoxAndButtonPanel);
		sendTextBoxAndButtonPanel.add(chatTextBox);
		sendTextBoxAndButtonPanel.add(btnSendToChat);
		textSendAndStartGamePanel.add(btnStartGame);
	}

	public void setPanelCharacteristics() {
		mainHorizontalPanel.setSize("968px", "505px");
		mainHorizontalPanel.setStyleName("LobbyRoomPage");

		gameDetailsPanel.setSize("50%", "100%");

		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("192px", "169px");

		horizontalPanel.setSize("5cm", "3cm");

		grid_1.setSize("187px", "111px");
		grid_1.setWidget(0, 0, lblPointLimit);
		grid_1.setWidget(0, 1, pointLimitBox);
		grid_1.setWidget(1, 0, lblTimePerRound);
		grid_1.setWidget(1, 1, timePerRoundBox);

		pointLimitBox.setValue(10);
		pointLimitBox.setValue(15);
		pointLimitBox.setValue(20);
		pointLimitBox.setValue(25);

		timePerRoundBox.setValue(roomData.timer);
		timePerRoundBox.setValue(15);
		timePerRoundBox.setValue(20);
		timePerRoundBox.setValue(25);
		timePerRoundBox.setValue(30);
		timePerRoundBox.setValue(35);
		timePerRoundBox.setValue(40);

		grid.setSize("5cm", "59px");
		grid.setWidget(0, 0, comboBox);

		comboBox.addItem("Pyromancer");
		comboBox.addItem("Golemancer");
		comboBox.addItem("Necromancer");
		comboBox.addItem("Hydromancer");
		comboBox.addItem("Arcanist");
		comboBox.setWidth("182px");

		lblUsers.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblUsers.setSize("100%", "100%");

		userListBox.setSize("95%", "169px");
		userListBox.setVisibleItemCount(5);

		btnLeaveRoom.setSize("100%", "30px");
		btnLeaveRoom.setStyleName("gwt-LoginExistingButton");

		chatAndStartGameHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		chatAndStartGameHolder.setSize("100%", "100%");

		theme.setText(roomData.theme);
		theme.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		theme.setSize("90%", "27px");

		lblChat.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblChat.setSize("90%", "100%");

		chatWindow.setReadOnly(true);
		chatWindow.setSize("90%", "316px");
		chatWindow.setText(total);
		textSendAndStartGamePanel.setSize("90%", "100%");

		sendTextBoxAndButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		sendTextBoxAndButtonPanel.setSize("100%", "100%");

		chatTextBox.setSize("100%", "20px");

		btnSendToChat.setStyleName("gwt-LoginExistingButton");
		btnSendToChat.setSize("100%", "30px");

		btnStartGame.setStyleName("gwt-LoginExistingButton");
		btnStartGame.setSize("145px", "30px");
	}

	public void setHandlers() {
		pointLimitBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				// Get point Limit TODO
				storyTimeService.updateLobbyRoomPointCap(roomData.roomName, event.getValue(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						if (DEBUG)
							System.out.println("Client: Error occurred changing the point value");
					}

					@Override
					public void onSuccess(Void result) {
						if (DEBUG)
							System.out.println("Client: Got confirmation of point change from server");
					}

				});
			}
		});

		timePerRoundBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(final ValueChangeEvent<Integer> event) {
				// Get time per round TODO
				storyTimeService.updateLobbyRoomTimer(roomData.roomName, event.getValue(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						if (DEBUG)
							System.out.println("Client: Error occurred in sending server the new timer value: " + event.getValue());
					}

					@Override
					public void onSuccess(Void result) {
						if (DEBUG)
							System.out.println("Client: Got confirmation of timer change from server");
					}

				});
			}
		});

		btnLeaveRoom.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				storyTimeService.leaveRoom(roomData.roomName, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void result) {
						if (DEBUG)
							System.out.println("Client: Confirmation received from server that this user has left the room: " + roomData.roomName);
					}
				});
			}
		});

		chatTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && !chatTextBox.getText().equalsIgnoreCase("")) {
					storyTimeService.sendRoomChatMessage(roomData.roomName, chatTextBox.getText(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							if (DEBUG)
								System.out.println("Client: Error occurred while sending text to the chat");
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Successfully posted to chat window");
							chatTextBox.setText("");
						}
					});
				}
			}
		});

		btnSendToChat.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Send text to users in room
				if (!chatTextBox.getText().equalsIgnoreCase("")) {
					storyTimeService.sendRoomChatMessage(roomData.roomName, chatTextBox.getText(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Sent message: " + chatTextBox.getText());
							chatTextBox.setText("");
						}

					});
				}
			}
		});

		btnStartGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				storyTimeService.startGame(roomData.roomName, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						if (DEBUG)
							System.out.println("Client: Error in the start game call");
					}

					@Override
					public void onSuccess(Void result) {
						if (DEBUG)
							System.out.println("Client: Confirmation of started game recieved");
					}
				});
			}
		});
	}

	public void setRemoteEventListenersAndHandleEvents() {
		if (DEBUG)
			System.out.println("Client: Trying to set the lobby room listeners for room " + roomData.roomName);
		theRemoteEventService.addListener(DomainFactory.getDomain(roomData.roomName), new RemoteEventListener() {

			public void apply(Event anEvent) {
				if (anEvent instanceof UpdatePointCapEvent) {
					// Update the point cap set for the room
					if (DEBUG)
						System.out.println("Client: Got an Update Point Cap Remote Event");
					UpdatePointCapEvent updatePointCapEvent = (UpdatePointCapEvent) anEvent;
					pointLimitBox.setValue(updatePointCapEvent.pointCap);
					roomData.pointCap = updatePointCapEvent.pointCap;
				} else if (anEvent instanceof UpdateTimerEvent) {
					// Update the timer
					if (DEBUG)
						System.out.println("Client: Got an Update Timer Remote Event");
					UpdateTimerEvent updateTimerEvent = (UpdateTimerEvent) anEvent;
					timePerRoundBox.setValue(updateTimerEvent.timer);
					roomData.timer = updateTimerEvent.timer;
				} else if (anEvent instanceof UserLeftRoomEvent) {
					// Remove the user from the user list
					UserLeftRoomEvent leftRoomEvent = (UserLeftRoomEvent) anEvent;
					for (String username : roomData.users) {
						if (username.equalsIgnoreCase(leftRoomEvent.username)) {
							roomData.users.remove(username);
						}
					}

				} else if (anEvent instanceof UpdateRoomChatWindowEvent) {
					UpdateRoomChatWindowEvent chatWindowEvent = (UpdateRoomChatWindowEvent) anEvent;
					roomData.messages.add(chatWindowEvent.message);
					if (DEBUG)
						System.out.println("Client: Got message: " + chatWindowEvent.message + ", from server");

					String total = "";
					for (String message : roomData.messages) {
						total += message + "\n";
					}
					chatWindow.setText(total);
					chatWindow.setCursorPos(chatWindow.getText().length());
				} else if (anEvent instanceof RoomDisbandedEvent) {
					theRemoteEventService.removeListeners();
					if (DEBUG)
						System.out.println("Client: Recieved Disband Room Event & Deactivated Listeners For Room: " + roomData.roomName);
					if (DEBUG)
						System.out.println("Client: Left Room: " + roomData.roomName);
					// StoryTimeOldEntryPoint.controller("Lobby");
					eventBus.fireEvent(new LeaveRoomLocalEvent());
				} else if (anEvent instanceof GameStartEvent) {
					if (DEBUG)
						System.out.println("Client: Got a game start event for room: " + roomData.roomName);
					theRemoteEventService.removeListeners();
					if (DEBUG)
						System.out.println("Client: Deactivated lobby room listeners in preparation for starting the game");
					StartGameLocalEvent startGameEvent = new StartGameLocalEvent();
					if (DEBUG)
						System.out.println("Client: Fired Local Event: Game Start Event");
					eventBus.fireEvent(startGameEvent);
				}
			}
		});
		if (DEBUG)
			System.out.println("Client: Lobby room listeners activated");
	}

	public void getInitialLobbyRoomInformation() {
		storyTimeService.getLobbyRoomInformation(new AsyncCallback<LobbyRoomData>() {

			@Override
			public void onFailure(Throwable caught) {
				if (DEBUG)
					System.out.println("Client: Error occurred while initializing the lobby room state");
			}

			@Override
			public void onSuccess(LobbyRoomData result) {
				roomData.pointCap = result.pointCap;
				roomData.roomName = result.roomName;
				roomData.theme = result.theme;
				roomData.timer = result.timer;
				roomData.users = result.users;
				roomData.inGame = result.inGame;
				roomData.domain = result.domain;
				System.out.println("Client: Got Data: PointCap: " + roomData.pointCap + ", Name: " + roomData.roomName + ", Theme: " + roomData.theme + ", " + "Timer: " + roomData.timer
						+ ", Users: " + roomData.users.toString() + ", and Domain: " + roomData.domain.getName());
				populateLobbyRoomView();
				if (DEBUG)
					System.out.println("Client: Set remote event listeners and populated the lobby with known data");
				setRemoteEventListenersAndHandleEvents();
			}
		});
	}

	public void populateLobbyRoomView() {
		userListBox.clear();
		for (String users : roomData.users) {
			userListBox.addItem(users);
		}
		total = "";
		for (String message : roomData.messages) {
			total += message + "\n";
		}
		theme.setText(roomData.theme);
	}

	public Widget asWidget() {
		return this;
	}
}