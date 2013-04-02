package com.storytime.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;

public class StoryTimeEntryMVP implements EntryPoint {
	public static StoryTimeServiceAsync rpcService = GWT.create(StoryTimeService.class);
	public static HandlerManager eventBus = new HandlerManager(null);
	public static RemoteEventService theRemoteEventService = RemoteEventServiceFactory.getInstance().getRemoteEventService();

	@Override
	public void onModuleLoad() {
		// Set up the old layout
		RootPanel rootPanel = RootPanel.get();
		Window.setTitle("StoryMode");
		rootPanel.setStyleName("rootPanel");

		// Initiate the MVP pattern

		StoryTimeController storyTimeViewer = new StoryTimeController(eventBus, rpcService);
		storyTimeViewer.go(rootPanel);
	}
}
