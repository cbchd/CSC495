package com.storytime.client.lobby;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.storytime.client.StoryTimeOldEntryPoint;
import com.storytime.client.StoryTimeService;
import com.storytime.client.StoryTimeServiceAsync;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class Lobby {

    RootPanel rootPanel;
    boolean DEBUG;
    static ArrayList<String> usersInLobby;
    static ArrayList<String> availableRooms;
    static ArrayList<String> chatWindowMessages;
    String roomSelection = null;
    StoryTimeServiceAsync storyTimeService = StoryTimeService.Util
	    .getInstance();

    public Lobby() {
	usersInLobby = new ArrayList<String>();
	availableRooms = new ArrayList<String>();
	chatWindowMessages = new ArrayList<String>();
	rootPanel = StoryTimeOldEntryPoint.rootPanel;
	DEBUG = true;
    }

    public void onModuleLoad() {
	// initialize the listeners and all of the data pertaining to the state
	// of the lobby
	storyTimeService
		.getInitialLobbyInformation(new AsyncCallback<LobbyInformation>() {

		    @Override
		    public void onFailure(Throwable caught) {
			if (DEBUG)
			    System.out
				    .println("Failed to get the initial lobby information");

		    }

		    @Override
		    public void onSuccess(LobbyInformation result) {
			if (DEBUG)
			    System.out.println("Got the intial lobby state");
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
				System.out.println("Client: Messages: "
					+ message);
			}
			displayView();
		    }

		});
	System.out.println("Added listener to the lobby messages domain");
    }

    /**
     * @wbp.parser.entryPoint
     */
    public void displayView() {
	rootPanel.clear();
	 //rootPanel = RootPanel.get();
	rootPanel = StoryTimeOldEntryPoint.rootPanel;
	final RemoteEventService theRemoteEventService = StoryTimeOldEntryPoint.theRemoteEventService;

	System.out.println("Lobby!");
	Label lblHellousername = new Label("Lobby");
	lblHellousername
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	rootPanel.add(lblHellousername, 248, 10);
	lblHellousername.setSize("59px", "18px");

	Label lblAvailableRooms = new Label("Available Rooms:");
	lblAvailableRooms.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	rootPanel.add(lblAvailableRooms, 477, 32);
	lblAvailableRooms.setSize("182px", "17px");

	final ListBox roomBox = new ListBox();
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
		int roomSelectionIndex = roomBox.getSelectedIndex();
		roomSelection = roomBox.getValue(roomSelectionIndex);
		// Fill in here what happens when someone tries to enter a room
		// by double clicking on a cell in the column
	    }
	});

	rootPanel.add(roomBox, 477, 56);
	roomBox.setSize("182px", "153px");
	roomBox.setVisibleItemCount(5);

	Label lblStartYourOwn = new Label("Start Your Own!");
	lblStartYourOwn
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	rootPanel.add(lblStartYourOwn, 504, 263);
	lblStartYourOwn.setSize("155px", "18px");

	final TextBox roomNameBox = new TextBox();
	roomNameBox.setName("Room Name");
	rootPanel.add(roomNameBox, 504, 287);

	final Label lblRoomName = new Label("Room Name:");
	rootPanel.add(lblRoomName, 400, 285);

	Button btnJoinRoom = new Button("Join Room");
	btnJoinRoom.setStyleName("gwt-LoginExistingButton");
	btnJoinRoom.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		// join the selected room
		if (roomSelection != null) {
		    storyTimeService.joinRoom(roomSelection,
			    new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
				    if (DEBUG)
					System.out
						.println("Client: Error sending the join room information");
				}

				@Override
				public void onSuccess(Void result) {
				    if (DEBUG)
					System.out.println("Client: Joined room "
						+ roomNameBox.getSelectedText());
				}

			    });
		    StoryTimeOldEntryPoint.controller("LobbyRoom");
		} else {
		    if (DEBUG)
			System.out
				.println("Room selection was null, nothing happened");
		}
	    }
	});
	rootPanel.add(btnJoinRoom, 477, 215);
	btnJoinRoom.setSize("182px", "28px");

	final ListBox usersListBox = new ListBox();
	usersListBox.setName("User List");
	for (String user : usersInLobby) { // populate user list for the first
					   // time
	    usersListBox.addItem(user);
	    System.out.println("Added user to display user list: " + user);
	}
	rootPanel.add(usersListBox, 10, 56);
	usersListBox.setSize("122px", "277px");
	usersListBox.setVisibleItemCount(5);

	Label lblUsersInLobby = new Label("Users in Lobby:");
	rootPanel.add(lblUsersInLobby, 10, 32);

	final TextBox themeBox = new TextBox();
	themeBox.setName("Theme Box");
	rootPanel.add(themeBox, 504, 321);

	final Label lblTheme = new Label("Theme:");
	rootPanel.add(lblTheme, 400, 321);
	lblTheme.setSize("74px", "18px");

	final TextArea chatTextArea = new TextArea();
	chatTextArea.setReadOnly(true);
	chatTextArea.setDirectionEstimator(true);
	rootPanel.add(chatTextArea, 138, 56);
	chatTextArea.setSize("245px", "271px");

	final TextBox textToSendToChat = new TextBox();
	textToSendToChat.addKeyDownHandler(new KeyDownHandler() {
	    public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    if (!textToSendToChat.getText().equalsIgnoreCase("")) {
			storyTimeService.sendLobbyChatMessage(
				textToSendToChat.getText(),
				new AsyncCallback<Void>() {

				    @Override
				    public void onFailure(Throwable caught) {
				    }

				    @Override
				    public void onSuccess(Void result) {
					System.out
						.println("Client: Sent Message: "
							+ textToSendToChat
								.getText());
					textToSendToChat.setText("");
				    }
				});
		    }
		}
	    }
	});
	textToSendToChat.setName("Send To Chat Field");
	rootPanel.add(textToSendToChat, 138, 339);
	textToSendToChat.setSize("186px", "22px");

	Button btnSend = new Button("Send");
	btnSend.setStyleName("gwt-LoginExistingButton");
	btnSend.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		// send message and update the chat window
		storyTimeService.sendLobbyChatMessage(
			textToSendToChat.getText(), new AsyncCallback<Void>() {

			    @Override
			    public void onFailure(Throwable caught) {
			    }

			    @Override
			    public void onSuccess(Void result) {
				System.out.println("Client: Sent Message: "
					+ textToSendToChat.getText());
			    }
			});
		textToSendToChat.setText(""); // blank out the message bar
	    }
	});
	rootPanel.add(btnSend, 338, 339);
	btnSend.setSize("53px", "30px");

	Button btnHost = new Button("Host");
	btnHost.setStyleName("gwt-LoginExistingButton");
//	btnHost.addClickHandler(new ClickHandler() {
//	    @Override
//	    public void onClick(ClickEvent event) {
//		// Host game logic
//		String roomName = roomNameBox.getText();
//		String theme = themeBox.getText();
//		if (!roomName.equalsIgnoreCase("")
//			&& !theme.equalsIgnoreCase("")) {
//		    storyTimeService.hostLobbyRoom(roomName, theme,
//			    new AsyncCallback<Void>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//				}
//
//				@Override
//				public void onSuccess(Void result) {
//				    theRemoteEventService
//					    .removeListeners(DomainFactory
//						    .getDomain("Lobby"));
//				    if (DEBUG)
//					System.out
//						.println("Lobby listeners deactivated");
//				    rootPanel.clear();
//				    StoryTimeOldEntryPoint.controller("LobbyRoom");
//				}
//			    });
//		}
//	    }
//	});
//	btnHost.setText("Host Room\r\n");
//	rootPanel.add(btnHost, 477, 360);
//	btnHost.setSize("182px", "28px");

	// add a listener
	theRemoteEventService.addListener(DomainFactory.getDomain("Lobby"),
		new RemoteEventListener() {

		    @Override
		    public void apply(Event anEvent) {
			if (anEvent instanceof UpdateLobbyMessagesEvent) {
			    UpdateLobbyMessagesEvent messageEvent = (UpdateLobbyMessagesEvent) anEvent;
			    Lobby.chatWindowMessages.add(messageEvent.message);
			    System.out.println("Client: Recieved Message "
				    + messageEvent.message);
			    String totalChat = "";
			    for (String s : chatWindowMessages) {
				totalChat += s + "\n";
			    }
			    chatTextArea.setText(totalChat);
			} else if (anEvent instanceof UpdateLobbyRoomsEvent) {
			    UpdateLobbyRoomsEvent lobbyRoomEvent = (UpdateLobbyRoomsEvent) anEvent;
			    if (DEBUG)
				System.out
					.println("Client: Got UpdateLobbyRoomsEvent, with room name: "
						+ lobbyRoomEvent.roomName);
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

	String totalChat = "";
	for (String s : chatWindowMessages) {
	    totalChat += s + "\n";
	}
	for (String r : availableRooms) {
	    roomBox.addItem(r);
	}
	chatTextArea.setText(totalChat);
	if (DEBUG)
	    System.out.println("Lobby listeners activated");
    }
}
