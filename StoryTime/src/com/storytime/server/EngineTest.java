package com.storytime.server;

public class EngineTest {
    
    static StoryTimeEngine ste;
    
    public static void main(String[] args) {
	ste = new StoryTimeEngine();
	ste.loginUser("Chad", "fev681298");
	displayUsersInLobby();
    }
    
    public static void displayUsersInLobby() {
	for (User user : ste.usersInLobby) {
	    System.out.println("User in lobby: " + user.username);
	}
    }
}
