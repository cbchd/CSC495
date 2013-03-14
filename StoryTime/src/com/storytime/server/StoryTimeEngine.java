package com.storytime.server;

import java.util.ArrayList;

import com.storytime.client.gameroom.GameData;

public class StoryTimeEngine {
    ArrayList<Room> currentLobbyRooms;
    ArrayList<User> currentOnlineUsers;
    ArrayList<User> legacyUsers;
    ArrayList<String> lobbyChat;
    ArrayList<User> usersInLobby;
    ArrayList<InGameRoom> inGameRooms;

    public StoryTimeEngine() {
	currentLobbyRooms = new ArrayList<Room>();
	currentOnlineUsers = new ArrayList<User>();
	legacyUsers = new ArrayList<User>();
	lobbyChat = new ArrayList<String>();
	usersInLobby = new ArrayList<User>();
	inGameRooms = new ArrayList<InGameRoom>();
    }
    
    public Boolean loginUser(String username, String password) {
	//Always returns true and adds the user to the current user list if he is not there
	User user = new User(username, password, this);
	if (!currentOnlineUsers.contains(user)) {
	    currentOnlineUsers.add(user);
	    usersInLobby.add(user);
	    return true;
	} else {
	    return true;
	}
    }

    public ArrayList<Room> getCurrentRooms() {
	return currentLobbyRooms;
    }

    public void setCurrentRooms(ArrayList<Room> currentRooms) {
	this.currentLobbyRooms = currentRooms;
    }

    public ArrayList<User> getCurrentOnlineUsers() {
	return currentOnlineUsers;
    }

    public void setCurrentOnlineUsers(ArrayList<User> currentOnlineUsers) {
	this.currentOnlineUsers = currentOnlineUsers;
    }

    public void hostRoom(User host, String roomName) {
	Room lobbyRoom = new Room(host, roomName);
	host.room = lobbyRoom;
	currentLobbyRooms.add(lobbyRoom);
    }

    public void joinRoom(User user, String roomName) {
	for (Room r : currentLobbyRooms) {
	    if (r.roomName.equalsIgnoreCase(roomName)) {
	    }
	}
    }

    public void setIngame(String roomName) {
	for (Room room : currentLobbyRooms) {
	    if (room.roomName.equalsIgnoreCase(roomName)) {
		room.inGame = true;
	    }
	}
    }

    public Room getUsersRoom(String username) {
	Room r = null;
	for (User user : currentOnlineUsers) {
	    if (username.equalsIgnoreCase(user.username)) {
		r = user.room;
	    }
	}
	return r;
    }
}
