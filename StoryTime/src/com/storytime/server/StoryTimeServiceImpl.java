package com.storytime.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class StoryTimeServiceImpl extends RemoteEventServiceServlet implements StoryTimeService {

	private static final long serialVersionUID = 1L;
	StoryTimeEngine engine;
	final boolean DEBUG = true;
	private final Logger logger = Logger.getLogger("Server");
	private final ConsoleHandler consoleHandler = new ConsoleHandler();

	public StoryTimeServiceImpl() {
		logger.addHandler(consoleHandler);
		logger.setLevel(Level.FINEST);
		consoleHandler.setLevel(Level.FINEST);
		engine = new StoryTimeEngine();
	}

	public void startServer() {
		// System.out.println("Started server");
		logger.log(Level.INFO, "Starting Server");
		for (int x = 0; x < 1000; x++) {
			addEvent(UpdateLobbyEvent.domain, new UpdateLobbyEvent());
			System.out.println("Adding event " + x);
		}
	}

	public Boolean loginUser(String username, String password) {
		logger.log(Level.FINEST, "Server: Attempting to log in " + username);

		if (engine.loginUser(username, password)) {
			HttpServletRequest request = this.getThreadLocalRequest();
			HttpSession session = request.getSession();
			session.setAttribute("User", new User(username, password, null));
			UpdateLobbyUsersEvent usersEvent = new UpdateLobbyUsersEvent();
			usersEvent.setUsername(username);
			addEvent(usersEvent.getDomain(), usersEvent);
			logger.log(Level.FINEST, "Server: " + username + " logged into the game with password: " + password);
			logger.log(Level.FINEST, "Server: Fired Update Lobby Users Event for user: " + username);
			return true;
		} else {
			return false;
		}
	}

	public LobbyInformation getInitialLobbyInformation() {
		LobbyInformation lobbyInfo = new LobbyInformation();
		lobbyInfo.chatMessages = engine.lobbyChat;
		for (String roomName : engine.getLobbyRooms().keySet()) {
			lobbyInfo.rooms.add(roomName);
		}
		for (String username : engine.usersInLobby.keySet()) {
			lobbyInfo.users.add(username);
		}
		logger.log(Level.FINEST, "Server: Lobby information has been sent out");
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
		logger.log(Level.FINEST, "Server: Lobby Message Event sent to clients to update for message: " + message);
	}

	public void hostLobbyRoom(String roomName, String theme) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		boolean foundUser = false;
		user = engine.getOnlineUsers().get(user.username);
		if (user != null) {
			foundUser = true;
		}
		if (!foundUser)
			logger.log(Level.FINER, "Server: Couldn't find user: " + user.username + " in the lobby user list. (Host Room Method)");
		engine.hostRoom(user, roomName, theme);
		UpdateLobbyRoomsEvent roomsEvent = new UpdateLobbyRoomsEvent();
		roomsEvent.roomName = roomName;
		addEvent(UpdateLobbyRoomsEvent.domain, roomsEvent);
		logger.log(Level.FINEST, "Server " + user.username + " hosted a room with the name: " + roomName + " and the theme: " + theme);
		logger.log(Level.FINEST, "Server: Fired UpdateLobbyRoomsEvent for room: " + roomName);
	}

	public void joinRoom(String roomName) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		User thisUser = engine.onlineUsers.get(user.username);
		boolean loginStatus = engine.joinRoom(thisUser, roomName);
		if (loginStatus)
			logger.log(Level.FINEST, "Server: " + user.username + "joined the room: " + roomName);
		if (!loginStatus)
			logger.log(Level.WARNING, "Server: " + user.username + " tried to join the room: " + roomName + ", which doesn't exist");
	}

	public LobbyRoomData getLobbyRoomInformation() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		boolean foundUser = false;
		user = engine.getOnlineUsers().get(user.username);
		Room room = engine.getUsersRoom(user.username);

		if (!(engine.getOnlineUsers().get(user.username) == null)) {
			foundUser = true;
		}
		if (!foundUser) {
			logger.log(Level.WARNING, "Server: " + user.username + " could not get his/her lobby room information because this user is not logged in");
		}
		if (room == null) {
			logger.log(Level.WARNING, "Server: " + user.username + " has no room so this client should not be calling getLobbyRoomInformation");
		} else {
			logger.log(Level.FINEST, "Server: Got " + user.username + "'s room: " + room.roomName);
		}
		LobbyRoomData roomInfo = new LobbyRoomData();
		roomInfo.pointCap = room.pointCap;
		roomInfo.theme = room.theme;
		roomInfo.timer = room.timer;
		roomInfo.roomName = room.roomName;
		logger.log(Level.FINEST, "Server: Adding users to the list of room information to be sent to " + user.username);
		for (String usrname : room.users.keySet()) {
			// Add the relevant users to the room info to be sent
			roomInfo.users.add(usrname);
			logger.log(Level.FINEST, "Server: Added user: " + usrname + " to the roomInfo list to be sent");
		}
		roomInfo.inGame = room.inGame;
		// set a new domain
		roomInfo.domain = DomainFactory.getDomain(room.roomName);
		logger.log(Level.FINEST, "Server: " + user.username + " has been sent the information pertaining to his/her current lobby room: " + room.roomName);
		return roomInfo;
	}

	public void updateLobbyRoomPointCap(String roomName, int pointCap) {
		Room r = engine.getLobbyRooms().get(roomName);
		r.pointCap = pointCap;
		logger.log(Level.FINEST, "Server: Set " + r.roomName + "'s pointCap to " + r.pointCap);
		UpdatePointCapEvent pointChangeEvent = new UpdatePointCapEvent();
		pointChangeEvent.pointCap = r.pointCap;
		addEvent(DomainFactory.getDomain(roomName), pointChangeEvent);
		logger.log(Level.FINEST, "Server: Fired Point Change Event for domain: " + roomName);
		return;
	}

	public void updateLobbyRoomTimer(String roomName, int timer) {
		logger.log(Level.FINEST, "Server: Got an UpdateTimerEvent for room: " + roomName + " and updated timer value to: " + timer);
		for (Room r : engine.getLobbyRooms().values()) {
			if (r.roomName.equalsIgnoreCase(roomName)) {
				r.timer = timer;
				UpdateTimerEvent timerEvent = new UpdateTimerEvent();
				timerEvent.timer = timer;
				addEvent(DomainFactory.getDomain(roomName), timerEvent);
				logger.log(Level.FINEST, "Server: Fired UpdateTimerEvent for domain: " + roomName);
				return;
			}
		}
	}

	public void leaveRoom(String roomName) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		User foundUser = engine.getOnlineUsers().get(usr.username);
		Room usersRoom = foundUser.room;
		if (usersRoom.host.getUsername().equalsIgnoreCase(foundUser.username)) {
			// Disband room
			RoomDisbandedEvent roomDisbandedEvent = new RoomDisbandedEvent();
			addEvent(DomainFactory.getDomain(roomName), roomDisbandedEvent);
			// Remove room from room list
			engine.getLobbyRooms().remove(usersRoom);
			logger.log(Level.INFO, "User " + usr.username + " was the host of " + roomName + " and has left the room, so it has been disbanded");
		} else {
			UserLeftRoomEvent userLeftEvent = new UserLeftRoomEvent();
			userLeftEvent.username = foundUser.username;
			addEvent(DomainFactory.getDomain(roomName), userLeftEvent);
			// Remove user from the room
			usersRoom.users.remove(foundUser);
			logger.log(Level.FINEST, "Server: Fired User Left Event for the domain " + roomName);
		}
		return;
	}

	public void sendRoomChatMessage(String roomName, String message) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");

		Room room = engine.getLobbyRooms().get(roomName);
		if (room.roomName.equalsIgnoreCase(roomName)) {
			room.roomChat.add(usr.username + ": " + message);
		}
		UpdateRoomChatWindowEvent chatWindowEvent = new UpdateRoomChatWindowEvent();
		chatWindowEvent.message = usr.username + ": " + message;
		logger.log(Level.FINEST, "Server: Recieved message '" + message + "' for dispersal");
		addEvent(DomainFactory.getDomain(roomName), chatWindowEvent);
		logger.log(Level.FINEST, "Server: Fired UpdateRoomChatWindowEvent");
	}

	public void startGame(String roomName) {
		// Find the room
		Room room = engine.getLobbyRooms().get(roomName);
		room.inGame = true;
		logger.log(Level.FINEST, "Server: Set the room: " + roomName + " to in-game");
		logger.log(Level.FINEST, "Server: Trying to start a game for room: " + room.roomName);
		// Add the room's data to a new InGameRoom object
		InGameRoom gameRoom = new InGameRoom();
		gameRoom.domain = DomainFactory.getDomain(room.roomName);
		gameRoom.pointCap = room.pointCap;
		gameRoom.theme = room.theme;
		gameRoom.timer = room.timer;

		for (User user : room.users.values()) {
			gameRoom.users.add(user);
		}

		// Set the turn order randomly
		Collections.shuffle(gameRoom.users);

		// initialize scores to 0
		for (User user : gameRoom.users) {
			gameRoom.scoreList.put(user, 0);
		}
		// Create an ingame room
		engine.getGameRooms().put(gameRoom.domain.getName(), gameRoom);
		engine.getLobbyRooms().remove(room.roomName);
		logger.log(Level.INFO, "Started a game for room " + roomName + ", with attributes: " + gameRoom.domain + ", pointCap: " + gameRoom.pointCap + ", theme: " + gameRoom.theme
				+ ", and timer: " + gameRoom.timer);

		// Fire off a game start event
		addEvent(gameRoom.domain, new GameStartEvent());
		logger.log(Level.FINEST, "Server: Fired Game Start Event for room: " + roomName);
	}

	public GameData getGameData(String roomName) {
		return null;
	}

	public String getMyUsername() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		logger.log(Level.FINEST, "Server: Client requested notification of his/her username. Sending the client it's username: " + usr.username);
		return usr.username;
	}

	public String getStartGameChooser(String roomName) {
		String username = "";
		Domain domain = null;
		InGameRoom foundGameRoom = engine.getGameRooms().get(roomName);
		username = foundGameRoom.users.get(0).username;
		logger.log(Level.FINEST, "Server: Sent client the first phrase chooser: " + username + ", for room: " + roomName);
		return username;
	}

	public void setReady(String username, String roomName) {
		int numberOfReady = 0;
		logger.log(Level.FINEST, "Server: The current rooms that are in-game are as follows:");
		InGameRoom ingameRoom = engine.getGameRooms().get(roomName);
		logger.log(Level.FINEST, "Server: Room: " + ingameRoom.domain.getName());
		logger.log(Level.FINEST, "Server: Found user's room " + roomName + " in the current list of in-game rooms");

		for (User user : ingameRoom.users) {
			logger.log(Level.FINEST, "Server: Contains user: " + user.username);
			if (user.username.equalsIgnoreCase(username)) {
				user.isReadyToStart = true;
				numberOfReady++;
				logger.log(Level.FINEST, "Server: " + username + " is now ready to begin the game (status is set to ready)");
			} else if (user.isReadyToStart) {
				numberOfReady++;
			}
		}
		if (numberOfReady == ingameRoom.users.size()) {
			// everyone is ready, fire the round start event
			RoundStartEvent roundEvent = new RoundStartEvent();
			roundEvent.chooser = ingameRoom.users.get(0).username;
			logger.log(Level.FINEST, "Server: Set the first rounds chooser for game room: " + ingameRoom.domain.getName() + ", to be: " + roundEvent.chooser);
			addEvent(ingameRoom.domain, roundEvent);
			logger.log(Level.FINEST, "Server: Fired Initial Round Start Event containing the name of the chooser: " + roundEvent.chooser);
		}
	}

	public void submitPhrase(String phrase, String roomName) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		for (InGameRoom r : engine.getGameRooms().values()) {
			if (r.domain.getName().equals(roomName)) {
				int phrasesSubmittedCount = 0;
				for (User user : r.users) {
					if (user.username.equals(usr.username)) {
						user.phrase = phrase;
						PhraseSubmittedEvent phraseEvent = new PhraseSubmittedEvent();
						phraseEvent.phrase = phrase;
						addEvent(r.domain, phraseEvent);
						logger.log(Level.FINEST, "Server: Fired Phrase Submitted Event for phrase '" + phrase + "' submitted by: " + usr.username);
						r.phrasesSubmitted++;
					}
				}
			}
			if (r.phrasesSubmitted == r.users.size()) {
				addEvent(r.domain, new AllPhrasesSubmittedEvent());
				logger.log(Level.FINEST, "Server: All users in the room: " + r.domain.getName() + " have submitted phrases for this round");
				logger.log(Level.FINEST, "Server: Fired All Phrases Submitted Event");
				r.phrasesSubmitted = 0;
			}
			return;
		}
	}

	public void choosePhrase(String phrase, String roomName) {
		InGameRoom r = engine.getGameRooms().get(roomName);
		r.story.add(" " + phrase);
		r.phrasesSubmitted = 0;
		logger.log(Level.FINEST, "Server: Got the chosen phrase: " + phrase);
		PhraseChosenEvent phraseChosenEvent = new PhraseChosenEvent();
		phraseChosenEvent.phraseChosen = phrase + " ";
		logger.log(Level.FINEST, "Server: Fired Phrase Chosen Event");
		addEvent(DomainFactory.getDomain(roomName), phraseChosenEvent);

		RoundCloseEvent roundCloseEvent = new RoundCloseEvent();
		HashMap<String, Integer> pointList = new HashMap<String, Integer>();
		for (User user : r.users) {
			if (user.phrase.equalsIgnoreCase(phrase)) {
				user.score++;
				logger.log(Level.FINEST, "Server: " + user.username + "'s word was chosen and his score is now " + user.score);
				if (user.score == r.pointCap) {
					GameEndEvent gameEndEvent = new GameEndEvent();
					gameEndEvent.winner = user.username;
					addEvent(DomainFactory.getDomain(roomName), gameEndEvent);
					logger.log(Level.INFO, "User: " + user.username + " has hit the point limit and won the game for room: " + roomName);
					logger.log(Level.FINEST, "Server: Fired GameEndEvent containing the winners username: " + user.username);
					r.gameEnded = true;
				}
			}
			pointList.put(user.username, user.score);
		}
		roundCloseEvent.pointList = pointList;
		addEvent(DomainFactory.getDomain(roomName), roundCloseEvent);
		if (r.gameEnded)
			return;
		logger.log(Level.FINEST, "Server: Fired Round Close Event for room: " + roomName);
		RoundStartEvent roundStartEvent = new RoundStartEvent();
		// advance turn count
		if (r.turnCounter == r.users.size() - 1) {
			r.turnCounter = 0;
		} else {
			r.turnCounter++;
		}
		roundStartEvent.chooser = r.users.get(r.turnCounter).username;
		addEvent(DomainFactory.getDomain(roomName), roundStartEvent);
		logger.log(Level.FINEST, "Server: Fired Round Start Event for room: " + roomName);
	}
}
