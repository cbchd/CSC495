package com.storytime.client.lobby;

import java.util.ArrayList;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

public class UpdateLobbyRoomsEvent implements Event {

    private static final long serialVersionUID = 1L;
    public static Domain domain = DomainFactory.getDomain("Lobby"); 
    public String roomName = "";
    public String theme = "";
    public String password = "";
    public int numberOfPlayers = 0;
    
    public UpdateLobbyRoomsEvent() {
    }
}
