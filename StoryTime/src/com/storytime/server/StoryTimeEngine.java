package com.storytime.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.storytime.client.spellrelated.Spell;

public class StoryTimeEngine {
	
	ArrayList<String> lobbyChat;
	HashMap<String, User> usersInLobby;
	HashMap<String, User> onlineUsers;
	HashMap<String, Room> lobbyRooms;
	HashMap<String, InGameRoom> gameRooms;

	public ArrayList<String> getLobbyChat() {
		return lobbyChat;
	}

	public void setLobbyChat(ArrayList<String> lobbyChat) {
		this.lobbyChat = lobbyChat;
	}

	public HashMap<String, User> getUsersInLobby() {
		return usersInLobby;
	}

	public void setUsersInLobby(HashMap<String, User> usersInLobby) {
		this.usersInLobby = usersInLobby;
	}

	public HashMap<String, InGameRoom> getGameRooms() {
		return gameRooms;
	}

	public void setGameRooms(HashMap<String, InGameRoom> gameRooms) {
		this.gameRooms = gameRooms;
	}

	public void setOnlineUsers(HashMap<String, User> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public void setLobbyRooms(HashMap<String, Room> lobbyRooms) {
		this.lobbyRooms = lobbyRooms;
	}

	public StoryTimeEngine() {
		lobbyChat = new ArrayList<String>();
		usersInLobby = new HashMap<String, User>();
		onlineUsers = new HashMap<String, User>();
		lobbyRooms = new HashMap<String, Room>();
		gameRooms = new HashMap<String, InGameRoom>();
	}

	public Boolean loginUser(String username, String password) {
		// Always returns true and adds the user to the current user list if he
		// is not there
		User user = new User(username, password, this);
		if (!onlineUsers.containsKey(username)) {
			onlineUsers.put(username, user);
			usersInLobby.put(username, user);
		}
		return true;

	}

	public HashMap<String, Room> getLobbyRooms() {
		return lobbyRooms;
	}

	public void setCurrentRooms(HashMap<String, Room> lobbyRooms) {
		this.lobbyRooms = lobbyRooms;
	}

	public HashMap<String, User> getOnlineUsers() {
		return onlineUsers;
	}

	public void setCurrentOnlineUsers(HashMap<String, User> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public void hostRoom(User host, String roomName, String theme, String password, int numberOfPlayers) {
		Room lobbyRoom = new Room(host, roomName);
		lobbyRoom.setTheme(theme);
		lobbyRoom.password = password;
		lobbyRoom.numberOfPlayers = numberOfPlayers;
		host.room = lobbyRoom;
		lobbyRooms.put(lobbyRoom.roomName, lobbyRoom);
	}

	public void leaveRoom(String userName, String roomName) {
		User user = this.usersInLobby.get(userName);
		lobbyRooms.remove(roomName);
		user.room = null;
	}

	public Boolean joinRoom(User user, String roomName) {
		usersInLobby.remove(user.username);
		if (lobbyRooms.containsKey(roomName)) {
			lobbyRooms.get(roomName).users.put(user.username, user);
			user.room = lobbyRooms.get(roomName);
			return true;
		} else {
			return false;
		}
	}

	public Room getUsersRoom(String username) {
		Room r = onlineUsers.get(username).room;
		return r;
	}

	public String chooseRandomPhrase(InGameRoom gameRoom) {
		ArrayList<String> phrases = new ArrayList<String>();
		for (User user : gameRoom.users) {
			if (!user.phrase.equalsIgnoreCase("")) {
				phrases.add(user.phrase);
			}
		}
		if (phrases.size() == 0) {
			phrases.add("");
		} else {
			Collections.shuffle(phrases);
		}
		return phrases.get(0);
	}

	public void refreshTimerElapsedValues(InGameRoom gameRoom) {
		for (User user : gameRoom.users) {
			user.timerElapsed = false;
		}
		gameRoom.choosersTimerElapsed = false;
		gameRoom.numberOfUsersWhoseTimersHaveElapsed = 0;
	}

	public void clearAllUsersSubmittedPhrases(String roomName) {
		InGameRoom gameRoom = gameRooms.get(roomName);
		for (User user : gameRoom.users) {
			user.phrase = "";
		}
	}

	public void setPassword(String roomName, User host, String password) {
		Room lobbyRoom = lobbyRooms.get(roomName);
		if (lobbyRoom.host.getUsername().equalsIgnoreCase(host.getUsername())) {
			lobbyRoom.setPassword(password);
		}
	}
	
	public void castSpell(String casterUsername, String targetUsername, Spell spell) {
		// TODO
	}
}
