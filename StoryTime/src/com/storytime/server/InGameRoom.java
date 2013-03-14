package com.storytime.server;

import java.util.ArrayList;
import java.util.HashMap;

import de.novanic.eventservice.client.event.domain.Domain;

public class InGameRoom {
    private static final long serialVersionUID = 1L;
    public String theme;
    public int pointCap = 0;
    public int timer = 0;
    public boolean inGame;
    public ArrayList<User> users;
    public String message;
    public Domain domain;
    public ArrayList<String> messages;
    public HashMap<User, Integer> scoreList;
    public int turnCounter = 0;
    public int phrasesSubmitted = 0;
    public ArrayList<String> story;
    public boolean gameEnded = false;
	
    public InGameRoom() {
	users = new ArrayList<User>();
	messages = new ArrayList<String>();
	scoreList = new HashMap<User, Integer>();
	story = new ArrayList<String>();
    }
}
