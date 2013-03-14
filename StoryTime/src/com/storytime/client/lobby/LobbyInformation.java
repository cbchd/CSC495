package com.storytime.client.lobby;

import java.io.Serializable;
import java.util.ArrayList;

import com.storytime.server.Room;
import com.storytime.server.User;

public class LobbyInformation implements Serializable {
   public ArrayList<String> chatMessages;
   public ArrayList<String> users;
   public ArrayList<String> rooms;

    private static final long serialVersionUID = 1L;
    public LobbyInformation() {
	chatMessages = new ArrayList<String>();
	users = new ArrayList<String>();
	rooms = new ArrayList<String>();
    }
}
