package com.storytime.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.storytime.client.gameroom.GameData;
import com.storytime.client.lobby.LobbyInformation;
import com.storytime.client.lobbyroom.LobbyRoomData;

public interface StoryTimeServiceAsync {

	void startServer(AsyncCallback<Void> callback);

	void loginUser(String username, String password, AsyncCallback<Boolean> callback);

	void getInitialLobbyInformation(AsyncCallback<LobbyInformation> callback);

	void sendLobbyChatMessage(String message, AsyncCallback<Void> callback);

	void hostLobbyRoom(String roomName, String theme, AsyncCallback<Void> callback);

	void joinRoom(String roomName, AsyncCallback<Void> callback);

	void getLobbyRoomInformation(AsyncCallback<LobbyRoomData> callback);

	void updateLobbyRoomPointCap(String roomName, int pointCap, AsyncCallback<Void> callback);

	void updateLobbyRoomTimer(String roomName, int timer, AsyncCallback<Void> callback);

	void leaveRoom(String roomName, AsyncCallback<Void> callback);

	void sendRoomChatMessage(String roomName, String message, AsyncCallback<Void> callback);

	void startGame(String roomName, AsyncCallback<Void> callback);

	void getGameData(String roomName, AsyncCallback<GameData> callback);

	void getMyUsername(AsyncCallback<String> callback);

	void getStartGameChooser(String roomName, AsyncCallback<String> callback);

	void setReady(String username, String roomName, AsyncCallback<Void> callback);

	void submitPhrase(String phrase, String roomName, AsyncCallback<Void> callback);

	void choosePhrase(String phrase, String roomName, AsyncCallback<Void> callback);

	void sendGameRoomChatMessage(String roomName, String message, AsyncCallback<Void> callback);

}
