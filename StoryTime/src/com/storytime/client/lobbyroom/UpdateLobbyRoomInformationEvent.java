package com.storytime.client.lobbyroom;

import java.util.ArrayList;

import com.storytime.server.User;

import de.novanic.eventservice.client.event.Event;

public class UpdateLobbyRoomInformationEvent implements Event {

    private static final long serialVersionUID = 1L;
    public String roomName; 
    public String theme;
    public int pointCap = 5;
    public int timer = 10;
    public User host;
    public boolean inGame;
    public ArrayList<User> users;
    
    public UpdateLobbyRoomInformationEvent() {
    }

}
