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
import com.storytime.client.gameroom.UpdateGameRoomChatWindowEvent;
import com.storytime.client.gameroom.UpdatePlaceEvent;
import com.storytime.client.joinroom.JoinRoom;
import com.storytime.client.joinroom.JoinableRoomsInformation;
import com.storytime.client.joinroom.LobbyRoomDisbandedEvent;
import com.storytime.client.joinroom.LobbyRoomHostedEvent;
import com.storytime.client.lobby.LobbyInformation;
import com.storytime.client.lobby.LobbyUserLeftEvent;
import com.storytime.client.lobby.UpdateLobbyEvent;
import com.storytime.client.lobby.UpdateLobbyMessagesEvent;
import com.storytime.client.lobby.UpdateLobbyUsersEvent;
import com.storytime.client.lobbyroom.GameStartEvent;
import com.storytime.client.lobbyroom.LobbyRoomData;
import com.storytime.client.lobbyroom.RoomDisbandedEvent;
import com.storytime.client.lobbyroom.UpdateAuthorsTimerEvent;
import com.storytime.client.lobbyroom.UpdateMastersTimerEvent;
import com.storytime.client.lobbyroom.UpdatePasswordEvent;
import com.storytime.client.lobbyroom.UpdatePointLimitEvent;
import com.storytime.client.lobbyroom.UpdateRoomChatWindowEvent;
import com.storytime.client.lobbyroom.UserEnteredRoomEvent;
import com.storytime.client.lobbyroom.UserLeftRoomEvent;
import com.storytime.client.skillrelated.Skill;
import com.storytime.client.skillrelated.SkillHolder;

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
		logger.log(Level.INFO, "Starting Server");
		for (int x = 0; x < 1000; x++) {
			addEvent(UpdateLobbyEvent.domain, new UpdateLobbyEvent());
			System.out.println("Adding event " + x);
		}
	}

	@Override
	public Boolean logoutUser() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		logger.log(Level.FINEST, "Server: Attempting to log-out " + user.getUsername());
		boolean successfullLogOut = engine.logoutUser(user.getUsername(), user.getPassword());
		if (successfullLogOut) {
			LobbyUserLeftEvent userLeftEvent = new LobbyUserLeftEvent();
			userLeftEvent.setUsernameOfUserWhoLeft(user.getUsername());
			logger.log(Level.FINEST, ("Server: Successfully logged out user: + " + user.getUsername()));
			addEvent(LobbyUserLeftEvent.domain, userLeftEvent);
			logger.log(Level.FINEST, "Server: Fired User-Left-Event for the lobby domain, for the user: " + user.getUsername());
			return true;
		} else {
			logger.log(Level.FINEST, "Server: User: " + user.getUsername() + " was unable to be logged out");
			return false;
		}
	}

	public Boolean loginUser(String username, String password) {
		logger.log(Level.FINEST, "Server: Attempting to log in " + username);
		if (engine.loginUser(username, password)) {
			HttpServletRequest request = this.getThreadLocalRequest();
			HttpSession session = request.getSession();
			session.setAttribute("User", new User(username, password, null));
			logger.log(Level.FINEST, "Server: " + username + " logged into the game with password: " + password);
			logger.log(Level.FINEST, "Server: Fired Update Lobby Users Event for user: " + username);
			User user = engine.onlineUsers.get(username);
			fireUserEnteredLobbyEvent(user);
			user.location = "Lobby";
			logger.log(Level.FINEST, "Server: Set " + user.username + "'s location to: " + user.location);
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

	public void hostLobbyRoom(String roomName, String theme, String password, int numberOfPlayers) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		boolean foundUser = false;
		user = engine.getOnlineUsers().get(user.username);
		if (user != null) {
			foundUser = true;
			engine.hostRoom(user, roomName, theme, password, numberOfPlayers);
			logger.log(Level.FINEST, "Server " + user.username + " hosted a room with the name: " + roomName + " and the theme: " + theme
					+ " and the with the password: " + password + " and with the maximum number of players at: " + numberOfPlayers);
			Room hostedRoom = engine.lobbyRooms.get(roomName);
			LobbyRoomHostedEvent roomsEvent = new LobbyRoomHostedEvent();
			roomsEvent.setRoomName(hostedRoom.getRoomName());
			roomsEvent.setTheme(hostedRoom.getTheme());
			roomsEvent.setPassword(hostedRoom.getPassword());
			roomsEvent.setNumberOfPlayers(hostedRoom.getNumberOfPlayers());
			roomsEvent.setPointLimit(hostedRoom.getPointLimit());
			roomsEvent.setAuthorsTime(hostedRoom.getAuthorsTime());
			roomsEvent.setMastersTime(hostedRoom.getMastersTime());
			addEvent(LobbyRoomHostedEvent.domain, roomsEvent);
			logger.log(Level.FINEST, "Server: Fired LobbyRoomHostedEvent for room: " + roomName);
			user.location = "LobbyRoom";
			logger.log(Level.FINEST, "Server: Set " + user.username + "'s location to: " + user.location);
		}
		if (!foundUser)
			logger.log(Level.FINER, "Server: Couldn't find user: " + user.username + " in the lobby user list. (Host Room Method)");
	}

	public void joinRoom(String roomName) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		User thisUser = engine.onlineUsers.get(user.username);
		boolean loginStatus = engine.joinRoom(thisUser, roomName);
		if (loginStatus) {
			logger.log(Level.FINEST, "Server: " + user.username + "joined the room: " + roomName);
			thisUser.location = "LobbyRoom";
			logger.log(Level.FINEST, "Server: Set " + user.username + "'s location to: " + user.location);
		}
		if (!loginStatus) {
			logger.log(Level.WARNING, "Server: " + user.username + " tried to join the room: " + roomName + ", which doesn't exist");
			thisUser.location = "Lobby";
			logger.log(Level.FINEST, "Server: Set " + user.username + "'s location to: " + user.location);
		}
		UserEnteredRoomEvent userEnteredRoomEvent = new UserEnteredRoomEvent();
		userEnteredRoomEvent.setUsername(thisUser.getUsername());
		addEvent(DomainFactory.getDomain(thisUser.getRoom().roomName), userEnteredRoomEvent);
		logger.log(Level.FINEST, "Server: Fired a UserEnteredRoomEvent for domain: " + thisUser.getRoom().getRoomName());
		addEvent(DomainFactory.getDomain("Lobby"), userEnteredRoomEvent);
		logger.log(Level.FINEST, "Server: Fired a UserEnteredRoomEvent for domain: Lobby");
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
		roomInfo.setPointCap(room.getPointLimit());
		roomInfo.setTheme(room.getTheme());
		roomInfo.setAuthorsTimer(room.getAuthorsTime());
		roomInfo.setRoomName(room.getRoomName());
		roomInfo.setMastersTimer(room.getMastersTime());
		roomInfo.setHostsName(room.getHost().getUsername());
		roomInfo.setPassword(room.getPassword());
		logger.log(Level.FINEST, "Server: Adding users to the list of room information to be sent to " + user.username);
		for (String usrname : room.users.keySet()) {
			// Add the relevant users to the room info to be sent
			roomInfo.users.add(usrname);
			logger.log(Level.FINEST, "Server: Added user: " + usrname + " to the roomInfo list to be sent");
		}
		roomInfo.inGame = room.inGame;
		roomInfo.messages = room.roomChat;
		// set a new domain
		roomInfo.domain = DomainFactory.getDomain(room.roomName);
		logger.log(Level.FINEST, "Server: " + user.username + " has been sent the information pertaining to his/her current lobby room: " + room.roomName);
		return roomInfo;
	}

	public void updateLobbyRoomPointLimit(String roomName, int pointCap) {
		Room r = engine.getLobbyRooms().get(roomName);
		r.pointLimit = pointCap;
		logger.log(Level.FINEST, "Server: Set " + r.roomName + "'s pointCap to " + r.pointLimit);
		UpdatePointLimitEvent pointChangeEvent = new UpdatePointLimitEvent();
		pointChangeEvent.setPointLimit(r.getPointLimit());
		pointChangeEvent.setRoomName(roomName);
		addEvent(DomainFactory.getDomain(roomName), pointChangeEvent);
		logger.log(Level.FINEST, "Server: Fired Point Change Event for domain: " + roomName);
		addEvent(DomainFactory.getDomain("Lobby"), pointChangeEvent);
		logger.log(Level.FINEST, "Server: Fired Point Change Event for domain: Lobby");
		return;
	}

	public void updateLobbyRoomTimer(String roomName, int timer) {
		logger.log(Level.FINEST, "Server: Got an UpdateSubmissionTimerEvent for room: " + roomName + " and updated submissionTimer value to: " + timer);
		Room r = engine.getLobbyRooms().get(roomName);
		r.authorsTime = timer;
		UpdateAuthorsTimerEvent submissionTimerEvent = new UpdateAuthorsTimerEvent();
		submissionTimerEvent.authorsTimer = timer;
		submissionTimerEvent.roomName = roomName;
		addEvent(DomainFactory.getDomain(roomName), submissionTimerEvent);
		logger.log(Level.FINEST, "Server: Fired UpdateSubmissionTimerEvent for domain: " + roomName);
		addEvent(DomainFactory.getDomain("Lobby"), submissionTimerEvent);
		logger.log(Level.FINEST, "Server: Fired UpdateSubmissionTimerEvent for domain: Lobby");
	}

	public void updateLobbyRoomChooserTimer(String roomName, int timer) {
		logger.log(Level.FINEST, "Server: Got an UpdateChooserTimerEvent for room: " + roomName + " and updated chooserTimer value to: " + timer);
		Room room = engine.getLobbyRooms().get(roomName);
		room.mastersTime = timer;
		UpdateMastersTimerEvent chooserTimerEvent = new UpdateMastersTimerEvent();
		chooserTimerEvent.mastersTime = timer;
		chooserTimerEvent.roomName = roomName;
		addEvent(DomainFactory.getDomain(roomName), chooserTimerEvent);
		logger.log(Level.FINEST, "Server: Fired UpdateChooserTimerEvent for domain: " + roomName);
		addEvent(DomainFactory.getDomain("Lobby"), chooserTimerEvent);
		logger.log(Level.FINEST, "Server: Fired UpdateChooserTimerEvent for domain: Lobby");
	}

	public void leaveRoom(String roomName) {
		leaveRoomHelper(roomName);
	}

	public void leaveRoomAfterLocationCheck() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		leaveRoomHelper(usr.room.roomName);
	}

	private void leaveRoomHelper(String roomName) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		User foundUser = engine.getOnlineUsers().get(usr.username);
		Room usersRoom = foundUser.room;
		if (usersRoom.host.getUsername().equalsIgnoreCase(foundUser.username)) {
			// Disband room
			engine.lobbyRooms.remove(usersRoom.roomName);
			foundUser.room = null;
			logger.log(Level.FINEST, "Server: User " + usr.username + " was the host of " + roomName + " and has left the room, so it has been disbanded");
			// Remove room from room list
			RoomDisbandedEvent roomDisbandedEvent = new RoomDisbandedEvent();
			addEvent(DomainFactory.getDomain(roomName), roomDisbandedEvent);
			logger.log(Level.FINEST, "Server: Fired a RoomDisbandedEvent to notify users in the current disbanded room");
			LobbyRoomDisbandedEvent lobbyRoomDisbandedEvent = new LobbyRoomDisbandedEvent();
			lobbyRoomDisbandedEvent.setRoomName(roomName);
			addEvent(DomainFactory.getDomain("Lobby"), lobbyRoomDisbandedEvent);
			logger.log(Level.FINEST, "Server: Fired a LobbyRoomDisbandedEvent to notify users in the lobby of a room that can no longer be joined");
		} else {
			UserLeftRoomEvent userLeftEvent = new UserLeftRoomEvent();
			userLeftEvent.username = foundUser.username;
			addEvent(DomainFactory.getDomain(roomName), userLeftEvent);
			// Remove user from the room
			usersRoom.users.remove(foundUser.username);
			logger.log(Level.FINEST, "Server: Fired User Left Event for the domain " + roomName);
		}
		foundUser.location = "Lobby";
		logger.log(Level.FINEST, "Server: Set the location for user: " + foundUser.username + " to be: " + foundUser.location);
	}

	public void sendRoomChatMessage(String roomName, String message) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");

		Room room = engine.getLobbyRooms().get(roomName);
		room.roomChat.add(usr.username + ": " + message);

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
		gameRoom.pointCap = room.pointLimit;
		gameRoom.theme = room.theme;
		gameRoom.authorTimer = room.authorsTime;
		gameRoom.mastersTimer = room.mastersTime;

		for (User user : room.users.values()) {
			gameRoom.users.add(user);
			user.ingameRoom = gameRoom;
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
		logger.log(Level.INFO, "Started a game for room " + roomName + ", with attributes: " + gameRoom.domain + ", pointCap: " + gameRoom.pointCap
				+ ", theme: " + gameRoom.theme + ", submissionTimer: " + gameRoom.authorTimer + ", and mastersTimer: " + gameRoom.mastersTimer);

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
				user.location = "GameRoom";
				logger.log(Level.FINEST, "Server: Set " + user.username + "'s location to: " + user.location);
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
			User chooser = ingameRoom.users.get(0);
			ingameRoom.chooser = chooser;
			roundEvent.chooser = chooser.username;
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
						// user.phrase = phrase; // TODO: Take out
						user.getPhrasesPerRound().add(phrase);
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
		r.story.add(phrase + " ");
		r.phrasesSubmitted = 0;
		logger.log(Level.FINEST, "Server: Got the chosen phrase: " + phrase);
		PhraseChosenEvent phraseChosenEvent = new PhraseChosenEvent();
		phraseChosenEvent.phraseChosen = phrase + " ";
		logger.log(Level.FINEST, "Server: Fired Phrase Chosen Event");
		addEvent(DomainFactory.getDomain(roomName), phraseChosenEvent);

		fireRoundCloseEvent(r, phrase);
		if (r.gameEnded)
			return;

		fireRoundStartEvent(r);
	}

	public void sendGameRoomChatMessage(String roomName, String message) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		String messageWithUser = usr.username + ": " + message;

		InGameRoom gameRoom = engine.gameRooms.get(roomName);
		logger.log(Level.FINEST, "Server: Received message: '" + message + "', for  domain: " + gameRoom.domain.getName());
		gameRoom.messages.add(messageWithUser);
		UpdateGameRoomChatWindowEvent chatEvent = new UpdateGameRoomChatWindowEvent();
		chatEvent.message = messageWithUser;
		addEvent(gameRoom.domain, chatEvent);
		logger.log(Level.FINEST, "Server: Fired UpdateGameRoomChatWindowEvent for domain: " + gameRoom.domain.getName());
	}

	public String getLocation() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		User user = engine.onlineUsers.get(usr.username);
		return user.location;
	}

	public void setTimerElapsed() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		User user = engine.onlineUsers.get(usr.username);
		InGameRoom usersRoom = user.ingameRoom;
		logger.log(Level.FINEST, "Server: Setting client: " + user.username + "'s timer as elapsed");
		if (usersRoom.chooser.username.equalsIgnoreCase(user.username)) {
			usersRoom.choosersTimerElapsed = true;
			logger.log(Level.FINEST, "Server: Client: " + user.username + " is supposed to be the chooser for this round");
			logger.log(Level.FINEST, "Server: Choosing random phrase for room: " + usersRoom.domain.getName());
			String randomlySelectedPhrase = engine.chooseRandomPhrase(usersRoom);
			if (randomlySelectedPhrase != null && !randomlySelectedPhrase.equalsIgnoreCase("")) {
				choosePhrase(randomlySelectedPhrase, usersRoom.domain.getName());
				logger.log(Level.FINEST, "Server: A randomly selected phrase was chosen: " + randomlySelectedPhrase);
			} else {
				logger.log(Level.FINEST, "Server: There were no submissions for room: " + usersRoom.domain.getName() + " so a new round will begin");
				logger.log(Level.FINEST, "Server: Initiating the round close for room: " + usersRoom.domain.getName());
				fireRoundCloseEvent(usersRoom, randomlySelectedPhrase);
				if (usersRoom.gameEnded) {
					return;
				}
				logger.log(Level.FINEST, "Server: Initiating a round start for room: " + usersRoom.domain.getName());
				fireRoundStartEvent(usersRoom);
			}
		} else {
			logger.log(Level.FINEST, "Server: The submitter: " + user.username + "'s submission timer has elapsed");
			user.timerElapsed = true;
			usersRoom.numberOfUsersWhoseTimersHaveElapsed++;
		}
		logger.log(Level.FINEST, "Server: Set " + user.username + "'s timer elapsed value to: " + user.timerElapsed);
		if (usersRoom.numberOfUsersWhoseTimersHaveElapsed == usersRoom.users.size() - 1) {
			logger.log(Level.FINEST,
					"Server: The timers for all the submitters for this round have elapsed and no phrases have been submitted for this round in room: "
							+ usersRoom.domain.getName());
			logger.log(Level.FINEST, "Server: Initiating round close for room: " + usersRoom.domain.getName());
			fireRoundCloseEvent(usersRoom, "");
			logger.log(Level.FINEST, "Server: Initiating round start for room: " + usersRoom.domain.getName());
			fireRoundStartEvent(usersRoom);
		}
	}

	private void fireRoundCloseEvent(InGameRoom gameRoom, String phrase) {
		String roomName = gameRoom.domain.getName();
		RoundCloseEvent roundCloseEvent = new RoundCloseEvent();
		HashMap<String, Integer> pointList = new HashMap<String, Integer>();
		for (User user : gameRoom.users) {
			int currentPhraseIndex = engine.getIndexOfNewestPhraseFromHistory(user);
			if (user.getPhrasesPerRound().size() != 0 && user.getPhrasesPerRound().get(currentPhraseIndex).equalsIgnoreCase(phrase)
					&& !phrase.equalsIgnoreCase("")) {
				user.score++;
				logger.log(Level.FINEST, "Server: " + user.username + "'s word was chosen and his score is now " + user.score);
				if (user.score == gameRoom.pointCap) {
					GameEndEvent gameEndEvent = new GameEndEvent();
					gameEndEvent.winner = user.username;
					addEvent(DomainFactory.getDomain(roomName), gameEndEvent);
					logger.log(Level.INFO, "Server: User: " + user.username + " has hit the point limit and won the game for room: " + roomName);
					logger.log(Level.FINEST, "Server: Fired GameEndEvent containing the winners username: " + user.username);
					gameRoom.gameEnded = true;
				}
			}
			pointList.put(user.username, user.score);
		}
		roundCloseEvent.pointList = pointList;
		addEvent(DomainFactory.getDomain(roomName), roundCloseEvent);
		logger.log(Level.FINEST, "Server: Fired Round Close Event for room: " + roomName);
		if (gameRoom.gameEnded)
			logger.log(Level.FINEST, "Server: The room: " + roomName + "'s game has ended");
		engine.refreshTimerElapsedValues(gameRoom);
		engine.clearAllUsersSubmittedPhrases(roomName);
		logger.log(Level.FINEST, "Server: Reset the phrases for room: " + roomName);
		firePlaceChangeEvent(gameRoom);
	}

	private void fireRoundStartEvent(InGameRoom gameRoom) {
		engine.refreshTimerElapsedValues(gameRoom);
		logger.log(Level.FINEST, "Server: Reset the timers for users in room: " + gameRoom.domain.getName());
		String roomName = gameRoom.domain.getName();
		RoundStartEvent roundStartEvent = new RoundStartEvent();
		// advance turn count
		if (gameRoom.turnCounter == gameRoom.users.size() - 1) {
			gameRoom.turnCounter = 0;
		} else {
			gameRoom.turnCounter++;
		}
		User chooser = gameRoom.users.get(gameRoom.turnCounter);
		roundStartEvent.chooser = chooser.username;
		gameRoom.chooser = chooser;
		addEvent(DomainFactory.getDomain(roomName), roundStartEvent);
		logger.log(Level.FINEST, "Server: Fired Round Start Event for room: " + roomName);
	}

	private void firePlaceChangeEvent(InGameRoom gameRoom) {
		UpdatePlaceEvent updatePlaceEvent = new UpdatePlaceEvent();
		HashMap<String, Integer> placesList = engine.setUserPlacesAtTheEndOfRound(gameRoom);
		updatePlaceEvent.setPlacesList(placesList);
		addEvent(gameRoom.domain, updatePlaceEvent);
		logger.log(Level.FINEST, "Server: Fired Place Change Event for room: " + gameRoom.domain.getName());
	}

	private void fireUserEnteredLobbyEvent(User user) {
		UpdateLobbyUsersEvent usersEvent = new UpdateLobbyUsersEvent();
		usersEvent.setUsername(user.getUsername());
		addEvent(usersEvent.getDomain(), usersEvent);
	}

	private void fireUserLeftLobbyEvent(User user) {
		UserEnteredRoomEvent userEnteredRoomEvent = new UserEnteredRoomEvent();
		userEnteredRoomEvent.setUsername(user.getUsername());
		addEvent(DomainFactory.getDomain("Lobby"), userEnteredRoomEvent);
	}

	@Override
	public JoinableRoomsInformation getJoinableRoomsAndTheirInformation() {
		JoinableRoomsInformation joinableRoomsInformation = new JoinableRoomsInformation();
		for (Room room : engine.lobbyRooms.values()) {
			JoinRoom joinRoom = new JoinRoom();
			joinRoom.roomName = room.roomName;
			joinRoom.theme = room.theme;
			joinRoom.mastersTime = room.mastersTime;
			joinRoom.authorsTime = room.authorsTime;
			joinRoom.numberOfPlayers = room.numberOfPlayers;
			joinRoom.pointLimit = room.pointLimit;
			joinRoom.password = room.password;
			joinableRoomsInformation.joinableRooms.add(joinRoom);
		}
		return joinableRoomsInformation;
	}

	@Override
	public void setPassword(String roomName, String password) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute("User");
		engine.setPassword(roomName, usr, password);
		logger.log(Level.FINEST, "Server: Set the password for room: " + roomName + " to be: " + password);
		UpdatePasswordEvent passwordChangedEvent = new UpdatePasswordEvent();
		passwordChangedEvent.setNewPassword(password);
		passwordChangedEvent.setRoomName(roomName);
		addEvent(DomainFactory.getDomain(roomName), passwordChangedEvent);
		logger.log(Level.FINEST, "Server: Fired a LobbyRoomPasswordChangedEvent for room: " + roomName);
		addEvent(DomainFactory.getDomain("Lobby"), passwordChangedEvent);
		logger.log(Level.FINEST, "Server: Fired a LobbyRoomPasswordChangedEvent for the lobby");
	}

	@Override
	public void activateSkills(SkillHolder skillHolder) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User someoneInTheRoom = (User) session.getAttribute("User");
		// User victim = engine.getUserThatOwnsThisPhrase(, someoneInTheRoom)
		for (Skill skill : skillHolder.skillList.values()) {

		}
	}

	@Override
	public boolean leaveLobby() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		// This isn't what really happens, the server should push a new type of
		// event because the user is just leaving the lobby
		if (engine.leaveLobby(user)) {
			System.out.println("Client: Fired userJoinedRoomEvent for user: " + user.getUsername()
					+ " (this is only a temporary fix. This needs to be a new type of event)");
			fireUserLeftLobbyEvent(user);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void enterLobby() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		engine.enterLobby(user);
		fireUserEnteredLobbyEvent(user);
	}

}
