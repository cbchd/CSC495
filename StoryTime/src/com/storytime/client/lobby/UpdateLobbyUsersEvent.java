package com.storytime.client.lobby;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

public class UpdateLobbyUsersEvent implements Event {

    private static final long serialVersionUID = 1L;
    String username = "";
    Domain domain = DomainFactory.getDomain("Lobby");
    
    public String getUsername() {
        return username;
    }
    public Domain getDomain() {
        return domain;
    }
    public void setDomain(Domain domain) {
        this.domain = domain;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
