package com.storytime.client.view;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.JoinRoomLocalEvent;
import com.storytime.client.joinroom.JoinRoom;
import com.storytime.client.joinroom.JoinableRoomsInformation;
import com.storytime.client.joinroom.LobbyRoomDisbandedEvent;
import com.storytime.client.joinroom.LobbyRoomHostedEvent;
import com.storytime.client.lobbyroom.UpdateAuthorsTimerEvent;
import com.storytime.client.lobbyroom.UpdateMastersTimerEvent;
import com.storytime.client.lobbyroom.UpdatePasswordEvent;
import com.storytime.client.lobbyroom.UpdatePointLimitEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class JoinRoomView extends Composite implements
		com.storytime.client.presenters.HostRoomPresenter.Display {

	boolean DEBUG = true;
	int row = 0;
	int column = 0;
	StoryTimeServiceAsync rpcService = StoryTimeEntryMVP.rpcService;
	RemoteEventService theRemoteEventService = StoryTimeEntryMVP.theRemoteEventService;
	HandlerManager eventBus = StoryTimeEntryMVP.eventBus;

	VerticalPanel verticalPanel = new VerticalPanel();
	CellTable<JoinRoom> currentRoomsTable = new CellTable<JoinRoom>();
	JoinableRoomsInformation joinableRoomsInformation = new JoinableRoomsInformation();
	Column<JoinRoom, String> pointLimitTextColumn;
	Column<JoinRoom, String> roomNameTextColumn;
	Column<JoinRoom, String> themeTextColumn;
	Column<JoinRoom, String> playersTextColumn;
	Column<JoinRoom, String> mastersTimeTextColumn;
	Column<JoinRoom, String> authorsTimeTextColumn;
	Column<JoinRoom, String> passwordTextColumn;
	Label lblCurrentRooms = new Label("Current Rooms");
	final PasswordPopupView popup = new PasswordPopupView();

	public JoinRoomView() {
		if (DEBUG)
			System.out.println("Client: Initializing the JoinRoomView");
		getJoinableRoomsInformation();
		initWidget(verticalPanel);
		verticalPanel.setSize("688px", "595px");
		verticalPanel.setStyleName("JoinRoomPage");
		lblCurrentRooms
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblCurrentRooms);
		currentRoomsTable.setStyleName("JoinRoomView");

		verticalPanel.add(currentRoomsTable);
		currentRoomsTable.setSize("100%", "100%");
		verticalPanel.setCellWidth(currentRoomsTable, "100%");
		verticalPanel.setCellHeight(currentRoomsTable, "100%");
		setHandlers();
		setUpColumns();
		setRemoteEventListenersAndHandleEvents();
		setPopupHandlers();
	}

	public void setUpColumns() {
		roomNameTextColumn = new Column<JoinRoom, String>(new TextCell()) {
			@Override
			public String getValue(JoinRoom joinRoom) {
				return joinRoom.roomName;
			}
		};
		currentRoomsTable.addColumn(roomNameTextColumn, "Room Name");

		themeTextColumn = new Column<JoinRoom, String>(new TextCell()) {
			@Override
			public String getValue(JoinRoom joinRoom) {
				return joinRoom.theme;
			}
		};
		currentRoomsTable.addColumn(themeTextColumn, "Theme");

		playersTextColumn = new Column<JoinRoom, String>(new TextCell()) {
			@Override
			public String getValue(JoinRoom joinRoom) {
				return joinRoom.numberOfPlayers + "";
			}
		};
		currentRoomsTable.addColumn(playersTextColumn, "Players");

		pointLimitTextColumn = new Column<JoinRoom, String>(new TextCell()) {
			@Override
			public String getValue(JoinRoom joinRoom) {
				return joinRoom.pointLimit + "";
			}
		};
		currentRoomsTable.addColumn(pointLimitTextColumn, "Point Limit");

		mastersTimeTextColumn = new Column<JoinRoom, String>(new TextCell()) {
			@Override
			public String getValue(JoinRoom joinRoom) {
				return joinRoom.mastersTime + "";
			}
		};
		currentRoomsTable.addColumn(mastersTimeTextColumn, "Master's Time");

		authorsTimeTextColumn = new Column<JoinRoom, String>(new TextCell()) {
			@Override
			public String getValue(JoinRoom joinRoom) {
				return joinRoom.authorsTime + "";
			}
		};
		currentRoomsTable.addColumn(authorsTimeTextColumn, "Author's Time");

		passwordTextColumn = new Column<JoinRoom, String>(new TextCell()) {

			@Override
			public String getValue(JoinRoom object) {
				if (!object.password.equalsIgnoreCase("")) {
					return "Yes";
				} else {
					return "No";
				}
			}

		};
		currentRoomsTable.addColumn(passwordTextColumn, "Password");
	}

	public void populateCellData() {
		currentRoomsTable.setRowData(joinableRoomsInformation.joinableRooms);
	}

	public void getJoinableRoomsInformation() {
		rpcService
				.getJoinableRoomsAndTheirInformation(new AsyncCallback<JoinableRoomsInformation>() {

					@Override
					public void onFailure(Throwable caught) {
						if (DEBUG)
							System.out
									.println("Client: Failed while asking the server for the current joinable lobby rooms information");
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(JoinableRoomsInformation result) {
						if (DEBUG)
							System.out
									.println("Client: Got the joinable lobby rooms information from the server");
						joinableRoomsInformation = result;
						populateCellData();
					}
				});
	}

	public void joinRoom(final String roomName) {

		rpcService.joinRoom(roomName, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (DEBUG)
					System.out
							.println("Client: Error sending the join room information");
			}

			@Override
			public void onSuccess(Void result) {
				theRemoteEventService.removeListeners(DomainFactory
						.getDomain("Lobby"));
				if (DEBUG)
					System.out.println("Client: Lobby listeners deactivated");
				if (DEBUG)
					System.out
							.println("Client: Got confirmation from the server that this client has joined the room: "
									+ roomName);
			}
		});
		if (DEBUG)
			System.out.println("Client: Fired a join room local event");
		eventBus.fireEvent(new JoinRoomLocalEvent());
	}

	public void setHandlers() {
		currentRoomsTable.addCellPreviewHandler(new Handler<JoinRoom>() {

			@Override
			public void onCellPreview(CellPreviewEvent<JoinRoom> event) {
				if (BrowserEvents.CLICK.equalsIgnoreCase(event.getNativeEvent()
						.getType())) {
					row = event.getIndex();
					column = event.getColumn();
				}
			}

		});
		currentRoomsTable.addDomHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (DEBUG)
					System.out
							.println("Client: Got double click event for row: "
									+ row + " and column: " + column);
				if (DEBUG)
					System.out.println("Client: Trying to join the room: "
							+ joinableRoomsInformation.joinableRooms.get(row).roomName);
				if (!joinableRoomsInformation.joinableRooms.get(row).password
						.equalsIgnoreCase("")) {
					popup.lblIncorrectPassword.setVisible(false);
					displayPasswordPopup();
					// joinRoom(joinableRoomsInformation.joinableRooms.get(row).roomName);
				} else {
					joinRoom(joinableRoomsInformation.joinableRooms.get(row).roomName);
				}
			}

		}, DoubleClickEvent.getType());
	}

	public void setRemoteEventListenersAndHandleEvents() {
		theRemoteEventService.addListener(DomainFactory.getDomain("Lobby"),
				new RemoteEventListener() {

					@Override
					public void apply(Event anEvent) {
						if (anEvent instanceof LobbyRoomHostedEvent) {
							LobbyRoomHostedEvent lobbyRoomHostedEvent = (LobbyRoomHostedEvent) anEvent;
							onLobbyRoomHosted(lobbyRoomHostedEvent);

						} else if (anEvent instanceof LobbyRoomDisbandedEvent) {
							LobbyRoomDisbandedEvent lobbyRoomDisbandedEvent = (LobbyRoomDisbandedEvent) anEvent;
							onLobbyRoomDisbanded(lobbyRoomDisbandedEvent);
							
						} else if (anEvent instanceof UpdatePointLimitEvent) {
							UpdatePointLimitEvent pointLimitChangeEvent = (UpdatePointLimitEvent) anEvent;
							onUpdatePointLimit(pointLimitChangeEvent);
							
						} else if (anEvent instanceof UpdateAuthorsTimerEvent) {
							UpdateAuthorsTimerEvent updateAuthorsTimerEvent = (UpdateAuthorsTimerEvent) anEvent;
							onUpdateAuthorsTimer(updateAuthorsTimerEvent);
							
						} else if (anEvent instanceof UpdateMastersTimerEvent) {
							UpdateMastersTimerEvent updateMastersTimerEvent = (UpdateMastersTimerEvent) anEvent;
							onUpdateMastersTimer(updateMastersTimerEvent);
						} else if (anEvent instanceof UpdatePasswordEvent) {
							UpdatePasswordEvent passwordEvent = new UpdatePasswordEvent();
							onRoomPasswordChanged(passwordEvent);
						}
					}
				});
	}

	public void displayPasswordPopup() {
		if (DEBUG)
			System.out.println("Client: Please enter the password for room: "
					+ joinableRoomsInformation.joinableRooms.get(row).roomName);

		popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				popup.setGlassEnabled(true);
				popup.textBox.setFocus(true);
				popup.textBox.setCursorPos(0);
				popup.center();
			}

		});
	}

	public void setPopupHandlers() {
		popup.textBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					popup.lblIncorrectPassword.setVisible(false);
					checkForCorrectPasswordEnteredInPopup();
				}
			}
		});
		popup.btnEnter.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				popup.lblIncorrectPassword.setVisible(false);
				checkForCorrectPasswordEnteredInPopup();
			}
		});
	}

	public void checkForCorrectPasswordEnteredInPopup() {
		JoinRoom roomTryingToJoin = joinableRoomsInformation.joinableRooms
				.get(row);
		if (popup.textBox.getText().equalsIgnoreCase(roomTryingToJoin.password)) {
			joinRoom(joinableRoomsInformation.joinableRooms.get(row).roomName);
		} else {
			if (DEBUG)
				System.out
						.println("Client: An incorrect password was entered for the room: "
								+ roomTryingToJoin.roomName);
			popup.lblIncorrectPassword.setVisible(true);
		}
	}

	public void onLobbyRoomHosted(LobbyRoomHostedEvent lobbyRoomEvent) {
		if (DEBUG)
			System.out
					.println("Client: Got LobbyRoomHostedEvent for room name: "
							+ lobbyRoomEvent.roomName);
		JoinRoom joinableRoom = new JoinRoom();
		joinableRoom.setRoomName(lobbyRoomEvent.getRoomName());
		joinableRoom.setTheme(lobbyRoomEvent.getTheme());
		joinableRoom.setPointLimit(lobbyRoomEvent.getPointLimit());
		joinableRoom.setNumberOfPlayers(lobbyRoomEvent.getNumberOfPlayers());
		joinableRoom.setMastersTime(lobbyRoomEvent.getMastersTime());
		joinableRoom.setAuthorsTime(lobbyRoomEvent.getAuthorsTime());
		joinableRoom.setPassword(lobbyRoomEvent.getPassword());
		joinableRoomsInformation.joinableRooms.add(joinableRoom);
		currentRoomsTable.setRowData(joinableRoomsInformation.joinableRooms);
	}

	public void onLobbyRoomDisbanded(
			LobbyRoomDisbandedEvent lobbyRoomDisbandedEvent) {
		if (DEBUG)
			System.out
					.println("Client: Got LobbyRoomDisbandedEvent for room name: "
							+ lobbyRoomDisbandedEvent.getRoomName());
		for (int x = 0; x < joinableRoomsInformation.joinableRooms.size(); x++) {
			if (joinableRoomsInformation.joinableRooms.get(x).roomName
					.equalsIgnoreCase(lobbyRoomDisbandedEvent.getRoomName())) {
				joinableRoomsInformation.joinableRooms.remove(x);
				break;
			}
		}
		currentRoomsTable.setRowData(joinableRoomsInformation.joinableRooms);
	}

	public void onUpdatePointLimit(UpdatePointLimitEvent pointLimitChangeEvent) {
		if (DEBUG)
			System.out
					.println("Client: Received a LobbyRoomPointLimitChangeEvent for room: "
							+ pointLimitChangeEvent
									.getRoomName());
		for (JoinRoom room : joinableRoomsInformation.joinableRooms) {
			if (room.getRoomName().equalsIgnoreCase(
					pointLimitChangeEvent.getRoomName())) {
				room.setPointLimit(pointLimitChangeEvent
						.getPointLimit());
			}
		}
		currentRoomsTable
				.setRowData(joinableRoomsInformation.joinableRooms);
	}
	
	public void onUpdateAuthorsTimer(UpdateAuthorsTimerEvent updateAuthorsTimerEvent) {
		String roomName = updateAuthorsTimerEvent
				.getRoomName();
		if (DEBUG)
			System.out
					.println("Client: Received an UpdatesubmissionTimerEvent for room: "
							+ roomName);
		for (JoinRoom room : joinableRoomsInformation.joinableRooms) {
			if (room.getRoomName().equalsIgnoreCase(
					roomName)) {
				room.setAuthorsTime(updateAuthorsTimerEvent
						.getAuthorsTimer());
			}
		}
		currentRoomsTable
				.setRowData(joinableRoomsInformation.joinableRooms);
	}
	
	public void onUpdateMastersTimer(UpdateMastersTimerEvent updateMastersTimerEvent) {
		String roomName = updateMastersTimerEvent
				.getRoomName();
		if (DEBUG)
			System.out
					.println("Client: Received an UpdateMastersTimerEvent for room: "
							+ roomName);
		for (JoinRoom room : joinableRoomsInformation.joinableRooms) {
			if (room.getRoomName().equalsIgnoreCase(
					roomName)) {
				room.setMastersTime(updateMastersTimerEvent
						.getMastersTime());
			}
		}
		currentRoomsTable
				.setRowData(joinableRoomsInformation.joinableRooms);
	}

	public void onRoomPasswordChanged(UpdatePasswordEvent passwordEvent) {
		String roomName = passwordEvent.getRoomName();
		String password = passwordEvent.getNewPassword();
		for (JoinRoom joinRoom : joinableRoomsInformation.joinableRooms) {
			if (joinRoom.getRoomName().equalsIgnoreCase(roomName)) {
				joinRoom.setPassword(password);
				if (DEBUG) System.out.println("Client: Successfuly set the password on the JoinRoomView for room: " + roomName  + " to be: " + password);
				currentRoomsTable
				.setRowData(joinableRoomsInformation.joinableRooms);
				break;
			}
		}
	}
}