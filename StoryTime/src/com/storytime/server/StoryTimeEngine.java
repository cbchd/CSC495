package com.storytime.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.storytime.client.skill.offense.LetterAdditionAttack;
import com.storytime.client.skill.offense.LetterRemovalAttack;
import com.storytime.client.skill.offense.LetterSubstitutionAttack;

/**
 * @author Chad
 *
 */
public class StoryTimeEngine {

	boolean DEBUG = false;

	ArrayList<String> lobbyChat;
	HashMap<String, User> usersInLobby;
	HashMap<String, User> onlineUsers;
	HashMap<String, Room> lobbyRooms;
	HashMap<String, InGameRoom> gameRooms;
	private final Logger logger = Logger.getLogger("Engine");

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

	/**
	 * @param gameRoom
	 * @return The phrase that was chosen, or null (if there weren't any phrases to choose from)
	 */
	public String chooseRandomPhrase(InGameRoom gameRoom) {
		ArrayList<String> phrases = new ArrayList<String>();
		for (User user : gameRoom.users) {
			if (user.getPhrasesPerRound().size() != 0) {
				phrases.add(user.getPhrasesPerRound().get(getIndexOfNewestPhraseFromHistory(user)));
			}
		}
		if (phrases.size() != 0) {
			Collections.shuffle(phrases);
			return phrases.get(0);
		} else {
			logger.log(Level.SEVERE, "Server: A random phrase could not be chosen for room: " + gameRoom.getDomain().getName()
					+ " because there were no phrases to choose from (error)");
			return null;
		}
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
			user.getPhrasesPerRound().clear();
		}
	}

	public void setPassword(String roomName, User host, String password) {
		Room lobbyRoom = lobbyRooms.get(roomName);
		if (lobbyRoom.host.getUsername().equalsIgnoreCase(host.getUsername())) {
			lobbyRoom.setPassword(password);
		}
	}

	/**Activates the LetterAdditionAttack on the specified user. Edits and adds a new phrase to the user's phrase list for this round. The edited
	 * phrase will serve as this user's current phrase.
	 * @param victim The user under attack
	 * @param additionAttack The LetterAdditionAttack that contains the attack information
	 * @return The user's current phrase, after it has been subject to this attack
	 */
	public String activateLetterAdditon(User victim, LetterAdditionAttack additionAttack) {
		int newestPhraseFromHistory = getIndexOfNewestPhraseFromHistory(victim);
		String phraseToBeEdited = victim.getPhrasesPerRound().get(newestPhraseFromHistory);
		String phraseToLookFor = additionAttack.getAfterThis();
		String phraseToAddIn = additionAttack.getAddThis();
		String editedPhrase = "";

		for (int x = 0; x < phraseToBeEdited.length(); x++) {
			editedPhrase += phraseToBeEdited.charAt(x);
			if (editedPhrase.length() >= phraseToLookFor.length()) {
				String possibleCombo = phraseToBeEdited.substring(x - phraseToLookFor.length() + 1, x + 1);
				if (DEBUG)
					System.out.println("Comparing: '" + possibleCombo + "' to: " + phraseToLookFor);
				if (possibleCombo.equalsIgnoreCase(phraseToLookFor)) {
					if (DEBUG)
						System.out.println("Found match: '" + possibleCombo + "' and '" + phraseToLookFor + "'");
					editedPhrase += phraseToAddIn;
					if (DEBUG)
						System.out.println("Total string: " + editedPhrase);
				}
			}
		}
		victim.getPhrasesPerRound().add(editedPhrase);
		return editedPhrase;
	}

	public String activateLetterSubstitution(User victim, LetterSubstitutionAttack substitutionAttack) {
		int newestPhraseFromHistory = getIndexOfNewestPhraseFromHistory(victim);
		String phraseToBeEdited = victim.getPhrasesPerRound().get(newestPhraseFromHistory);
		String phraseToLookFor = substitutionAttack.getPhraseToLookFor();
		String phraseToSwapFor = substitutionAttack.getPhraseToSwapFor();
		String editedPhrase = phraseToBeEdited.replaceAll(phraseToLookFor, phraseToSwapFor);
		victim.getPhrasesPerRound().add(editedPhrase);
		return editedPhrase;
	}

	public String activateLetterRemoval(User victim, LetterRemovalAttack removalAttack) {
		int newestPhraseFromHistory = getIndexOfNewestPhraseFromHistory(victim);
		String phraseToBeEdited = victim.getPhrasesPerRound().get(newestPhraseFromHistory);
		String phraseToRemove = removalAttack.getPhraseToRemove();
		String editedPhrase = phraseToBeEdited.replace(phraseToRemove, "");
		victim.getPhrasesPerRound().add(editedPhrase);
		return editedPhrase;
	}

	public User getUserThatOwnsThisPhrase(String phrase, User somePersonInTheRoom) {
		for (User user : somePersonInTheRoom.getIngameRoom().getUsers()) {
			int newestPhraseFromHistoryIndex = user.getPhrasesPerRound().size() - 1;
			if (user.getPhrasesPerRound().get(newestPhraseFromHistoryIndex).equalsIgnoreCase(phrase)) {
				return user;
			}
		}
		logger.log(Level.FINEST, "Engine: No user was found to have a phrase that matched: " + phrase);
		return null;
	}

	public Integer getIndexOfNewestPhraseFromHistory(User user) {
		int newestPhraseFromHistoryIndex = user.getPhrasesPerRound().size() - 1;
		if (newestPhraseFromHistoryIndex == -1) {
			return 0;
		} else {
			return newestPhraseFromHistoryIndex;
		}
	}
}
