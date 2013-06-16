package com.storytime.client.presenters;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.presenters.GameInProgressRoomPresenter.Display;

public class HostRoomPresenter implements Presenter {

	private final StoryTimeServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	
	public HostRoomPresenter(StoryTimeServiceAsync rpcService, HandlerManager eventBus, Display display) {
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
		System.out.println("Client: Trying to initialize the host room window");
		container.add(display.asWidget());
	}

}
