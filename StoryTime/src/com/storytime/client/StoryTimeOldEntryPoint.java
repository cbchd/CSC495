package com.storytime.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.storytime.client.gameroom.GameInProgressRoom;
import com.storytime.client.lobby.Lobby;
import com.storytime.client.lobbyroom.LobbyRoom;
import com.storytime.client.login.LoginPage;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;

public class StoryTimeOldEntryPoint implements EntryPoint {
    public static RootPanel rootPanel;
    public static boolean DEBUG = true;
    public StoryTimeServiceAsync storyTimeService;
    public static RemoteEventService theRemoteEventService = RemoteEventServiceFactory
	    .getInstance().getRemoteEventService();

    public void onModuleLoad() {
	RootPanel rp = RootPanel.get();
	FlowPanel fp = new FlowPanel();
	fp.add(new Image("ws_Light_arch_1920x1200.jpg"));
	rp.add(fp);
	rootPanel = RootPanel.get("container");
	storyTimeService = StoryTimeService.Util.getInstance();
	Window.setTitle("StoryMode");
	rootPanel.setStyleName("center");
	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	//rootPanel.getElement().getStyle().setBackgroundImage("home.jpg");
	controller("Login");
    }

    static public void controller(String location) {
	rootPanel.clear();
	// TODO logic code for switching between states of the game
	if (location.equalsIgnoreCase("Lobby")) {
	    Lobby lobby = new Lobby();
	    lobby.onModuleLoad();
	} else if (location.equalsIgnoreCase("Login")) {
	    LoginPage loginPage = new LoginPage();
	    loginPage.onModuleLoad();
	} else if (location.equalsIgnoreCase("LobbyRoom")) {
	    LobbyRoom lobbyRoom = new LobbyRoom();
	    lobbyRoom.onModuleLoad();
	} else if (location.equalsIgnoreCase("GameInProgressRoom")) {
	    GameInProgressRoom gameRoom = new GameInProgressRoom();
	    gameRoom.initialize();
	}
    }
}
