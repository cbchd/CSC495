package com.storytime.client.lobby;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

public class UpdateLobbyMessagesEvent implements Event {

    private static final long serialVersionUID = 1L;
    public static Domain domain = DomainFactory.getDomain("Lobby");
    public String message = "";

}
