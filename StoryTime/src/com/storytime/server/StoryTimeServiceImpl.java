package com.storytime.server;

import java.util.Collections;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.storytime.client.StoryTimeService;
import com.storytime.client.gameroom.AllPhrasesSubmittedEvent;
import com.storytime.client.gameroom.GameData;
import com.storytime.client.gameroom.GameEndEvent;
import com.storytime.client.gameroom.PhraseChosenEvent;
import com.storytime.client.gameroom.PhraseSubmittedEvent;
import com.storytime.client.gameroom.RoundCloseEvent;
import com.storytime.client.gameroom.RoundStartEvent;
import com.storytime.client.lobby.LobbyInformation;
import com.storytime.client.lobby.UpdateLobbyEvent;
import com.storytime.client.lobby.UpdateLobbyMessagesEvent;
import com.storytime.client.lobby.UpdateLobbyRoomsEvent;
import com.storytime.client.lobby.UpdateLobbyUsersEvent;
import com.storytime.client.lobbyroom.GameStartEvent;
import com.storytime.client.lobbyroom.LobbyRoomData;
import com.storytime.client.lobbyroom.RoomDisbandedEvent;
import com.storytime.client.lobbyroom.UpdatePointCapEvent;
import com.storytime.client.lobbyroom.UpdateRoomChatWindowEvent;
import com.storytime.client.lobbyroom.UpdateTimerEvent;
import com.storytime.client.lobbyroom.UserLeftRoomEvent;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

public class StoryTimeServiceImpl extends RemoteEventServiceServlet implements
	StoryTimeService {

    private static final long serialVersionUID = 1L;
    StoryTimeEngine engine;
    boolean DEBUG = true;

    public StoryTimeServiceImpl() {
	engine = new StoryTimeEngine();
    }

    public void startServer() {
	// TODO Auto-generated method stub
	System.out.println("Started server");
	for (int x = 0; x < 1000; x++) {
	    addEvent(UpdateLobbyEvent.domain, new UpdateLobbyEvent());
	    System.out.println("Adding event " + x);
	}
    }

    public Boolean loginUser(String username, String password) {
	System.out.println("Server: Logging user into the game");

	if (engine.loginUser(username, password)) {
	    HttpServletRequest request = this.getThreadLocalRequest();
	    HttpSession session = request.getSession();
	    session.setAttribute("User", new User(username, password, null));
	    UpdateLobbyUsersEvent usersEvent = new UpdateLobbyUsersEvent();
	    usersEvent.setUsername(username);
	    addEvent(usersEvent.getDomain(), usersEvent);
	    if (DEBUG)
		System.out
			.println("Server: Fired Update Lobby Users Event for user: "
				+ username);
	    return true;
	} else {
	    return false;
	}
    }

    public LobbyInformation getInitialLobbyInformation() {
	LobbyInformation lobbyInfo = new LobbyInformation();
	lobbyInfo.chatMessages = engine.lobbyChat;
	for (Room r : engine.currentLobbyRooms) {
	    lobbyInfo.rooms.add(r.roomName);
	}
	for (User user : engine.usersInLobby) {
	    lobbyInfo.users.add(user.username);
	}
	return lobbyInfo;
    }

    public void sendLobbyChatMessage(String message) {
	UpdateLobbyMessagesEvent messageEvent = new UpdateLobbyMessagesEvent();
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("User");
	String userMessage = user.username + ": " + message;
	engine.lobbyChat.add(userMessage);
	messageEvent.message = userMessage;
	addEvent(UpdateLobbyMessagesEvent.domain, messageEvent);
	if (DEBUG)
	    System.out.println("Server: Message Event Sent");
    }

    public void hostLobbyRoom(String roomName, String theme) {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("User");
	boolean foundUser = false;
	for (User usr : engine.currentOnlineUsers) {
	    if (user.username.equalsIgnoreCase(usr.username)) {
		user = usr;
		foundUser = true;
	    }
	}
	if (!foundUser)
	    System.out.println("Server: Couldn't find user: " + user.username
		    + " in the lobby list. (host room method)");
	engine.hostRoom(user, roomName);
	for (Room r : engine.getCurrentRooms()) {
	    if (r.roomName.equalsIgnoreCase(roomName)) {
		r.theme = theme;
	    }
	}
	UpdateLobbyRoomsEvent roomsEvent = new UpdateLobbyRoomsEvent();
	roomsEvent.roomName = roomName;
	addEvent(UpdateLobbyRoomsEvent.domain, roomsEvent);
	if (DEBUG)
	    System.out.println("Server: Hosted room: " + roomName
		    + ", with a theme of: " + theme
		    + ", and fired a UpdateLobbyRoomsEvent");
    }

    public void joinRoom(String roomName) {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("User");
	User foundUser = null;
	for (User usr : engine.currentOnlineUsers) {
	    if (usr.username.equalsIgnoreCase(user.username)) {
		if (DEBUG)
		    System.out.println("Server: Found user: " + usr.username
			    + ". Trying to add him to the room: " + roomName);
		foundUser = usr;
		for (Room r : engine.currentLobbyRooms) {
		    if (r.roomName.equalsIgnoreCase(roomName)) {
			r.users.add(usr);
			usr.room = r;
			if (DEBUG)
			    System.out.println("Server: Added user: "
				    + usr.username + " to the room: "
				    + r.roomName);
			break;
		    }
		}
	    }
	}
	engine.usersInLobby.remove(foundUser);
    }

    public LobbyRoomData getLobbyRoomInformation() {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("User");
	boolean foundUser = false;
	for (User userReal : engine.currentOnlineUsers) {
	    if (user.username.equalsIgnoreCase(userReal.username)) {
		user = userReal;
		foundUser = true;
		if (DEBUG)
		    System.out
			    .println("Server: Found user "
				    + user.username
				    + " in the currentOnlineUsers list (getLobbyRoomInfo)");
	    }
	}
	if (!foundUser)
	    System.out
		    .println("Server: User could not get the lobby room information because the user is not logged in");
	Room room = engine.getUsersRoom(user.username);
	if (room == null) {
	    if (DEBUG)
		System.out
			.println("Server: User : "
				+ user.username
				+ " has no room... so the client shouldn't be calling getlobbyroominformation");
	} else {
	    if (DEBUG)
		System.out.println("Server: Got " + user.username + "'s room: "
			+ room.roomName);
	}
	LobbyRoomData roomInfo = new LobbyRoomData();
	roomInfo.pointCap = room.pointCap;
	roomInfo.theme = room.theme;
	roomInfo.timer = room.timer;
	roomInfo.roomName = room.roomName;
	if (DEBUG)
	    System.out
		    .println("Server: Adding users to the room info to be sent to user "
			    + user.username);
	for (User usr : room.users) {
	    // Add the relevant users to the room info to be sent
	    roomInfo.users.add(usr.username);
	    if (DEBUG)
		System.out.println("Server: Added user: " + usr.username
			+ " to the roomInfo list");
	}
	roomInfo.inGame = room.inGame;
	// set a new domain
	roomInfo.domain = DomainFactory.getDomain(room.roomName);
	return roomInfo;
    }

    public void updateLobbyRoomPointCap(String roomName, int pointCap) {
	for (Room r : engine.currentLobbyRooms) {
	    if (r.roomName.equalsIgnoreCase(roomName)) {
		r.pointCap = pointCap;
		if (DEBUG)
		    System.out.println("Server: Set " + r.roomName
			    + "'s pointCap to " + r.pointCap);
		UpdatePointCapEvent pointChangeEvent = new UpdatePointCapEvent();
		pointChangeEvent.pointCap = r.pointCap;
		addEvent(DomainFactory.getDomain(roomName), pointChangeEvent);
		if (DEBUG)
		    System.out.println("Server: Fired Point Change Event");
		return;
	    }
	}
    }

    public void updateLobbyRoomTimer(String roomName, int timer) {
	if (DEBUG)
	    System.out.println("Server: Got UpdateTimerEvent for room: "
		    + roomName + " and updated timer value: " + timer);
	for (Room r : engine.currentLobbyRooms) {
	    if (r.roomName.equalsIgnoreCase(roomName)) {
		r.timer = timer;
		UpdateTimerEvent timerEvent = new UpdateTimerEvent();
		timerEvent.timer = timer;
		addEvent(DomainFactory.getDomain(roomName), timerEvent);
		if (DEBUG)
		    System.out
			    .println("Server: Fired UpdateTimerEvent for domain "
				    + DomainFactory.getDomain(roomName)
					    .getName());
		return;
	    }
	}
    }

    public void leaveRoom(String roomName) {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User usr = (User) session.getAttribute("User");
	Room usersRoom = null;
	User foundUser = null;
	for (Room r : engine.currentLobbyRooms) {
	    if (r.roomName.equalsIgnoreCase(roomName)) {
		usersRoom = r;
		for (User user : r.users) {
		    if (user.username.equalsIgnoreCase(usr.username)) {
			foundUser = user;
			break;
		    }
		}
	    }
	}
	if (usersRoom.host.getUsername().equalsIgnoreCase(foundUser.username)) {
	    // Disband room
	    RoomDisbandedEvent roomDisbandedEvent = new RoomDisbandedEvent();
	    addEvent(DomainFactory.getDomain(roomName), roomDisbandedEvent);
	    // Remove room from room list
	    engine.currentLobbyRooms.remove(usersRoom);

	    if (DEBUG)
		System.out.println("Server: User " + usr.username
			+ " was the host of " + roomName
			+ ", so the room was disbanded");
	    return;
	} else {
	    UserLeftRoomEvent userLeftEvent = new UserLeftRoomEvent();
	    userLeftEvent.username = foundUser.username;
	    addEvent(DomainFactory.getDomain(roomName), userLeftEvent);
	    // Remove user from the room
	    usersRoom.users.remove(foundUser);
	    if (DEBUG)
		System.out.println("Server: Fired user left event for domain "
			+ roomName);
	    return;
	}
    }

    public void sendRoomChatMessage(String roomName, String message) {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User usr = (User) session.getAttribute("User");

	for (Room r : engine.currentLobbyRooms) {
	    if (r.roomName.equalsIgnoreCase(roomName)) {
		r.roomChat.add(usr.username + ": " + message);
	    }
	}
	UpdateRoomChatWindowEvent chatWindowEvent = new UpdateRoomChatWindowEvent();
	chatWindowEvent.message = usr.username + ": " + message;
	if (DEBUG)
	    System.out.println("Server: Got message: " + message);
	addEvent(DomainFactory.getDomain(roomName), chatWindowEvent);
	if (DEBUG)
	    System.out.println("Sever: Fired UpdateRoonChatWindowEvent");
    }

    public void startGame(String roomName) {
	// Find the room
	Room room = null;
	for (Room r : engine.currentLobbyRooms) {
	    r.inGame = true;
	    room = r;
	}

	if (DEBUG)
	    System.out.println("Server: Trying to start game: Removed room: "
		    + room.roomName);
	// Add the room's data to a new InGameRoom object
	InGameRoom gameRoom = new InGameRoom();
	gameRoom.domain = DomainFactory.getDomain(room.roomName);
	gameRoom.pointCap = room.pointCap;
	gameRoom.theme = room.theme;
	gameRoom.timer = room.timer;
	gameRoom.users = room.users;

	// Set the turn order randomly
	Collections.shuffle(gameRoom.users);

	// initialize scores to 0
	for (User user : gameRoom.users) {
	    gameRoom.scoreList.put(user, 0);
	}
	// Create an ingame room
	engine.inGameRooms.add(gameRoom);
	engine.currentLobbyRooms.remove(room);
	if (DEBUG)
	    System.out.println("Server: Started game with attributes: Domain: "
		    + gameRoom.domain + ", pointCap: " + gameRoom.pointCap
		    + ", theme: " + gameRoom.theme + ", and timer: "
		    + gameRoom.timer);

	// Fire off a game start event
	addEvent(gameRoom.domain, new GameStartEvent());
	if (DEBUG)
	    System.out.println("Server: Fired Game Start Event");
    }

    public GameData getGameData(String roomName) {
	return null;
    }

    public String getMyUsername() {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User usr = (User) session.getAttribute("User");
	if (DEBUG)
	    System.out.println("Server: Sending client's username: "
		    + usr.username + " to client");
	return usr.username;
    }

    public String getStartGameChooser(String roomName) {
	String username = "";
	Domain domain = null;
	for (InGameRoom r : engine.inGameRooms) {
	    if (r.domain.getName().equalsIgnoreCase(roomName)) {
		domain = r.domain;
		// get the first username and send it
		username = r.users.get(0).username;
		if (DEBUG)
		    System.out.println("Server: Found the first chooser "
			    + username);
	    }
	}
	if (DEBUG)
	    System.out.println("Server: Sent client the first chooser: "
		    + username); // domain.getName() is null //TODO
	return username;
    }

    public void setReady(String username, String roomName) {
	int numberOfReady = 0;
	System.out.println("Server: Current ingameRooms:");
	for (InGameRoom ingameRoom : engine.inGameRooms) {
	    if (DEBUG)
		System.out.println("Server: Room: "
			+ ingameRoom.domain.getName());
	    if (ingameRoom.domain.getName().equals(roomName)) {
		if (DEBUG)
		    System.out.println("Server: Found user's room: " + roomName
			    + " in the current ingameRooms list");
		for (User user : ingameRoom.users) {
		    if (DEBUG)
			System.out.println("Contains user: " + user.username);
		    if (user.username.equalsIgnoreCase(username)) {
			user.isReadyToStart = true;
			numberOfReady++;
			if (DEBUG)
			    System.out.println("Server: Client: " + username
				    + " has his status set to 'ready'");
		    } else if (user.isReadyToStart) {
			numberOfReady++;
		    }
		}
		if (numberOfReady == ingameRoom.users.size()) {
		    // everyone is ready, fire the round start event
		    RoundStartEvent roundEvent = new RoundStartEvent();
		    roundEvent.chooser = ingameRoom.users
			    .get(0).username;
		    if (DEBUG) System.out.println("Server: Set the first rounds chooser to: " + roundEvent.chooser);
		    addEvent(ingameRoom.domain, roundEvent);
		    if (DEBUG)
			System.out
				.println("Server: Fired Initial Round Start Event with chooser: " + roundEvent.chooser);
		}
	    }
	}
    }

    public void submitPhrase(String phrase, String roomName) {
	HttpServletRequest request = this.getThreadLocalRequest();
	HttpSession session = request.getSession();
	User usr = (User) session.getAttribute("User");
	for (InGameRoom r : engine.inGameRooms) {
	    if (r.domain.getName().equals(roomName)) {
		int phrasesSubmittedCount = 0;
		for (User user : r.users) {
		    if (user.username.equals(usr.username)) {
			user.phrase = phrase;
			PhraseSubmittedEvent phraseEvent = new PhraseSubmittedEvent();
			phraseEvent.phrase = phrase;
			addEvent(r.domain, phraseEvent);
			if (DEBUG)
			    System.out
				    .println("Server: Fired Phrase Submitted Event for phrase "
					    + phrase);
			r.phrasesSubmitted++;
		    }
		}
	    }
	    if (r.phrasesSubmitted == r.users.size()) {
		addEvent(r.domain, new AllPhrasesSubmittedEvent());
		if (DEBUG)
		    System.out.println("Server: All users in room: "
			    + r.domain.getName()
			    + " have submitted phrases for this round.");
		if (DEBUG)
		    System.out
			    .println("Server: Fired All Phrases Submitted Event");
		r.phrasesSubmitted = 0;
	    }
	    return;
	}
    }

    public void choosePhrase(String phrase, String roomName) {
	for (InGameRoom r : engine.inGameRooms) {
	    if (r.domain.getName().equalsIgnoreCase(roomName)) {
		r.story.add(" " + phrase);
		r.phrasesSubmitted = 0;
		if (DEBUG)
		    System.out.println("Server: Got chosen phrase: " + phrase);
		PhraseChosenEvent phraseChosenEvent = new PhraseChosenEvent();
		phraseChosenEvent.phraseChosen = phrase + " ";
		if (DEBUG) 
		    System.out.println("Server: Fired Phrase Chosen Event");
		addEvent(DomainFactory.getDomain(roomName), phraseChosenEvent);

		RoundCloseEvent roundCloseEvent = new RoundCloseEvent();
		HashMap<String, Integer> pointList = new HashMap<String, Integer>();
		for (User user : r.users) {
		    if (user.phrase.equalsIgnoreCase(phrase)) {
			user.score++;
			if (DEBUG)
			    System.out.println("Server: " + user.username
				    + "'s word was chosen, his score is now: "
				    + user.score);
			if (user.score == r.pointCap) {
			    GameEndEvent gameEndEvent = new GameEndEvent();
			    gameEndEvent.winner = user.username;
			    addEvent(DomainFactory.getDomain(roomName), gameEndEvent);
			    if (DEBUG) System.out.println("Server: User: " + user.username + " has hit the point limit.");
			    if (DEBUG) System.out.println("Server: Fired GameEndEvent with winner: " + user.username);
			    r.gameEnded = true;
			}
		    }
		    pointList.put(user.username, user.score);
		}
		roundCloseEvent.pointList = pointList;
		addEvent(DomainFactory.getDomain(roomName), roundCloseEvent);
		if (r.gameEnded) return;
		if (DEBUG) System.out.println("Server: Fired Round Close Event for room: " + roomName);
		RoundStartEvent roundStartEvent = new RoundStartEvent();
		//advance turn count
		if (r.turnCounter == r.users.size() - 1) {
		    r.turnCounter = 0;
		} else {
		    r.turnCounter++;
		}
		roundStartEvent.chooser = r.users.get(r.turnCounter).username;
		addEvent(DomainFactory.getDomain(roomName), roundStartEvent);
		if (DEBUG) System.out.println("Server: Fired Round Start Event for room: " + roomName);
	    }
	}
    }

}
