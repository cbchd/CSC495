package com.storytime.client.presenters;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeServiceAsync;

public class GameInProgressRoomPowerMenuPresenter implements Presenter {
	private final StoryTimeServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	
	public GameInProgressRoomPowerMenuPresenter(StoryTimeServiceAsync rpcService, HandlerManager eventBus, Display display) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
	}
	
	public interface Display {
		Widget asWidget();
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		System.out.println("Client: Trying to initialize the game in progress room window");
		container.add(display.asWidget());
	}
}
