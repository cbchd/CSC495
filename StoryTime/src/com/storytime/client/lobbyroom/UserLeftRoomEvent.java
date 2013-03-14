package com.storytime.client.lobbyroom;

import com.storytime.server.User;

import de.novanic.eventservice.client.event.Event;

public class UserLeftRoomEvent implements Event {

    private static final long serialVersionUID = 1L;
    public String username;

}
