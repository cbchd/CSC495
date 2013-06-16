package com.storytime.client.presenters;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.presenters.LobbyPresenter.Display;

public class SpellCustomizationPresenter implements Presenter {

	private final StoryTimeServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public interface Display {
		Widget asWidget();
	}

	public SpellCustomizationPresenter(StoryTimeServiceAsync rpcService, HandlerManager eventBus, Display display) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		System.out.println("Client: Trying to initialize the spell customization page");
		container.add(display.asWidget());
	}

}
