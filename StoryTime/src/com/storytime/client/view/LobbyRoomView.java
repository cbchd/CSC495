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
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.LeaveRoomLocalEvent;
import com.storytime.client.changeviewevents.StartGameLocalEvent;
import com.storytime.client.lobbyroom.GameStartEvent;
import com.storytime.client.lobbyroom.LobbyRoomData;
import com.storytime.client.lobbyroom.RoomDisbandedEvent;
import com.storytime.client.lobbyroom.UpdateAuthorsTimerEvent;
import com.storytime.client.lobbyroom.UpdateMastersTimerEvent;
import com.storytime.client.lobbyroom.UpdatePointLimitEvent;
import com.storytime.client.lobbyroom.UpdateRoomChatWindowEvent;
import com.storytime.client.lobbyroom.UserEnteredRoomEvent;
import com.storytime.client.lobbyroom.UserLeftRoomEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class LobbyRoomView extends Composite implements
		com.storytime.client.presenters.LobbyRoomPresenter.Display {

	boolean DEBUG = true;

	RemoteEventService theRemoteEventService = StoryTimeEntryMVP.theRemoteEventService;
	StoryTimeServiceAsync storyTimeService = StoryTimeEntryMVP.rpcService;
	HandlerManager eventBus = StoryTimeEntryMVP.eventBus;
	public static LobbyRoomData roomData = new LobbyRoomData();
	String total = "";
	String username = "";

	HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
	VerticalPanel gameDetailsPanel = new VerticalPanel();
	TabPanel tabPanel = new TabPanel();
	HorizontalPanel horizontalPanel = new HorizontalPanel();
	Grid grid_1 = new Grid(4, 2);
	Label lblPointLimit = new Label("Point Limit:");
	ValueListBox<Integer> pointLimitBox = new ValueListBox<Integer>(
			IntegerRenderer.instance());
	Label lblSubmissionTime = new Label("Author's Time:");
	ValueListBox<Integer> submittersTimeBox = new ValueListBox<Integer>(
			IntegerRenderer.instance());
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
	Label label_1 = new Label("");
	Label lblChooseTime = new Label("Master's Time:");
	ValueListBox<Integer> choosersTimeBox = new ValueListBox<Integer>(
			IntegerRenderer.instance());
	private final Label lblPassword = new Label("Password:");
	private final SimpleCheckBox passwordEnabledCheckBox = new SimpleCheckBox();
	PasswordPopupView passwordPopup = new PasswordPopupView();

	public LobbyRoomView() {
		initWidget(mainHorizontalPanel);
		if (DEBUG)
			System.out
					.println("Client: Trying to initialize the lobby room view");
		chatWindow.setText("");
		passwordEnabledCheckBox.setVisible(false);
		lblPassword.setVisible(false);
		initialize();
		if (DEBUG)
			System.out
					.println("Client: The lobby room view has been initialized");
	}

	private void initialize() {
		getAndSetMyUsername();
		setPanelOrder();
		setPanelCharacteristics();
		if (DEBUG)
			System.out
					.println("Client: Set lobby room panel order & characteristics");
		if (DEBUG)
			System.out
					.println("Client: Trying to get the lobby room information from the server");
		getInitialLobbyRoomInformation();
		if (DEBUG)
			System.out
					.println("Client: Got the lobby room information from the server");
		populateLobbyRoomView();
		setHandlers();
	}

	private void setPanelOrder() {
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
		textSendAndStartGamePanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		chatAndStartGameHolder.add(textSendAndStartGamePanel);
		sendTextBoxAndButtonPanel
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textSendAndStartGamePanel.add(sendTextBoxAndButtonPanel);
		sendTextBoxAndButtonPanel.add(chatTextBox);
		sendTextBoxAndButtonPanel.add(btnSendToChat);
		textSendAndStartGamePanel.add(btnStartGame);
	}

	private void setPanelCharacteristics() {
		mainHorizontalPanel.setSize("968px", "505px");
		mainHorizontalPanel.setStyleName("LobbyRoomPage");
		passwordPopup.lblIncorrectPassword.setText("");
		gameDetailsPanel.setSize("50%", "100%");

		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("192px", "169px");

		horizontalPanel.setSize("5cm", "3cm");

		grid_1.setSize("187px", "111px");
		grid_1.setWidget(0, 0, lblPointLimit);
		grid_1.setWidget(0, 1, pointLimitBox);
		grid_1.setWidget(1, 0, lblSubmissionTime);
		lblSubmissionTime.setWidth("257px");
		grid_1.setWidget(1, 1, submittersTimeBox);

		grid_1.setWidget(2, 0, lblChooseTime);
		grid_1.setWidget(2, 1, choosersTimeBox);

		grid_1.setWidget(3, 0, lblPassword);
		grid_1.setWidget(3, 1, passwordEnabledCheckBox);
		passwordEnabledCheckBox.setSize("80%", "100%");

		pointLimitBox.setValue(5);
		pointLimitBox.setValue(10);
		pointLimitBox.setValue(15);
		pointLimitBox.setValue(20);
		pointLimitBox.setValue(25);
		pointLimitBox.setValue(30);
		pointLimitBox.setValue(35);
		pointLimitBox.setValue(40);
		pointLimitBox.setValue(45);
		pointLimitBox.setValue(50);
		pointLimitBox.setValue(55);
		pointLimitBox.setValue(60);

		submittersTimeBox.setValue(5);
		submittersTimeBox.setValue(10);
		submittersTimeBox.setValue(15);
		submittersTimeBox.setValue(20);
		submittersTimeBox.setValue(25);
		submittersTimeBox.setValue(30);
		submittersTimeBox.setValue(35);
		submittersTimeBox.setValue(40);

		choosersTimeBox.setValue(submittersTimeBox.getValue() + 5);

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

		chatAndStartGameHolder
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
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

		sendTextBoxAndButtonPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		sendTextBoxAndButtonPanel.setSize("100%", "100%");

		chatTextBox.setSize("100%", "20px");

		btnSendToChat.setStyleName("gwt-LoginExistingButton");
		btnSendToChat.setSize("100%", "30px");

		btnStartGame.setStyleName("gwt-LoginExistingButton");
		btnStartGame.setSize("145px", "30px");
	}

	private void setHandlers() {
		passwordEnabledCheckBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!passwordEnabledCheckBox.isEnabled()) {
					passwordPopup.textBox.setText("");
					passwordPopup.setVisible(true);
					passwordPopup.show();
				} else {
					passwordPopup.textBox.setText("");
					setPassword();
				}
			}
		});
		passwordEnabledCheckBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					setPassword();
				}
			}
			
		});
		passwordPopup.btnEnter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				storyTimeService.setPassword(roomData.roomName,
						passwordPopup.textBox.getText(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								if (DEBUG)
									System.out
											.println("Client: Failed to tell the server what the new password for room "
													+ roomData.roomName + " is");
							}

							@Override
							public void onSuccess(Void result) {
								if (DEBUG)
									System.out
											.println("Client: Successfully told the server to change the password for room "
													+ roomData.roomName);
								passwordPopup.textBox.setText("");
								passwordPopup.hide();
							}

						});
			}

		});

		pointLimitBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				storyTimeService.updateLobbyRoomPointLimit(roomData.roomName,
						event.getValue(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								if (DEBUG)
									System.out
											.println("Client: Error occurred changing the point value");
							}

							@Override
							public void onSuccess(Void result) {
								if (DEBUG)
									System.out
											.println("Client: Got confirmation of point change from server");
							}

						});
			}
		});

		submittersTimeBox
				.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					@Override
					public void onValueChange(
							final ValueChangeEvent<Integer> event) {
						// Get time per round TODO
						storyTimeService.updateLobbyRoomTimer(
								roomData.roomName, event.getValue(),
								new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										if (DEBUG)
											System.out
													.println("Client: Error occurred in sending server the new submissionTimer value: "
															+ event.getValue());
									}

									@Override
									public void onSuccess(Void result) {
										if (DEBUG)
											System.out
													.println("Client: Got confirmation of submissionTimer change from server");
										choosersTimeBox.setValue(
												submittersTimeBox.getValue() + 5,
												true);
									}
								});
					}
				});

		choosersTimeBox
				.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					public void onValueChange(ValueChangeEvent<Integer> event) {
						storyTimeService.updateLobbyRoomChooserTimer(
								roomData.roomName, choosersTimeBox.getValue(),
								new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										if (DEBUG)
											System.out
													.println("Client: Failed to tell server to update the chooser's time for room: "
															+ roomData.roomName);
									}

									@Override
									public void onSuccess(Void result) {
										if (DEBUG)
											System.out
													.println("Client: Sent the server the new chooser's time for room: "
															+ roomData.roomName);
									}

								});
					}
				});

		btnLeaveRoom.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				storyTimeService.leaveRoom(roomData.roomName,
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Void result) {
								if (DEBUG)
									System.out
											.println("Client: Confirmation received from server that this user has left the room: "
													+ roomData.roomName);
							}
						});
			}
		});

		chatTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
						&& !chatTextBox.getText().equalsIgnoreCase("")) {
					storyTimeService.sendRoomChatMessage(roomData.roomName,
							chatTextBox.getText(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									if (DEBUG)
										System.out
												.println("Client: Error occurred while sending text to the chat");
								}

								@Override
								public void onSuccess(Void result) {
									if (DEBUG)
										System.out
												.println("Client: Successfully posted to chat window");
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
					storyTimeService.sendRoomChatMessage(roomData.roomName,
							chatTextBox.getText(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
								}

								@Override
								public void onSuccess(Void result) {
									if (DEBUG)
										System.out
												.println("Client: Sent message: "
														+ chatTextBox.getText());
									chatTextBox.setText("");
								}

							});
				}
			}
		});

		btnStartGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startGame();
			}
		});

	}

	private void setRemoteEventListenersAndHandleEvents() {
		if (DEBUG)
			System.out
					.println("Client: Trying to set the lobby room listeners for room "
							+ roomData.roomName);
		theRemoteEventService.addListener(
				DomainFactory.getDomain(roomData.roomName),
				new RemoteEventListener() {

					public void apply(Event anEvent) {
						if (anEvent instanceof UpdatePointLimitEvent) {
							UpdatePointLimitEvent updatePointLimitEvent = (UpdatePointLimitEvent) anEvent;
							onUpdatePointLimit(updatePointLimitEvent);

						} else if (anEvent instanceof UpdateAuthorsTimerEvent) {
							UpdateAuthorsTimerEvent updateAuthorsTimerEvent = (UpdateAuthorsTimerEvent) anEvent;
							onUpdateAuthorsTimer(updateAuthorsTimerEvent);

						} else if (anEvent instanceof UpdateMastersTimerEvent) {
							UpdateMastersTimerEvent chooserTimerEvent = (UpdateMastersTimerEvent) anEvent;
							onUpdateChooseTime(chooserTimerEvent.mastersTime);

						} else if (anEvent instanceof UserLeftRoomEvent) {
							UserLeftRoomEvent userLeftRoomEvent = (UserLeftRoomEvent) anEvent;
							onUserLeftRoom(userLeftRoomEvent);

						} else if (anEvent instanceof UserEnteredRoomEvent) {
							UserEnteredRoomEvent userEnteredRoomEvent = (UserEnteredRoomEvent) anEvent;
							onUserEnteredRoom(userEnteredRoomEvent);

						} else if (anEvent instanceof UpdateRoomChatWindowEvent) {
							UpdateRoomChatWindowEvent updateRoomChatWindowEvent = (UpdateRoomChatWindowEvent) anEvent;
							onUpdateRoomChatWindow(updateRoomChatWindowEvent);

						} else if (anEvent instanceof RoomDisbandedEvent) {
							RoomDisbandedEvent roomDisbandedEvent = new RoomDisbandedEvent();
							onRoomDisbanded(roomDisbandedEvent);

						} else if (anEvent instanceof GameStartEvent) {
							GameStartEvent gameStartEvent = new GameStartEvent();
							onGameStart(gameStartEvent);

						}
					}
				});
		if (DEBUG)
			System.out.println("Client: Lobby room listeners activated");
	}

	private void getInitialLobbyRoomInformation() {
		storyTimeService
				.getLobbyRoomInformation(new AsyncCallback<LobbyRoomData>() {

					@Override
					public void onFailure(Throwable caught) {
						if (DEBUG)
							System.out
									.println("Client: Error occurred while initializing the lobby room state");
					}

					@Override
					public void onSuccess(LobbyRoomData result) {
						roomData.pointCap = result.pointCap;
						roomData.roomName = result.roomName;
						roomData.theme = result.theme;
						roomData.authorsTimer = result.authorsTimer;
						roomData.users = result.users;
						roomData.inGame = result.inGame;
						roomData.domain = result.domain;
						roomData.messages = result.messages;
						roomData.hostsName = result.hostsName;
						roomData.setPassword(result.getPassword());
						System.out.println("Client: Got Data: PointCap: "
								+ roomData.pointCap + ", Name: "
								+ roomData.roomName + ", Theme: "
								+ roomData.theme + ", " + "Timer: "
								+ roomData.authorsTimer + ", Users: "
								+ roomData.users.toString() + ", and Domain: "
								+ roomData.domain.getName());
						if (roomData.getHostsName().equalsIgnoreCase(username)) {
							if (DEBUG)
								System.out.println("Client: "
										+ username
										+ " is the host of room: "
										+ roomData.getRoomName()
										+ " so he is able to edit the room password");
							passwordEnabledCheckBox.setVisible(true);
							lblPassword.setVisible(true);
							if (!roomData.getPassword().equalsIgnoreCase("")) {
								passwordEnabledCheckBox.setValue(true);
							}
						}
						populateLobbyRoomView();
						if (DEBUG)
							System.out
									.println("Client: Set remote event listeners and populated the lobby with known data");
						setRemoteEventListenersAndHandleEvents();
					}
				});
	}

	private void getAndSetMyUsername() {
		storyTimeService.getMyUsername(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				if (DEBUG)
					System.out
							.println("Client: Failed to get this users username from the server");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String result) {
				username = result;
			}

		});
	}

	private void populateLobbyRoomView() {
		populateUserList();
		populateMessages();
		populateTheme();
	}

	private void populateUserList() {
		userListBox.clear();
		for (String users : roomData.users) {
			userListBox.addItem(users);
		}
	}

	private void populateMessages() {
		total = "";
		for (String message : roomData.messages) {
			total += message + "\n";
		}
		chatWindow.setText(total);
		chatWindow.setCursorPos(chatWindow.getText().length());
	}

	private void populateTheme() {
		theme.setText(roomData.theme);
	}

	private void onUpdateChooseTime(int chooseTime) {
		if (DEBUG)
			System.out
					.println("Client: Got an UpdateChooserTimerEvent for room: "
							+ roomData.roomName);
		choosersTimeBox.setValue(chooseTime);
		if (DEBUG)
			System.out.println("Client: Set the choose time to: " + chooseTime);
	}

	private void onUpdatePointLimit(UpdatePointLimitEvent updatePointLimitEvent) {
		if (DEBUG)
			System.out.println("Client: Got an Update Point Cap Remote Event");
		pointLimitBox.setValue(updatePointLimitEvent.getPointLimit());
		roomData.pointCap = updatePointLimitEvent.getPointLimit();
	}

	private void onUpdateAuthorsTimer(
			UpdateAuthorsTimerEvent updateAuthorsTimerEvent) {
		if (DEBUG)
			System.out.println("Client: Got an Update Timer Remote Event");
		submittersTimeBox.setValue(updateAuthorsTimerEvent.authorsTimer);
		roomData.authorsTimer = updateAuthorsTimerEvent.authorsTimer;
	}

	private void onUserLeftRoom(UserLeftRoomEvent userLeftRoomEvent) {
		if (DEBUG)
			System.out.println("Client: Got a UserLeftRoomEvent for user: "
					+ userLeftRoomEvent.username);
		int indexToBeRemoved = -1;
		for (int x = 0; x < roomData.users.size(); x++) {
			if (roomData.users.get(x).equalsIgnoreCase(
					userLeftRoomEvent.username)) {
				if (DEBUG)
					System.out.println("Client: Found user: "
							+ userLeftRoomEvent.username + " in the user list");
				indexToBeRemoved = x;
			}
		}
		if (indexToBeRemoved != -1) {
			roomData.users.remove(indexToBeRemoved);
			populateUserList();
			if (DEBUG)
				System.out.println("Client: Removed "
						+ userLeftRoomEvent.username + " from the user list");
		}
		if (userLeftRoomEvent.username.equalsIgnoreCase(username)) {
			if (DEBUG)
				System.out
						.println("Client: This user was the user who requested to leave the room. Firing a LeaveRoomLocalEvent");
			theRemoteEventService.removeListeners();
			eventBus.fireEvent(new LeaveRoomLocalEvent());
		}
	}

	private void onUserEnteredRoom(UserEnteredRoomEvent userEnteredRoomEvent) {
		String username = userEnteredRoomEvent.getUsername();
		if (DEBUG)
			System.out.println("Client: Received UserEnteredRoomEvent ("
					+ username + ") for room: " + roomData.roomName);
		roomData.users.add(username);
		populateUserList();
	}

	private void onUpdateRoomChatWindow(
			UpdateRoomChatWindowEvent updateRoomChatWindowEvent) {
		roomData.messages.add(updateRoomChatWindowEvent.message);
		if (DEBUG)
			System.out.println("Client: Got message: "
					+ updateRoomChatWindowEvent.message + ", from server");

		populateMessages();
	}

	private void onRoomDisbanded(RoomDisbandedEvent roomDisbandedEvent) {
		theRemoteEventService.removeListeners();
		if (DEBUG)
			System.out
					.println("Client: Recieved Disband Room Event & Deactivated Listeners For Room: "
							+ roomData.roomName);
		if (DEBUG)
			System.out.println("Client: Left Room: " + roomData.roomName);
		eventBus.fireEvent(new LeaveRoomLocalEvent());
	}

	private void onGameStart(GameStartEvent gameStartEvent) {
		if (DEBUG)
			System.out.println("Client: Got a game start event for room: "
					+ roomData.roomName);
		theRemoteEventService.removeListeners();
		if (DEBUG)
			System.out
					.println("Client: Deactivated lobby room listeners in preparation for starting the game");
		StartGameLocalEvent startGameEvent = new StartGameLocalEvent();
		if (DEBUG)
			System.out.println("Client: Fired Local Event: Game Start Event");
		eventBus.fireEvent(startGameEvent);
	}

	private void startGame() {
		storyTimeService.startGame(roomData.roomName,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						if (DEBUG)
							System.out
									.println("Client: Error in the start game call");
					}

					@Override
					public void onSuccess(Void result) {
						if (DEBUG)
							System.out
									.println("Client: Confirmation of started game recieved");
					}
				});
	}
	
	private void setPassword() {
		String password = passwordPopup.textBox.getText();
		storyTimeService.setPassword(roomData.getRoomName(), password, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (DEBUG) System.out.println("Client: Failed to tell the server to change the password for room: " + roomData.getRoomName());
			}

			@Override
			public void onSuccess(Void result) {
				if (DEBUG) System.out.println("Client: Successfully told server to change the password for room: " + roomData.getRoomName());
			}
			
		});
	}
	public Widget asWidget() {
		return this;
	}
}