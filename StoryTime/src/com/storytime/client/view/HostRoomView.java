package com.storytime.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.HostRoomLocalEvent;

import de.novanic.eventservice.client.event.domain.DomainFactory;

public class HostRoomView extends Composite implements com.storytime.client.presenters.HostRoomPresenter.Display {

	boolean DEBUG = true;
	StoryTimeServiceAsync rpcService = StoryTimeEntryMVP.rpcService;
	HandlerManager eventBus = StoryTimeEntryMVP.eventBus;
	VerticalPanel verticalPanel = new VerticalPanel();
	Grid grid = new Grid(4, 2);
	Label lblRoomName = new Label("Room Name:");
	TextBox roomNameTextBox = new TextBox();
	Label lblTheme = new Label("Theme:");
	TextBox themeTextBox = new TextBox();
	Label lblPassword = new Label("Password:");
	TextBox passwordTextBox = new TextBox();
	Label lblPlayers = new Label("Players:");
	ValueListBox<Integer> numberOfPlayersValueListBox = new ValueListBox<Integer>(IntegerRenderer.instance());
	VerticalPanel verticalPanel_1 = new VerticalPanel();
	Button btnHost = new Button("Host");

	public HostRoomView() {
		if (DEBUG)
			System.out.println("Client: Initializing the host room view");
		initWidget(verticalPanel);
		setCharacteristics();
		setPanelOrder();
		setHandlers();
	}

	public void setCharacteristics() {
		verticalPanel.setStyleName("HostRoomPage");
		verticalPanel.setBorderWidth(5);
		verticalPanel.setSize("772px", "565px");

		grid.setCellPadding(5);
		grid.setBorderWidth(2);

		grid.setSize("100%", "513px");

		lblRoomName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		roomNameTextBox.setWidth("90%");

		lblTheme.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		themeTextBox.setWidth("90%");

		lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		passwordTextBox.setWidth("90%");

		lblPlayers.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		numberOfPlayersValueListBox.setWidth("90%");
		setNumberOfPlayers(12);

		verticalPanel_1.setSpacing(10);
		verticalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		verticalPanel_1.setSize("100%", "53px");

		btnHost.setStyleName("gwt-LoginExistingButton");

		btnHost.setSize("45%", "31px");
	}

	public void setPanelOrder() {
		verticalPanel.add(grid);
		grid.setWidget(0, 0, lblRoomName);
		grid.setWidget(0, 1, roomNameTextBox);
		grid.setWidget(1, 0, lblTheme);
		grid.setWidget(1, 1, themeTextBox);
		grid.setWidget(2, 0, lblPassword);
		grid.setWidget(2, 1, passwordTextBox);
		grid.setWidget(3, 0, lblPlayers);
		grid.setWidget(3, 1, numberOfPlayersValueListBox);
		verticalPanel.add(verticalPanel_1);
		verticalPanel_1.add(btnHost);
	}

	public void setHandlers() {
		btnHost.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (DEBUG) System.out.println("Client: The host room button has been clicked");
				// Host game logic
				final String roomName = roomNameTextBox.getText();
				final String theme = themeTextBox.getText();
				final String password = passwordTextBox.getText();
				final int numberOfPlayers = numberOfPlayersValueListBox.getValue();

				if (!roomName.equalsIgnoreCase("") && !theme.equalsIgnoreCase("")) {
					rpcService.hostLobbyRoom(roomName, theme, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							if (DEBUG)
								System.out.println("Client: Failed to host the room: " + roomName);
						}

						@Override
						public void onSuccess(Void result) {
							// theRemoteEventService.removeListeners(DomainFactory.getDomain("Lobby"));
							// TODO!
							if (DEBUG)
								System.out.println("Client: Lobby listeners deactivated");
							String theme = themeTextBox.getText();
							String roomName = roomNameTextBox.getText();
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

	private void setNumberOfPlayers(int numberOfPlayers) {
		for (int x = 2; x <= numberOfPlayers; x++) {
			numberOfPlayersValueListBox.setValue(x);
		}
		numberOfPlayersValueListBox.setValue(2);
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
