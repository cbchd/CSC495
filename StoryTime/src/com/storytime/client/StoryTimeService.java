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
import com.storytime.client.lobby.LobbyInformation;
import com.storytime.client.lobbyroom.LobbyRoomData;

@RemoteServiceRelativePath("StoryTimeService")
public interface StoryTimeService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static StoryTimeServiceAsync instance;
		public static StoryTimeServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(StoryTimeService.class);
			}
			return instance;
		}
	}
	
	public void startServer();
	
	public Boolean loginUser(String username, String password);
	
	public LobbyInformation getInitialLobbyInformation();
	
	public void sendLobbyChatMessage(String message);
	
	public void hostLobbyRoom(String roomName, String theme);
	
	public void joinRoom(String roomName);
	
	public LobbyRoomData getLobbyRoomInformation();
	
	public void updateLobbyRoomPointCap(String roomName, int pointCap);
	
	public void updateLobbyRoomTimer(String roomName, int timer);
	
	public void leaveRoom(String roomName);
	
	public void sendRoomChatMessage(String roomName, String message);
	
	public void startGame(String roomName);
	
	public GameData getGameData(String roomName);
	
	public String getMyUsername();
	
	/**
	 * @param roomName
	 * @return
	 */
	public String getStartGameChooser(String roomName);
	
	/**Sets the current user's status to "ready"
	 * If all users in the room are ready, a round start event will be fired
	 * @param username
	 * @param roomName
	 */
	public void setReady(String username, String roomName);
	
	public void submitPhrase(String phrase, String roomName);
	
	public void choosePhrase(String phrase, String roomName);
}
