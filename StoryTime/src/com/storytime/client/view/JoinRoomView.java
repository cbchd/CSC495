package com.storytime.client.view;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.JoinRoomLocalEvent;
import com.storytime.client.joinroom.JoinRoom;
import com.storytime.client.joinroom.JoinableRoomsInformation;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.DomainFactory;

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

	public JoinRoomView() {
		if (DEBUG)
			System.out.println("Client: Initializing the JoinRoomView");
		// RootPanel rp = RootPanel.get();
		getJoinableRoomsInformation();
		// rp.add(verticalPanel, 10, 10);
		initWidget(verticalPanel);
		verticalPanel.setSize("688px", "595px");
		//verticalPanel.setStyleName("JoinRoomPage");
		Label lblCurrentRooms = new Label("Current Rooms");
		lblCurrentRooms
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblCurrentRooms);
		currentRoomsTable.setStyleName("JoinRoomView");

		verticalPanel.add(currentRoomsTable);
		currentRoomsTable.setSize("100%", "100%");
		verticalPanel.setCellWidth(currentRoomsTable, "100%");
		verticalPanel.setCellHeight(currentRoomsTable, "100%");
		setUpColumns();
		setHandlers();
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
				joinRoom(joinableRoomsInformation.joinableRooms.get(row).roomName);

			}

		}, DoubleClickEvent.getType());
	}
}
