package com.storytime.server;

import java.util.ArrayList;

public class Room {
    String roomName; 
    String theme;
    int pointCap = 5;
    int timer = 10;
    User host;
    boolean inGame;
    ArrayList<User> users;
    ArrayList<String> roomChat;
    
    public Room(User host, String roomName) {
        this.roomName = roomName;
        this.host = host;
        this.inGame = false;
        this.users = new ArrayList<User>();
        this.roomChat = new ArrayList<String>();
        this.users.add(host);
    }
}
