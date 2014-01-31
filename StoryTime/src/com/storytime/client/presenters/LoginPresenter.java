package com.storytime.client.presenters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.changeviewevents.LoginExistingUserLocalEvent;
import com.storytime.client.changeviewevents.LoginNewUserLocalEvent;

public class LoginPresenter implements Presenter {
	private final StoryTimeServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public interface Display {
		HasClickHandlers getLoginExistingUserButton();

		HasClickHandlers getLoginNewUserButton();

		String getUsername();

		String getPassword();

		Widget asWidget();
	}

	public LoginPresenter(StoryTimeServiceAsync rpcService, HandlerManager eventBus, Display display) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
	}

	@Override
	public void go(HasWidgets container) {
		bindHandlers();
		container.clear();
		System.out.println("Client: Bound the event handlers for the LoginPresenter & cleared the contents of the container");
		container.add(display.asWidget());
		System.out.println("Client: Added the login view to the container");
	}

	private void bindHandlers() {
		// Throw a Login Existing User Event when the button is clicked
		setLoginExistingButtonHandler();
	}

	private void setLoginExistingButtonHandler() {
		display.getLoginExistingUserButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("Client: Fired LoginExistingUserLocalEvent");
				LoginExistingUserLocalEvent existingUserLoginEvent = new LoginExistingUserLocalEvent();
				String username = display.getUsername();
				String password = display.getPassword();
				if (!username.equals("") && !password.equals("")) {
					existingUserLoginEvent.setUsername(display.getUsername());
					existingUserLoginEvent.setPassword(display.getPassword());
					rpcService.loginUser(username, password, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(Boolean result) {
							System.out.println("Client: Server authentication reply: " + result);
							if (result) {
								eventBus.fireEvent(new LoginExistingUserLocalEvent());
							} else {
								System.out.println("Client: Please enter valid login credentials");
							}
						}

					});
				} else {
					System.out.println("Client: Username or password was blank");
				}
			}
		});
		display.getLoginNewUserButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("Client: Fired LoginExistingUserLocalEvent");
				LoginNewUserLocalEvent newUserLoginEvent = new LoginNewUserLocalEvent();
				final String username = display.getUsername();
				final String password = display.getPassword();
				if (!username.equals("") && !password.equals("")) {
					newUserLoginEvent.setUsername(display.getUsername());
					newUserLoginEvent.setPassword(display.getPassword());
					rpcService.loginUser(username, password, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							System.out.println("Client: Failed to log in the new user: " + username);
						}

						@Override
						public void onSuccess(Boolean result) {
							System.out.println("Client: Server authentication reply: " + result);
							if (result) {
								eventBus.fireEvent(new LoginNewUserLocalEvent());
							} else {
								System.out.println("Client: Please enter valid login credentials");
							}
						}

					});
				} else {
					System.out.println("Client: Username or password was blank");
				}
			}
		});
	}

}
