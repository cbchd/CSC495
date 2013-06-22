package com.storytime.client.joinroom;

import java.io.Serializable;

public class JoinRoom implements Serializable {

	private static final long serialVersionUID = 1641648764300990204L;
	public int numberOfPlayers = 0;
	public String roomName = "";
	public int pointLimit = 0;
	public int mastersTime = 0;
	public int authorsTime = 0;
	public String theme = "";

}
