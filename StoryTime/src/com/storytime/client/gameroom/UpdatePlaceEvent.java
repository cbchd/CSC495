package com.storytime.client.gameroom;

import java.util.HashMap;

import de.novanic.eventservice.client.event.Event;

public class UpdatePlaceEvent implements Event {

	private static final long serialVersionUID = 1L;
	HashMap<String, Integer> placesList = new HashMap<String, Integer>();

	public HashMap<String, Integer> getPlacesList() {
		return placesList;
	}

	public void setPlacesList(HashMap<String, Integer> placesList) {
		this.placesList = placesList;
	}

}
