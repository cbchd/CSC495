package com.storytime.client.login;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.storytime.client.StoryTime;
import com.storytime.client.StoryTimeService;
import com.storytime.client.StoryTimeServiceAsync;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LoginPage {

    RootPanel rootPanel;
    boolean debug = true;
    boolean loginSuccessful = false;
    final StoryTimeServiceAsync storyTimeService;

    public LoginPage() {
	rootPanel = StoryTime.rootPanel;
	debug = StoryTime.DEBUG;
	storyTimeService = StoryTimeService.Util.getInstance();
    }

    /**
     * @wbp.parser.entryPoint
     */
    public void onModuleLoad() {
	rootPanel = StoryTime.rootPanel;
	rootPanel.clear();
	// rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	// rootPanel = StoryTime.rootPanel;
	// rootPanel.setStyleName("center");
	final Label lblWrongUsername = new Label("");
	rootPanel.add(lblWrongUsername, 225, 263);

	VerticalPanel verticalPanel_1 = new VerticalPanel();
	verticalPanel_1.setStyleName("LoginPanel");
	verticalPanel_1.setBorderWidth(10);
	verticalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	verticalPanel_1
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	rootPanel.add(verticalPanel_1, 10, 239);
	verticalPanel_1.setSize("597px", "285px");

	VerticalPanel verticalPanel_3 = new VerticalPanel();
	verticalPanel_1.add(verticalPanel_3);
	verticalPanel_3.setSize("100%", "25px");

	Label lblWelcomeToStorymode = new Label(
		"Welcome to StoryMode! Login Below");
	verticalPanel_3.add(lblWelcomeToStorymode);
	lblWelcomeToStorymode
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	lblWelcomeToStorymode.setWidth("100%");

	HorizontalPanel horizontalPanel = new HorizontalPanel();
	verticalPanel_1.add(horizontalPanel);
	horizontalPanel.setSize("100%", "100%");

	VerticalPanel verticalPanel_2 = new VerticalPanel();
	horizontalPanel.add(verticalPanel_2);
	verticalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	verticalPanel_2.setSize("266px", "100%");

	Label lblUsername = new Label("Username:");
	verticalPanel_2.add(lblUsername);
	verticalPanel_2.setCellHorizontalAlignment(lblUsername,
		HasHorizontalAlignment.ALIGN_CENTER);
	verticalPanel_2.setCellVerticalAlignment(lblUsername,
		HasVerticalAlignment.ALIGN_MIDDLE);
	lblUsername.setDirectionEstimator(true);
	lblUsername.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	lblUsername.setSize("100%", "100%");

	Label lblPassword = new Label("Password:");
	verticalPanel_2.add(lblPassword);
	verticalPanel_2.setCellHorizontalAlignment(lblPassword,
		HasHorizontalAlignment.ALIGN_CENTER);
	verticalPanel_2.setCellVerticalAlignment(lblPassword,
		HasVerticalAlignment.ALIGN_MIDDLE);
	lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	lblPassword.setSize("100%", "100%");

	VerticalPanel verticalPanel = new VerticalPanel();
	horizontalPanel.add(verticalPanel);
	verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	verticalPanel.setSize("331px", "100%");

	final TextBox usernameBox = new TextBox();
	verticalPanel.add(usernameBox);
	verticalPanel.setCellVerticalAlignment(usernameBox,
		HasVerticalAlignment.ALIGN_MIDDLE);
	usernameBox.setAlignment(TextAlignment.LEFT);
	usernameBox.setTextAlignment(TextBoxBase.ALIGN_LEFT);
	usernameBox.setSize("230px", "75%");

	final PasswordTextBox passwordBox = new PasswordTextBox();
	verticalPanel.add(passwordBox);
	verticalPanel.setCellVerticalAlignment(passwordBox,
		HasVerticalAlignment.ALIGN_MIDDLE);
	passwordBox.setAlignment(TextAlignment.LEFT);
	passwordBox.setSize("230px", "75%");

	HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
	verticalPanel_1.add(horizontalPanel_1);
	horizontalPanel_1.setSize("100%", "100%");

	VerticalPanel verticalPanel_4 = new VerticalPanel();
	verticalPanel_4
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	horizontalPanel_1.add(verticalPanel_4);
	verticalPanel_4.setSize("100%", "100%");

	// Existing user button
	Button btnSubmit = new Button("Login Existing User");
	verticalPanel_4.add(btnSubmit);
	btnSubmit.setStyleName("gwt-LoginExistingButton");
	btnSubmit.setSize("10", "34px");

	Button button = new Button("Login as New User");
	verticalPanel_4.add(button);
	button.setStyleName("gwt-LoginAsNewUserButton");
	// New user button
	button.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		lblWrongUsername.setText("");
		// Login as a new user
	    }
	});
	button.setSize("148px", "34px");

	final Label lblRatedBy = new Label("Rated #1 By OnlineGamersAnonymus!");
	verticalPanel_4.add(lblRatedBy);
	lblRatedBy.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	lblRatedBy.setSize("100%", "100%");
	btnSubmit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		String username = usernameBox.getText();
		String password = passwordBox.getText();
		boolean notBlank = usernameAndPassPreCheck(username, password);
		System.out.println("Please enter a username or a password");
		if (notBlank) {
		    storyTimeService.loginUser(username, password,
			    new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
				    // TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(Boolean result) {
				    // TODO Auto-generated method stub
				    if (result) {
					System.out
						.println("Client: Successfully logged in");
					StoryTime.controller("Lobby");
				    } else {
					System.out
						.println("Client: Was not logged in");
				    }
				}

			    });
		}
	    }
	});
	final ArrayList<String> funnyQuotes = new ArrayList<String>();
	funnyQuotes.add("Rated #1 By OnlineGamersAnonymus!");
	funnyQuotes.add("This game is the shiz-nat! - Steve Jobs");
	funnyQuotes.add("It's like I've reclaimed my childhood! - Bill Gates");
	Timer timer = new Timer() {
	    int count = 0;

	    @Override
	    public void run() {
		if (count != funnyQuotes.size()) {
		    lblRatedBy.setText(funnyQuotes.get(count));
		    count++;
		} else {
		    this.cancel();
		}
	    }

	};
	timer.scheduleRepeating(4000);
    }

    public void setSession() {
    }

    private boolean usernameAndPassPreCheck(String username, String password) {
	if (!(username.equalsIgnoreCase("") && password.equalsIgnoreCase(""))) {
	    return true;
	} else {
	    return false;
	}
    }
}