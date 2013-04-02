package com.storytime.client.presenters;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.presenters.LoginPresenter.Display;

public class LobbyPresenter implements Presenter {

	private final StoryTimeServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public interface Display {
		Widget asWidget();
	}

	public LobbyPresenter(StoryTimeServiceAsync rpcService, HandlerManager eventBus, Display display) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		System.out.println("Client: Trying to initialize the lobby window");
		container.add(display.asWidget());
	}
}