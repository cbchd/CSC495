package com.storytime.client.view;

import java.util.Set;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeEntryMVP;

import de.novanic.eventservice.client.event.domain.Domain;

public class LoginView extends Composite implements com.storytime.client.presenters.LoginPresenter.Display {

	VerticalPanel mainVerticalPanel = new VerticalPanel();

	Label lblWelcomeToStorymode = new Label("Welcome To StoryMode!");
	VerticalPanel welcomeLabelHolder = new VerticalPanel();

	Label lblUsername = new Label("Username:");
	Label lblPassword = new Label("Password:");
	TextBox usernameBox = new TextBox();
	PasswordTextBox passwordBox = new PasswordTextBox();
	HorizontalPanel usernameAndPassLabelsAndFields = new HorizontalPanel();
	VerticalPanel usernameAndPassSubHolder = new VerticalPanel();
	VerticalPanel fieldsSubHolder = new VerticalPanel();

	Label lblRatedBy = new Label("Rated #1 By OnlineGamersAnonymus!");
	Button btnLoginNewUser = new Button("Login as New User");
	Button btnLoginExistingUser = new Button("Login Existing User");
	HorizontalPanel loginButtonHolder = new HorizontalPanel();
	VerticalPanel loginSubButtonHolder = new VerticalPanel();

	SoundController soundController = new SoundController();

	public LoginView() {
		initWidget(mainVerticalPanel);
		initPanelOrder();
		initMainVerticalPanel();
		initWelcomeHeader();
		initUsernameAndPasswordPanel();
		initButtons();
		initSounds();
	}

	public Label getLblWelcomeToStorymode() {
		return lblWelcomeToStorymode;
	}

	public void setLblWelcomeToStorymode(Label lblWelcomeToStorymode) {
		this.lblWelcomeToStorymode = lblWelcomeToStorymode;
	}

	public Button getBtnLoginNewUser() {
		return btnLoginNewUser;
	}

	public void setBtnLoginNewUser(Button btnLoginNewUser) {
		this.btnLoginNewUser = btnLoginNewUser;
	}

	public Button getBtnLoginExistingUser() {
		return btnLoginExistingUser;
	}

	public void setBtnLoginExistingUser(Button btnLoginExistingUser) {
		this.btnLoginExistingUser = btnLoginExistingUser;
	}

	public void initPanelOrder() {
		mainVerticalPanel.add(welcomeLabelHolder);
		mainVerticalPanel.add(usernameAndPassLabelsAndFields);
		mainVerticalPanel.add(usernameAndPassLabelsAndFields);
		mainVerticalPanel.add(loginButtonHolder);

		welcomeLabelHolder.add(lblWelcomeToStorymode);

		usernameAndPassLabelsAndFields.add(usernameAndPassSubHolder);
		usernameAndPassSubHolder.add(lblUsername);
		usernameAndPassSubHolder.add(lblPassword);
		usernameAndPassLabelsAndFields.add(fieldsSubHolder);

		fieldsSubHolder.add(usernameBox);
		fieldsSubHolder.add(passwordBox);

		loginButtonHolder.add(loginSubButtonHolder);
		loginSubButtonHolder.add(btnLoginExistingUser);
		loginSubButtonHolder.setCellHorizontalAlignment(btnLoginExistingUser, HasHorizontalAlignment.ALIGN_CENTER);
		loginSubButtonHolder.add(btnLoginNewUser);
		loginSubButtonHolder.setCellHorizontalAlignment(btnLoginNewUser, HasHorizontalAlignment.ALIGN_CENTER);
	}

	public void initMainVerticalPanel() {
		mainVerticalPanel.setStyleName("LoginPage");
		mainVerticalPanel.setBorderWidth(10);
		//mainVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		//mainVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainVerticalPanel.setSize("597px", "285px");
	}

	public void initWelcomeHeader() {
		lblWelcomeToStorymode.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblWelcomeToStorymode.setWidth("100%");
		lblWelcomeToStorymode.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblWelcomeToStorymode.setWidth("100%");
		welcomeLabelHolder.setSize("100%", "25px");
	}

	public void initUsernameAndPasswordPanel() {
		usernameAndPassLabelsAndFields.setSize("100%", "100%");

		usernameAndPassSubHolder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		usernameAndPassSubHolder.setSize("266px", "100%");
		usernameAndPassSubHolder.setCellHorizontalAlignment(lblUsername, HasHorizontalAlignment.ALIGN_CENTER);
		usernameAndPassSubHolder.setCellVerticalAlignment(lblUsername, HasVerticalAlignment.ALIGN_MIDDLE);
		usernameAndPassSubHolder.setCellHorizontalAlignment(lblPassword, HasHorizontalAlignment.ALIGN_CENTER);
		usernameAndPassSubHolder.setCellVerticalAlignment(lblPassword, HasVerticalAlignment.ALIGN_MIDDLE);

		fieldsSubHolder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		fieldsSubHolder.setSize("331px", "100%");
		fieldsSubHolder.setCellVerticalAlignment(usernameBox, HasVerticalAlignment.ALIGN_MIDDLE);
		fieldsSubHolder.setCellVerticalAlignment(passwordBox, HasVerticalAlignment.ALIGN_MIDDLE);

		lblUsername.setDirectionEstimator(true);
		lblUsername.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblUsername.setSize("100%", "100%");

		lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblPassword.setSize("100%", "100%");

		usernameBox.setAlignment(TextAlignment.LEFT);
		usernameBox.setTextAlignment(TextBoxBase.ALIGN_LEFT);
		usernameBox.setSize("230px", "75%");

		passwordBox.setAlignment(TextAlignment.LEFT);
		passwordBox.setSize("230px", "75%");
	}

	public void initButtons() {
		loginButtonHolder.setSize("100%", "100%");
		loginSubButtonHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		loginSubButtonHolder.setSize("100%", "100%");
		btnLoginExistingUser.setStyleName("gwt-LoginExistingButton");
		btnLoginExistingUser.setSize("10", "34px");
		btnLoginNewUser.setStyleName("gwt-LoginAsNewUserButton");
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasClickHandlers getLoginExistingUserButton() {
		return btnLoginExistingUser;
	}

	@Override
	public HasClickHandlers getLoginNewUserButton() {
		return btnLoginNewUser;
	}

	@Override
	public String getUsername() {
		return usernameBox.getText();
	}

	@Override
	public String getPassword() {
		return passwordBox.getText();
	}

	public void initSounds() {
		// Set the sounds to be played on button focus
		final Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_UNKNOWN, "buttonClick5.wav");
		
		btnLoginExistingUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sound.play();
			}
		});
	}

	public void deactivateExtraneousListeners() {
		Set<Domain> domains = StoryTimeEntryMVP.theRemoteEventService.getActiveDomains();
		for (Domain domain : domains) {
			StoryTimeEntryMVP.theRemoteEventService.removeListeners(domain);
		}
	}
}
