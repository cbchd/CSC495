package com.storytime.client.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.HostRoomLocalEvent;
import com.storytime.client.changeviewevents.JoinRoomLocalEvent;
import com.storytime.client.lobby.LobbyInformation;
import com.storytime.client.lobby.UpdateLobbyMessagesEvent;
import com.storytime.client.lobby.UpdateLobbyRoomsEvent;
import com.storytime.client.lobby.UpdateLobbyUsersEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class LobbyView extends Composite implements com.storytime.client.presenters.LobbyPresenter.Display {

	boolean DEBUG = true;
	ArrayList<String> usersInLobby = new ArrayList<String>();
	ArrayList<String> availableRooms = new ArrayList<String>();
	ArrayList<String> chatWindowMessages = new ArrayList<String>();
	String roomSelection = "";
	StoryTimeServiceAsync rpcService = StoryTimeEntryMVP.rpcService;
	RemoteEventService theRemoteEventService = StoryTimeEntryMVP.theRemoteEventService;
	String totalChat = "";
	HandlerManager eventBus = StoryTimeEntryMVP.eventBus;

	FlowPanel mainFlowPanel = new FlowPanel();
	HorizontalPanel horizontalInMainFlow = new HorizontalPanel();

	VerticalPanel firstVerticalUsersInLobby = new VerticalPanel();
	Label lblUsersInLobby = new Label("Users in Lobby");
	ListBox usersListBox = new ListBox();

	Label lblHellousername = new Label("Lobby Chat");

	VerticalPanel secondVerticalLobbyChat = new VerticalPanel();
	TextArea chatTextArea = new TextArea();
	HorizontalPanel textToSendBoxAndButtonHolder = new HorizontalPanel();
	TextBox textToSendToChat = new TextBox();
	Button btnSend = new Button("Send");

	VerticalPanel thirdVerticalRoomOptions = new VerticalPanel();
	Label lblAvailableRooms = new Label("Available Rooms");
	ListBox roomBox = new ListBox();
	Button btnJoinRoom = new Button("Join Room");

	Label lblStartYourOwn = new Label("Start Your Own");

	VerticalPanel underThirdVertHostOptions = new VerticalPanel();
	HorizontalPanel roomNameAndTextBoxHolder = new HorizontalPanel();
	Label lblRoomName = new Label("Room Name:");
	TextBox roomNameBox = new TextBox();
	HorizontalPanel roomThemeAndTextBoxHolder = new HorizontalPanel();
	Label lblTheme = new Label("Theme:");
	TextBox themeBox = new TextBox();
	Button btnHost = new Button("Host");

	public LobbyView() {
		initWidget(mainFlowPanel);
		System.out.println("Client: Trying to initialize the lobby view");
		initialize();
	}

	public void getInitialLobbyInformation() {
		rpcService.getInitialLobbyInformation(new AsyncCallback<LobbyInformation>() {

			@Override
			public void onFailure(Throwable caught) {
				if (DEBUG)
					System.out.println("Failed to get the initial lobby information");
			}

			@Override
			public void onSuccess(LobbyInformation result) {
				if (DEBUG)
					System.out.println("Got the initial lobby information");
				for (String user : result.users) {
					if (DEBUG)
						System.out.println("Client: Users: " + user);
					usersInLobby.add(user);
				}
				for (String r : result.rooms) {
					if (DEBUG)
						System.out.println("Client: Room: " + r);
					availableRooms.add(r);
				}
				for (String message : result.chatMessages) {
					chatWindowMessages.add(message);
					if (DEBUG)
						System.out.println("Client: Messages: " + message);
				}
				populateLobbyViewWithInformation();
			}
		});
	}

	/**
	 * This method will initialize the view after the lobby information has been
	 * retrieved
	 * 
	 */
	public void initialize() {
		setPanelOrder();
		setPanelCharacteristics();
		setRemoteEventListenersAndHandleEvents();
		populateLobbyViewWithInformation();
		setHandlers();
		getInitialLobbyInformation();
	}

	/**
	 * Sets up the panel framework for the lobby
	 * 
	 */
	public void setPanelOrder() {
		mainFlowPanel.add(horizontalInMainFlow);
		horizontalInMainFlow.add(firstVerticalUsersInLobby);
		lblUsersInLobby.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		firstVerticalUsersInLobby.add(lblUsersInLobby);
		lblUsersInLobby.setSize("100%", "100%");
		firstVerticalUsersInLobby.add(usersListBox);
		horizontalInMainFlow.add(secondVerticalLobbyChat);
		secondVerticalLobbyChat.add(lblHellousername);
		secondVerticalLobbyChat.add(chatTextArea);
		secondVerticalLobbyChat.add(textToSendBoxAndButtonHolder);
		textToSendBoxAndButtonHolder.add(textToSendToChat);
		textToSendBoxAndButtonHolder.add(btnSend);
		thirdVerticalRoomOptions.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalInMainFlow.add(thirdVerticalRoomOptions);
		thirdVerticalRoomOptions.add(lblAvailableRooms);
		thirdVerticalRoomOptions.add(roomBox);
		thirdVerticalRoomOptions.add(btnJoinRoom);
		thirdVerticalRoomOptions.add(lblStartYourOwn);
		underThirdVertHostOptions.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		thirdVerticalRoomOptions.add(underThirdVertHostOptions);
		underThirdVertHostOptions.add(roomNameAndTextBoxHolder);
		roomNameAndTextBoxHolder.add(lblRoomName);
		roomNameAndTextBoxHolder.add(roomNameBox);
		underThirdVertHostOptions.add(roomThemeAndTextBoxHolder);
		roomThemeAndTextBoxHolder.add(lblTheme);
		roomThemeAndTextBoxHolder.add(themeBox);
		underThirdVertHostOptions.add(btnHost);
	}

	/**
	 * Sets up how the panels will look
	 * 
	 */
	public void setPanelCharacteristics() {
		mainFlowPanel.setSize("955px", "427px");
		mainFlowPanel.setStyleName("LobbyPage");

		horizontalInMainFlow.setSize("100%", "100%");

		firstVerticalUsersInLobby.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		firstVerticalUsersInLobby.setSize("100%", "100%");

		usersListBox.setName("User List");
		usersListBox.setSize("95%", "346px");
		usersListBox.setVisibleItemCount(5);

		secondVerticalLobbyChat.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		secondVerticalLobbyChat.setSize("100%", "100%");

		lblHellousername.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblHellousername.setSize("100%", "100%");

		chatTextArea.setReadOnly(true);
		chatTextArea.setDirectionEstimator(true);
		chatTextArea.setSize("95%", "276px");
		chatTextArea.setText(totalChat);

		textToSendBoxAndButtonHolder.setSize("100%", "100%");

		textToSendToChat.setName("Send To Chat Field");
		textToSendToChat.setSize("95%", "22px");

		btnSend.setSize("100%", "30px");
		btnSend.setStyleName("gwt-LoginExistingButton");
		thirdVerticalRoomOptions.setSize("100%", "100%");

		lblAvailableRooms.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblAvailableRooms.setSize("100%", "44px");
		roomBox.setSize("100%", "153px");
		roomBox.setVisibleItemCount(5);

		btnJoinRoom.setSize("182px", "28px");
		btnJoinRoom.setStyleName("gwt-LoginExistingButton");

		lblStartYourOwn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblStartYourOwn.setSize("100%", "100%");
		underThirdVertHostOptions.setSize("100%", "100%");

		roomNameAndTextBoxHolder.setSize("100%", "100%");

		lblRoomName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lblRoomName.setSize("109px", "100%");

		roomNameBox.setWidth("100%");
		roomNameBox.setName("Room Name");

		roomThemeAndTextBoxHolder.setSize("100%", "34px");

		lblTheme.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lblTheme.setSize("109px", "100%");

		themeBox.setWidth("100%");
		themeBox.setName("Theme Box");

		btnHost.setStyleName("gwt-LoginExistingButton");
		btnHost.setText("Host Room\r\n");
		btnHost.setSize("182px", "28px");
	}

	/**
	 * Adds local event handlers
	 * 
	 */
	public void setHandlers() {
		textToSendToChat.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (!textToSendToChat.getText().equalsIgnoreCase("")) {
						rpcService.sendLobbyChatMessage(textToSendToChat.getText(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Void result) {
								System.out.println("Client: Sent Message: " + textToSendToChat.getText());
								textToSendToChat.setText("");
							}
						});
					}
				}
			}
		});

		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// send message and update the chat window
				if (!textToSendToChat.getText().equals("")) {
					rpcService.sendLobbyChatMessage(textToSendToChat.getText(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(Void result) {
							System.out.println("Client: Sent Message: " + textToSendToChat.getText());
						}
					});
					textToSendToChat.setText(""); // blank out the message bar
				}
			}
		});

		roomBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int roomSelectionIndex = roomBox.getSelectedIndex();
				roomSelection = roomBox.getValue(roomSelectionIndex);
			}
		});

		roomBox.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (roomSelection != null && roomSelection != "") {
					rpcService.joinRoom(roomSelection, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							if (DEBUG)
								System.out.println("Client: Error sending the join room information");
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Joined room " + roomNameBox.getSelectedText());
						}

					});
					if (DEBUG)
						System.out.println("Client: Fired a join room local event");
					eventBus.fireEvent(new JoinRoomLocalEvent());
				} else {
					if (DEBUG)
						System.out.println("Room selection was null, nothing happened");
				}

			}
		});

		btnJoinRoom.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// join the selected room
				if (roomSelection != null && roomSelection != "") {
					rpcService.joinRoom(roomSelection, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							if (DEBUG)
								System.out.println("Client: Error sending the join room information");
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Joined room " + roomNameBox.getSelectedText());
						}

					});
					if (DEBUG)
						System.out.println("Client: Fired a join room local event");
					eventBus.fireEvent(new JoinRoomLocalEvent());
				} else {
					if (DEBUG)
						System.out.println("Room selection was null, nothing happened");
				}
			}
		});

		btnHost.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Host game logic
				String roomName = roomNameBox.getText();
				String theme = themeBox.getText();
				if (!roomName.equalsIgnoreCase("") && !theme.equalsIgnoreCase("")) {
					rpcService.hostLobbyRoom(roomName, theme, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(Void result) {
							theRemoteEventService.removeListeners(DomainFactory.getDomain("Lobby"));
							if (DEBUG)
								System.out.println("Lobby listeners deactivated");
							// TODO:Clear the rootPanel
							String theme = themeBox.getText();
							String roomName = roomNameBox.getText();
							if (!theme.equalsIgnoreCase("") && !roomName.equalsIgnoreCase("")) {
								HostRoomLocalEvent hostRoomLocalEvent = new HostRoomLocalEvent();
								hostRoomLocalEvent.setTheme(theme);
								hostRoomLocalEvent.setRoomName(roomName);
								eventBus.fireEvent(hostRoomLocalEvent);
								if (DEBUG)
									System.out.println("Client: Fired host room event for room: " + roomName + ", with theme: " + theme);
							}
						}
					});
				}
			}
		});
	}

	/**
	 * Activates remote event listeners and handles displaying any new
	 * information that is passed in, via a remote event
	 * 
	 */
	public void setRemoteEventListenersAndHandleEvents() {
		// There is more logic in here than just setting listeners
		theRemoteEventService.addListener(DomainFactory.getDomain("Lobby"), new RemoteEventListener() {

			@Override
			public void apply(Event anEvent) {
				if (anEvent instanceof UpdateLobbyMessagesEvent) {
					UpdateLobbyMessagesEvent messageEvent = (UpdateLobbyMessagesEvent) anEvent;
					chatWindowMessages.add(messageEvent.message);
					System.out.println("Client: Recieved Message " + messageEvent.message);
					String totalChat = "";
					for (String s : chatWindowMessages) {
						totalChat += s + "\n";
					}
					chatTextArea.setText(totalChat);
					chatTextArea.setCursorPos(chatTextArea.getText().length());
				} else if (anEvent instanceof UpdateLobbyRoomsEvent) {
					UpdateLobbyRoomsEvent lobbyRoomEvent = (UpdateLobbyRoomsEvent) anEvent;
					if (DEBUG)
						System.out.println("Client: Got UpdateLobbyRoomsEvent, with room name: " + lobbyRoomEvent.roomName);
					availableRooms.add(lobbyRoomEvent.roomName);

					roomBox.clear();
					for (String r : availableRooms) {
						roomBox.addItem(r);
					}
				} else if (anEvent instanceof UpdateLobbyUsersEvent) {
					UpdateLobbyUsersEvent usersEvent = (UpdateLobbyUsersEvent) anEvent;
					usersListBox.addItem(usersEvent.getUsername());
				}
			}
		});
	}

	/**
	 * Populates the lobby with the information that has been acquired from the
	 * server
	 * 
	 */
	public void populateLobbyViewWithInformation() {
		System.out.println("Updating the initial lobby information");
		usersListBox.clear();
		for (String user : usersInLobby) { // populate user list for the first
			// time
			usersListBox.addItem(user);
			System.out.println("Added user to display user list: " + user);
		}
		totalChat = "";
		for (String s : chatWindowMessages) {
			totalChat += s + "\n";
		}
		chatTextArea.setText(totalChat);
		chatTextArea.setCursorPos(chatTextArea.getText().length());
		roomBox.clear();
		for (String r : availableRooms) {
			roomBox.addItem(r);
		}
		if (DEBUG)
			System.out.println("Lobby listeners activated");
	}

	public Widget asWidget() {
		return this;
	}

}
