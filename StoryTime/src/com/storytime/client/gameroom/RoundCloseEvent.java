package com.storytime.client.gameroom;

import java.util.HashMap;

import de.novanic.eventservice.client.event.Event;

public class RoundCloseEvent implements Event {

    private static final long serialVersionUID = 1L;

    public HashMap<String, Integer> pointList = new HashMap<String, Integer>();
}
