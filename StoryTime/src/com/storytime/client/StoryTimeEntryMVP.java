package com.storytime.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


public class StoryTimeEntryMVP implements EntryPoint {

	@Override
	public void onModuleLoad() {
		//Set up the old layout
		RootPanel rootPanel = RootPanel.get("container");
		Window.setTitle("StoryMode");
		rootPanel.setStyleName("center");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		
		//Initiate the MVP pattern
		StoryTimeServiceAsync rpcService = GWT.create(StoryTimeService.class);
	    HandlerManager eventBus = new HandlerManager(null);
	    StoryTimeController storyTimeViewer = new StoryTimeController(eventBus, rpcService);
	    storyTimeViewer.go(rootPanel);
	    
	   
	}

}
