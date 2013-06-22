/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.storytime.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.storytime.client.gameroom.GameData;
import com.storytime.client.joinroom.JoinableRoomsInformation;
import com.storytime.client.lobby.LobbyInformation;
import com.storytime.client.lobbyroom.LobbyRoomData;

@RemoteServiceRelativePath("StoryTimeService")
public interface StoryTimeService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static StoryTimeServiceAsync instance;

		public static StoryTimeServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(StoryTimeService.class);
			}
			return instance;
		}
	}

	public void startServer();

	/**Logs a user into the game
	 * @param username
	 * @param password
	 * @return true if login was successful
	 */
	public Boolean loginUser(String username, String password);

	/**
	 * @return the initial state of the lobby (lobby related information pertinent to a user who has just gotten into the lobby)
	 */
	public LobbyInformation getInitialLobbyInformation();

	/**Sends a chat message through the server, to all users in the lobby
	 * @param message
	 */
	public void sendLobbyChatMessage(String message);

	/**Hosts a lobby room for the user who activates this method, based on the theme & room name specified
	 * @param roomName
	 * @param theme
	 */
	public void hostLobbyRoom(String roomName, String theme, String password, int numberOfPlayers);

	/**
	 * When the user activates this method, the user will be put into the room
	 * specified by the room name on the server side
	 * 
	 * @param roomName
	 */
	public void joinRoom(String roomName);

	/**
	 * @return the lobby room information
	 */
	public LobbyRoomData getLobbyRoomInformation();

	/**
	 * Updates the point capacity for a specific lobby room
	 * 
	 * @param roomName
	 * @param pointCap
	 */
	public void updateLobbyRoomPointCap(String roomName, int pointCap);

	/**
	 * Updates the lobby room timer for a specific room name
	 * 
	 * @param roomName
	 * @param timer
	 */
	public void updateLobbyRoomTimer(String roomName, int timer);

	public void updateLobbyRoomChooserTimer(String roomName, int timer);
	
	public JoinableRoomsInformation getJoinableRoomsAndTheirInformation();

	public void leaveRoom(String roomName);

	public void leaveRoomAfterLocationCheck();

	public void sendRoomChatMessage(String roomName, String message);

	public void startGame(String roomName);

	public GameData getGameData(String roomName);

	/**
	 * @return username of the user who requested the information
	 */
	public String getMyUsername();

	/**
	 * @param roomName
	 * @return
	 */
	public String getStartGameChooser(String roomName);

	/**
	 * Sets the current user's status to "ready" If all users in the room are
	 * ready, a round start event will be fired
	 * 
	 * @param username
	 * @param roomName
	 */
	public void setReady(String username, String roomName);

	/**
	 * Takes a phrase that is submitted by a user and the name of the in-game
	 * room (domain) that it applies to
	 * 
	 * @param phrase
	 * @param roomName
	 */
	public void submitPhrase(String phrase, String roomName);

	/**
	 * Takes the winning phrase that is selected by a user, and the in-game room
	 * name (domain) that it applies to
	 * 
	 * @param phrase
	 * @param roomName
	 */
	public void choosePhrase(String phrase, String roomName);

	/**
	 * Takes the InGameRoom name (domain name) and the message that is to be
	 * sent to the in-game chat
	 * 
	 * @param roomName
	 * @param message
	 */
	public void sendGameRoomChatMessage(String roomName, String message);

	public String getLocation();

	/**
	 * Sets this users 'timerElapsed' boolean value to true, called after the
	 * submission or choosing submissionTimer expires to let the server know
	 * that this user is unable to submit or choose because they took longer
	 * than the time allotted for their room to act
	 */
	public void setTimerElapsed();

}
