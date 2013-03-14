package com.storytime.client.lobbyroom;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.storytime.client.StoryTime;
import com.storytime.client.StoryTimeService;
import com.storytime.client.StoryTimeServiceAsync;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class LobbyRoom {

    RootPanel rootPanel;
    boolean DEBUG = StoryTime.DEBUG;

    StoryTimeServiceAsync storyTimeService = StoryTimeService.Util
	    .getInstance();
    public static LobbyRoomData roomData;

    public LobbyRoom() {
	roomData = new LobbyRoomData();
    }

    public void onModuleLoad() {
	initializeLobbyRoom();
    }

    public void initializeLobbyRoom() {
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
			roomData.timer = result.timer;
			roomData.users = result.users;
			roomData.inGame = result.inGame;
			roomData.domain = result.domain;
			System.out.println("Client: Got Data: PointCap: "
				+ roomData.pointCap + ", Name: "
				+ roomData.roomName + ", Theme: "
				+ roomData.theme + ", " + "Timer: "
				+ roomData.timer + ", Users: "
				+ roomData.users.toString() + ", and Domain: "
				+ roomData.domain.getName());
			displayView();
		    }
		});
    }

    /**
     * @wbp.parser.entryPoint
     */
    public void displayView() {
	final RemoteEventService theRemoteEventService = StoryTime.theRemoteEventService;
	rootPanel = StoryTime.rootPanel;
	rootPanel.clear();
	if (DEBUG)
	    System.out.println("LobbyRoom!");
	// final RootPanel rootPanel = RootPanel.get();

	FlowPanel flowPanel_2 = new FlowPanel();
	rootPanel.add(flowPanel_2, 227, 52);
	flowPanel_2.setSize("441px", "286px");

	final TextArea chatWindow = new TextArea();
	chatWindow.setReadOnly(true);
	flowPanel_2.add(chatWindow);
	chatWindow.setSize("436px", "282px");

	Label lblChat = new Label("Chat");
	lblChat.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	rootPanel.add(lblChat, 227, 31);
	lblChat.setSize("30px", "18px");

	TabPanel tabPanel = new TabPanel();
	tabPanel.setAnimationEnabled(true);
	rootPanel.add(tabPanel, 5, 10);
	tabPanel.setSize("216px", "176px");

	HorizontalPanel horizontalPanel = new HorizontalPanel();
	tabPanel.add(horizontalPanel, "Game Settings", false);
	horizontalPanel.setSize("5cm", "3cm");

	Grid grid_1 = new Grid(2, 2);
	horizontalPanel.add(grid_1);
	grid_1.setSize("187px", "111px");

	Label lblPointLimit = new Label("Point Limit:");
	grid_1.setWidget(0, 0, lblPointLimit);

	final ValueListBox<Integer> pointLimitBox = new ValueListBox<Integer>(
		IntegerRenderer.instance());
	pointLimitBox.setValue(10);
	pointLimitBox.setValue(15);
	pointLimitBox.setValue(20);
	pointLimitBox.setValue(25);
	pointLimitBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<Integer> event) {
		// Get point Limit TODO
		storyTimeService.updateLobbyRoomPointCap(roomData.roomName,
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
	grid_1.setWidget(0, 1, pointLimitBox);

	Label lblTimePerRound = new Label("Time Per Round:");
	grid_1.setWidget(1, 0, lblTimePerRound);

	final ValueListBox<Integer> timePerRoundBox = new ValueListBox<Integer>(
		IntegerRenderer.instance());
	timePerRoundBox.setValue(roomData.timer);
	timePerRoundBox.setValue(15);
	timePerRoundBox.setValue(20);
	timePerRoundBox.setValue(25);
	timePerRoundBox.setValue(30);
	timePerRoundBox.setValue(35);
	timePerRoundBox.setValue(40);
	timePerRoundBox
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
						    .println("Client: Error occurred in sending server the new timer value: "
							    + event.getValue());
				    }

				    @Override
				    public void onSuccess(Void result) {
					if (DEBUG)
					    System.out
						    .println("Client: Got confirmation of timer change from server");
				    }

				});
		    }
		});
	grid_1.setWidget(1, 1, timePerRoundBox);

	Grid grid = new Grid(1, 1);
	tabPanel.add(grid, "My Profession", false);
	grid.setSize("5cm", "59px");

	ListBox comboBox = new ListBox();
	comboBox.addItem("Pyromancer");
	comboBox.addItem("Golemancer");
	comboBox.addItem("Necromancer");
	comboBox.addItem("Hydromancer");
	comboBox.addItem("Arcanist");
	grid.setWidget(0, 0, comboBox);
	comboBox.setWidth("182px");

	Label lblUsers = new Label("Users");
	lblUsers.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	rootPanel.add(lblUsers, 10, 192);
	lblUsers.setSize("150px", "18px");

	final TextBox chatTextBox = new TextBox();
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
	rootPanel.add(chatTextBox, 227, 359);
	chatTextBox.setSize("343px", "17px");

	Label theme = new Label("Theme:");
	theme.setText(roomData.theme);
	if (DEBUG)
	    System.out.println("Client: Set Theme Tag to: " + roomData.theme);
	theme.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	rootPanel.add(theme, 227, 10);
	theme.setSize("441px", "18px");

	Button btnLeaveRoom = new Button("Leave Room");
	btnLeaveRoom.setStyleName("gwt-LoginExistingButton");
	btnLeaveRoom.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		rootPanel.clear();
		storyTimeService.leaveRoom(roomData.roomName,
			new AsyncCallback<Void>() {

			    @Override
			    public void onFailure(Throwable caught) {
			    }

			    @Override
			    public void onSuccess(Void result) {
				if (DEBUG)
				    System.out
					    .println("Client: Successfully left the room: "
						    + roomData.roomName);
			    }

			});
		StoryTime.controller("Lobby");
	    }
	});
	rootPanel.add(btnLeaveRoom, 10, 415);

	Button btnStartGame = new Button("Start Game");
	btnStartGame.setStyleName("gwt-LoginExistingButton");
	btnStartGame.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		rootPanel.clear();
		storyTimeService.startGame(roomData.roomName, new AsyncCallback<Void>(){

		    @Override
		    public void onFailure(Throwable caught) {
			if (DEBUG) System.out.println("Client: Error in the start game call");
		    }

		    @Override
		    public void onSuccess(Void result) {
			if (DEBUG) System.out.println("Client: Confirmation of started game recieved");
		    }
		});
	    }
	});
	rootPanel.add(btnStartGame, 384, 415);
	btnStartGame.setSize("145px", "25px");

	ListBox userListBox = new ListBox();
	for (String users : roomData.users) {
	    userListBox.addItem(users);
	}
	rootPanel.add(userListBox, 5, 215);
	userListBox.setSize("150px", "169px");
	userListBox.setVisibleItemCount(5);

	Button btnNewButton = new Button("Send");
	btnNewButton.setStyleName("gwt-LoginExistingButton");
	btnNewButton.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		// Send text to users in room
		storyTimeService.sendRoomChatMessage(roomData.roomName,
			chatTextBox.getText(), new AsyncCallback<Void>() {

			    @Override
			    public void onFailure(Throwable caught) {
			    }

			    @Override
			    public void onSuccess(Void result) {
				if (DEBUG)
				    System.out.println("Client: Sent message: "
					    + chatTextBox.getText());
				chatTextBox.setText("");
			    }

			});
	    }
	});
	rootPanel.add(btnNewButton, 590, 359);
	btnNewButton.setSize("78px", "25px");

	theRemoteEventService.addListener(
		DomainFactory.getDomain(roomData.roomName),
		new RemoteEventListener() {

		    public void apply(Event anEvent) {
			if (anEvent instanceof UpdatePointCapEvent) {
			    // Update the point cap set for the room
			    UpdatePointCapEvent updatePointCapEvent = (UpdatePointCapEvent) anEvent;
			    pointLimitBox
				    .setValue(updatePointCapEvent.pointCap);
			    roomData.pointCap = updatePointCapEvent.pointCap;
			} else if (anEvent instanceof UpdateTimerEvent) {
			    // Update the timer
			    UpdateTimerEvent updateTimerEvent = (UpdateTimerEvent) anEvent;
			    timePerRoundBox.setValue(updateTimerEvent.timer);
			    roomData.timer = updateTimerEvent.timer;
			} else if (anEvent instanceof UserLeftRoomEvent) {
			    // Remove the user from the user list
			    UserLeftRoomEvent leftRoomEvent = (UserLeftRoomEvent) anEvent;
			    for (String username : roomData.users) {
				if (username
					.equalsIgnoreCase(leftRoomEvent.username)) {
				    roomData.users.remove(username);
				}
			    }

			} else if (anEvent instanceof UpdateRoomChatWindowEvent) {
			    UpdateRoomChatWindowEvent chatWindowEvent = (UpdateRoomChatWindowEvent) anEvent;
			    roomData.messages.add(chatWindowEvent.message);
			    if (DEBUG)
				System.out.println("Client: Got message: "
					+ chatWindowEvent.message
					+ ", from server");

			    String total = "";
			    for (String message : roomData.messages) {
				total += message + "\n";
			    }
			    chatWindow.setText(total);
			} else if (anEvent instanceof RoomDisbandedEvent) {
			    theRemoteEventService.removeListeners();
			    if (DEBUG)
				System.out
					.println("Client: Recieved Disband Room Event & Deactivated Listeners For Room: "
						+ roomData.roomName);
			    if (DEBUG)
				System.out.println("Client: Left Room: "
					+ roomData.roomName);
			    StoryTime.controller("Lobby");
			    
			} else if (anEvent instanceof GameStartEvent) {
			    theRemoteEventService.removeListeners();
			    StoryTime.controller("GameInProgressRoom");
			}
		    }

		});
	String total = "";
	for (String message : roomData.messages) {
	    total += message + "\n";
	}
	chatWindow.setText(total);
    }
}
