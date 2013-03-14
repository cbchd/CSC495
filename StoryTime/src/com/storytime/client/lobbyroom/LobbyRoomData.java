package com.storytime.client.lobbyroom;

import java.io.Serializable;
import java.util.ArrayList;

import de.novanic.eventservice.client.event.domain.Domain;

public class LobbyRoomData implements Serializable{

    private static final long serialVersionUID = 1L;
    public String roomName; 
    public String theme;
    public int pointCap = 5;
    public int timer = 10;
    public boolean inGame;
    public ArrayList<String> users;
    public String message;
    public Domain domain;
    public ArrayList<String> messages;
 
    public LobbyRoomData() {
	users = new ArrayList<String>();
	messages = new ArrayList<String>();
    }
}
