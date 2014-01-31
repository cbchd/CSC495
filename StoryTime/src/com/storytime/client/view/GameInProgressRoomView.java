package com.storytime.client.view;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.StoryTimeEntryMVP;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.gameroom.AllPhrasesSubmittedEvent;
import com.storytime.client.gameroom.GameData;
import com.storytime.client.gameroom.GameEndEvent;
import com.storytime.client.gameroom.PhraseChosenEvent;
import com.storytime.client.gameroom.PhraseSubmittedEvent;
import com.storytime.client.gameroom.RoundCloseEvent;
import com.storytime.client.gameroom.RoundStartEvent;
import com.storytime.client.gameroom.UpdateGameRoomChatWindowEvent;
import com.storytime.client.gameroom.UpdatePlaceEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class GameInProgressRoomView extends Composite implements com.storytime.client.presenters.GameInProgressRoomPresenter.Display {

	boolean DEBUG = true;
	GameData gameData = new GameData();
	StoryTimeServiceAsync storyTimeService = StoryTimeEntryMVP.rpcService;
	RemoteEventService theRemoteEventService = StoryTimeEntryMVP.theRemoteEventService;
	boolean choosing = false;
	int turnPosition = 0;

	Label lblTitle_1 = new Label("Story");
	TextArea storyBox = new TextArea();
	Label lblChat = new Label("Chat");
	Label lblUsers_1 = new Label("Users");
	TextArea chatArea = new TextArea();
	TextBox sendToChatBox = new TextBox();
	Button btnSendToChatButton = new Button("Send");
	ListBox phraseListBox = new ListBox();
	Label label = new Label("My Score");
	Label lblCurrentChooserName = new Label("");
	Label lblPointsNeededTo = new Label("Max Score");
	Label lblScoreBox = new Label("0");
	Label lblCurrentChooser = new Label("Story Master");
	Label lblMaxPoints = new Label("0");
	Button btnChoosePhrase = new Button("Choose");
	ListBox userListBox = new ListBox();
	Button btnSubmitPhrase = new Button("Submit");
	TextBox phraseSubmitBox = new TextBox();
	Label lblSubmit = new Label("Enter a Phrase");
	Label lblTimeLeft = new Label("Time Left");
	Label lblTime = new Label("");
	Label lblSubmitAPhrase = new Label("Your Phrase");
	int timeLeftChoose = gameData.mastersTimer;
	int timeLeftSubmission = gameData.submissionTimer;
	boolean okToDisableChoosing = true;
	boolean okToDisableSubmitting = true;

	VerticalPanel overallVerticalPanel = new VerticalPanel();
	HorizontalPanel bigMiddlePanel = new HorizontalPanel();
	VerticalPanel usersAndChatLeftMiddlePanel = new VerticalPanel();
	HorizontalPanel chatSendBoxAndButtonPanel = new HorizontalPanel();
	VerticalPanel swapHolderPanelAndSubmittedPhrases = new VerticalPanel();
	VerticalPanel choosePanelForSwap = new VerticalPanel();
	HorizontalPanel underLabelsScoreChooserAndPointsPanel = new HorizontalPanel();
	HorizontalPanel scoresAndUsernamePanel = new HorizontalPanel();
	HorizontalPanel labelsScoreChooserAndPointsPanel = new HorizontalPanel();
	Label lblSubmittedPhrases = new Label("Submitted Phrases");
	Label lblSelectAPhrase = new Label("Choose The Winning Phrase");
	VerticalPanel verticalPanel_5 = new VerticalPanel();
	VerticalPanel timePanel = new VerticalPanel();
	HorizontalPanel phraseSubmissionPanelForSwap = new HorizontalPanel();
	VerticalPanel phraseSubmissionRealPanel = new VerticalPanel();
	GameInProgressRoomPowerMenuView playsPopup = new GameInProgressRoomPowerMenuView();

	Timer submitTimer = new Timer() {

		@Override
		public void run() {
			if (timeLeftSubmission == 0) {
				submitTimer.cancel();
				lblTime.setText("0");
				if (okToDisableSubmitting) {
					if (DEBUG)
						System.out.println("Client: " + gameData.thisUser + " has not submitted a word within the time limit of: " + gameData.submissionTimer
								+ " seconds");
					disableSubmitting();
					if (DEBUG)
						System.out.println("Client: Disabled submitting");
					timerHasElapsed();
				}
			} else {
				lblTime.setText(timeLeftSubmission + "");
			}
			timeLeftSubmission--;
		}
	};

	Timer chooseTimer = new Timer() {

		@Override
		public void run() {
			if (DEBUG)
				System.out.println("Client: Chooser timer for client: " + gameData.thisUser + " is at: " + timeLeftChoose);
			if (DEBUG)
				System.out.println("Client: The total choose time is: " + gameData.mastersTimer);

			if (timeLeftChoose == 0) {
				chooseTimer.cancel();
				lblTime.setText("0");
				if (okToDisableChoosing) {
					if (DEBUG)
						System.out.println("Client: " + gameData.thisUser + " has not chosen a word within the time limit of: " + gameData.mastersTimer * 1000);
					disableChoosing();
					if (DEBUG)
						System.out.println("Client: Disabled choosing");
					timerHasElapsed();
				}
			} else {
				lblTime.setText(timeLeftChoose + "");
			}
			timeLeftChoose--;
		}
	};

	Timer readyTimer = new Timer() {
		public void run() {
			setReady();
			if (DEBUG)
				System.out.println("Setting the client as ready.. after 5 seconds");
			this.cancel();
		}
	};
	private final VerticalPanel verticalPanel = new VerticalPanel();
	// private final Button btnPlays = new Button("Plays");
	private final HorizontalPanel horizontalPanel = new HorizontalPanel();
	private final Label lblPlace = new Label("Place: 0");

	public GameInProgressRoomView() {
		// RootPanel rootPanel = RootPanel.get();
		// rootPanel.add(overallVerticalPanel);
		//initialize();
		//enableChoosing();
		// enableSubmitting();
		theRemoteEventService.removeListeners();
		overallVerticalPanel.setStyleName("GameInProgressPage");
		initWidget(overallVerticalPanel);
		if (DEBUG)
			System.out.println("Initializing the game room");
		// gameData.domain = LobbyRoomView.roomData.getDomain();
		// gameData.pointCap = LobbyRoomView.roomData.getPointCap();
		// gameData.users = LobbyRoomView.roomData.getUsers();
		// gameData.submissionTimer = LobbyRoomView.roomData.getAuthorsTimer();
		// gameData.mastersTimer = LobbyRoomView.roomData.getMastersTimer();
		// gameData.theme = LobbyRoomView.roomData.getTheme();
		// gameData.messages = new ArrayList<String>();
		getAndSetGameData();
	}

	public void initialize() {
		deactivateExtraneousListeners();
		for (String user : gameData.users) {
			if (user.equalsIgnoreCase(gameData.thisUser)) {
				lblScoreBox.setText(Integer.toString(gameData.scoreList.get(gameData.thisUser)));
				if (DEBUG)
					System.out.println("Client: Set his own score to: " + lblScoreBox.getText());
			}
			if (user.length() > 25) {
				userListBox.addItem(user.substring(0, 25));
			} else {
				userListBox.addItem(user);
			}
		}
		setPanelOrder();
		setCharacteristics();
		setHandlers();
		setRemoteEventHandlersAndHandleEvents();
		readyTimer.schedule(5000);
	}

	public void setPanelOrder() {

		overallVerticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("100%", "46px");
		overallVerticalPanel.add(storyBox);
		overallVerticalPanel.add(bigMiddlePanel);

		usersAndChatLeftMiddlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		bigMiddlePanel.add(usersAndChatLeftMiddlePanel);

		usersAndChatLeftMiddlePanel.add(lblUsers_1);
		usersAndChatLeftMiddlePanel.add(userListBox);
		usersAndChatLeftMiddlePanel.add(lblChat);
		chatArea.setStyleName("chat-TextArea");
		usersAndChatLeftMiddlePanel.add(chatArea);
		chatSendBoxAndButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		usersAndChatLeftMiddlePanel.add(chatSendBoxAndButtonPanel);

		chatSendBoxAndButtonPanel.add(sendToChatBox);

		chatSendBoxAndButtonPanel.add(btnSendToChatButton);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		usersAndChatLeftMiddlePanel.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		// btnPlays.setStyleName("gwt-LoginAsNewUserButton");

		// verticalPanel.add(btnPlays);
		// btnPlays.setWidth("70%");

		bigMiddlePanel.add(swapHolderPanelAndSubmittedPhrases);

		swapHolderPanelAndSubmittedPhrases.add(lblSubmittedPhrases);
		swapHolderPanelAndSubmittedPhrases.add(phraseListBox);

		timePanel.add(lblTimeLeft);
		timePanel.add(lblTime);
	}

	public void setCharacteristics() {
		chatArea.setReadOnly(true);
		storyBox.setReadOnly(true);

		overallVerticalPanel.setBorderWidth(1);

		storyBox.setAlignment(TextAlignment.LEFT);
		storyBox.setTextAlignment(TextBoxBase.ALIGN_LEFT);

		userListBox.setVisibleItemCount(5);

		overallVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		lblMaxPoints.setText(Integer.toString(gameData.pointCap));

		overallVerticalPanel.setSize("900px", "633px");
		horizontalPanel.add(lblTitle_1);

		lblTitle_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		lblTitle_1.setSize("479px", "46px");

		horizontalPanel.add(lblPlace);
		lblPlace.setSize("100%", "100%");
		storyBox.setSize("899px", "156px");
		bigMiddlePanel.setSize("100%", "346px");

		usersAndChatLeftMiddlePanel.setSize("100%", "100%");

		lblUsers_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblUsers_1.setSize("100%", "46px");
		userListBox.setSize("95%", "146px");

		lblChat.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblChat.setSize("100%", "46px");
		chatArea.setSize("94%", "125px");
		chatSendBoxAndButtonPanel.setSize("100%", "45px");
		sendToChatBox.setSize("91%", "21px");

		btnSendToChatButton.setText("Send");
		btnSendToChatButton.setStyleName("gwt-LoginExistingButton");
		btnSendToChatButton.setSize("90%", "30px");

		swapHolderPanelAndSubmittedPhrases.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		swapHolderPanelAndSubmittedPhrases.setSize("100%", "403px");

		lblSubmittedPhrases.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblSubmittedPhrases.setSize("100%", "37px");
		phraseListBox.setSize("100%", "155px");
		phraseListBox.setVisibleItemCount(5);

		choosePanelForSwap.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		choosePanelForSwap.setSize("100%", "70px");

		lblSelectAPhrase.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		choosePanelForSwap.add(lblSelectAPhrase);
		choosePanelForSwap.add(btnChoosePhrase);

		overallVerticalPanel.add(labelsScoreChooserAndPointsPanel);

		labelsScoreChooserAndPointsPanel.add(underLabelsScoreChooserAndPointsPanel);

		underLabelsScoreChooserAndPointsPanel.add(label);

		overallVerticalPanel.add(scoresAndUsernamePanel);
		underLabelsScoreChooserAndPointsPanel.add(lblCurrentChooser);
		lblCurrentChooser.setSize("224px", "27px");

		lblCurrentChooser.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		underLabelsScoreChooserAndPointsPanel.add(lblPointsNeededTo);

		scoresAndUsernamePanel.add(lblScoreBox);
		scoresAndUsernamePanel.add(lblCurrentChooserName);
		lblCurrentChooserName.setSize("545px", "27px");
		lblCurrentChooserName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		scoresAndUsernamePanel.add(lblMaxPoints);
		lblSelectAPhrase.setSize("100%", "27px");

		btnChoosePhrase.setStyleName("gwt-LoginExistingButton");
		btnChoosePhrase.setSize("126px", "30px");

		labelsScoreChooserAndPointsPanel.setSize("100%", "100%");

		underLabelsScoreChooserAndPointsPanel.setSize("100%", "100%");

		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		label.setSize("208px", "100%");

		lblPointsNeededTo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		lblPointsNeededTo.setSize("100%", "100%");

		scoresAndUsernamePanel.setSize("100%", "100%");

		lblScoreBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lblScoreBox.setSize("152px", "100%");

		lblMaxPoints.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		lblMaxPoints.setSize("197px", "100%");

		VerticalPanel phraseSubmissionRealPanel = new VerticalPanel();
		// rootPanel.add(phraseSubmissionRealPanel, 86, 697);
		phraseSubmissionRealPanel.setSize("100%", "100%");

		lblSubmitAPhrase.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		phraseSubmissionRealPanel.add(lblSubmitAPhrase);
		phraseSubmissionRealPanel.add(phraseSubmissionPanelForSwap);

		phraseSubmissionPanelForSwap.add(phraseSubmitBox);
		phraseSubmissionPanelForSwap.add(btnSubmitPhrase);

		phraseSubmissionPanelForSwap.setSize("675px", "48px");
		phraseSubmitBox.setWidth("541px");

		btnSubmitPhrase.setText("Submit");
		btnSubmitPhrase.setStyleName("gwt-LoginExistingButton");
		btnSubmitPhrase.setSize("103px", "30px");

		lblTime.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}

	public void setHandlers() {
		btnChoosePhrase.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String chosenPhrase = null;
				if (phraseListBox.getSelectedIndex() != -1) {
					chosenPhrase = phraseListBox.getItemText(phraseListBox.getSelectedIndex());
				}
				if (chosenPhrase != null && !chosenPhrase.equalsIgnoreCase("")) {
					storyTimeService.choosePhrase(chosenPhrase, gameData.domain.getName(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Got confirmation of phrase chosen call");
							disableChoosing();
							chooseTimer.cancel();
						}
					});
				}
			}
		});

		phraseSubmitBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (!phraseSubmitBox.getText().equalsIgnoreCase("")) {
						storyTimeService.submitPhrase(phraseSubmitBox.getText(), gameData.domain.getName(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Void result) {
								if (DEBUG)
									System.out.println("Client: Got confirmation of phrase submission");
								phraseSubmitBox.setText("");
								disableSubmitting();
								submitTimer.cancel();
							}
						});
					}
				}
			}
		});

		btnSubmitPhrase.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!phraseSubmitBox.getText().equalsIgnoreCase("")) {
					storyTimeService.submitPhrase(phraseSubmitBox.getText(), gameData.domain.getName(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Got confirmation of phrase submission");
							phraseSubmitBox.setText("");
							disableSubmitting();
							submitTimer.cancel();
						}
					});
				}
			}
		});

		sendToChatBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					final String message = sendToChatBox.getText();
					if (!message.equalsIgnoreCase("")) {
						if (DEBUG)
							System.out.println("Client: Trying to send message: '" + message + "' to the server, for the domain: " + gameData.domain.getName());
						storyTimeService.sendGameRoomChatMessage(gameData.domain.getName(), message, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								if (DEBUG)
									System.out.println("Client: Failed to send the in-game chat message: '" + message + "', to the server");
							}

							@Override
							public void onSuccess(Void result) {
								sendToChatBox.setText("");
								if (DEBUG)
									System.out.println("Client: Successfully sent the message: " + message);
							}

						});
					}
				}
			}
		});

		btnSendToChatButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final String message = sendToChatBox.getText();
				if (!message.equalsIgnoreCase("")) {
					if (DEBUG)
						System.out.println("Client: Trying to send message: '" + message + "' to the server, for the domain: " + gameData.domain.getName());
					storyTimeService.sendGameRoomChatMessage(gameData.domain.getName(), message, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							if (DEBUG)
								System.out.println("Client: Failed to send the message '" + message + "' to the server, for the domain: "
										+ gameData.domain.getName());
						}

						@Override
						public void onSuccess(Void result) {
							sendToChatBox.setText("");
							if (DEBUG)
								System.out.println("Client: Successfully sent the message: " + message + " to the server, for the domain: "
										+ gameData.domain.getName());
						}
					});
				}
			}
		});

		// btnPlays.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		//
		// playsPopup.setPopupPositionAndShow(new PopupPanel.PositionCallback()
		// {
		//
		// @Override
		// public void setPosition(int offsetWidth, int offsetHeight) {
		// playsPopup.setGlassEnabled(false);
		// playsPopup.center();
		// }
		//
		// });
		//
		// }
		// });
	}

	public void setRemoteEventHandlersAndHandleEvents() {
		theRemoteEventService.addListener(gameData.domain, new RemoteEventListener() {

			@Override
			public void apply(Event anEvent) {
				if (anEvent instanceof UpdateGameRoomChatWindowEvent) {
					UpdateGameRoomChatWindowEvent chatEvent = (UpdateGameRoomChatWindowEvent) anEvent;
					onChatMessage(chatEvent.message);

				} else if (anEvent instanceof RoundStartEvent) {
					RoundStartEvent roundEvent = (RoundStartEvent) anEvent;
					onRoundStart(roundEvent.chooser);

				} else if (anEvent instanceof PhraseSubmittedEvent) {
					PhraseSubmittedEvent phraseEvent = (PhraseSubmittedEvent) anEvent;
					onPhraseRecieved(phraseEvent.phrase);

				} else if (anEvent instanceof AllPhrasesSubmittedEvent) {
					if (DEBUG)
						System.out.println("Client: Recieved All Phrases Submitted Event");
					Window.alert("All users have submitted phrases (broken)!");

				} else if (anEvent instanceof PhraseChosenEvent) {
					PhraseChosenEvent phraseChosenEvent = (PhraseChosenEvent) anEvent;
					onPhraseChosen(phraseChosenEvent.phraseChosen);

				} else if (anEvent instanceof RoundCloseEvent) {
					RoundCloseEvent roundCloseEvent = (RoundCloseEvent) anEvent;
					onRoundClose(roundCloseEvent.pointList);

				} else if (anEvent instanceof GameEndEvent) {
					GameEndEvent gameEndEvent = (GameEndEvent) anEvent;
					onGameEnd(gameEndEvent.winner);
				} else if (anEvent instanceof UpdatePlaceEvent) {
					UpdatePlaceEvent placeChangeEvent = (UpdatePlaceEvent) anEvent;
					onPlaceChange(placeChangeEvent.getPlacesList());
				}
			}
		});
	}

	public void onRoundStart(String chooser) {
		if (DEBUG)
			System.out.println("Client: Got Round Start Event");
		gameData.currentChooser = chooser;
		lblCurrentChooserName.setText(gameData.currentChooser);
		okToDisableSubmitting = true;
		okToDisableChoosing = true;

		if (DEBUG)
			System.out.println("Client: Got current chooser: " + gameData.currentChooser);
		if (DEBUG)
			System.out.println("Client: This username stored in this user's gameData object is: " + gameData.thisUser);
		if (gameData.currentChooser.equalsIgnoreCase(gameData.thisUser)) {
			if (DEBUG)
				System.out.println("Client: This user (" + gameData.thisUser + ") is currently in charge of choosing");
			choosing = true;
			lblCurrentChooserName.setText("ME!");
			enableChoosing();
			chooseTimer.scheduleRepeating(1000);
			if (DEBUG)
				System.out.println("Client: Set " + gameData.thisUser + "'s choose-timer for: " + gameData.mastersTimer * 1000 + " milli-seconds");
		} else {
			if (DEBUG)
				System.out.println("Client: User: " + gameData.thisUser + " is a submitter for this round");
			phraseSubmitBox.setText("");
			enableSubmitting();
			// set the submitTimer
			submitTimer.scheduleRepeating(1000);
			if (DEBUG)
				System.out.println("Client: Set " + gameData.thisUser + "'s submission-timer for: " + gameData.submissionTimer * 1000 + " milli-seconds");
		}
	}

	public void onPhraseRecieved(String phrase) {
		phraseListBox.addItem(phrase);
		if (DEBUG)
			System.out.println("Client: Got Phrase Submitted Event for phrase " + phrase);
	}

	public void onPhraseChosen(String phraseChosen) {
		if (DEBUG)
			System.out.println("Client: Recieved Phrase Chosen Event");
		gameData.story += phraseChosen;
		storyBox.setText(gameData.story);
		storyBox.setCursorPos(storyBox.getText().length());
		phraseListBox.clear();
		chooseTimer.cancel();
		submitTimer.cancel();
		okToDisableChoosing = false;
		okToDisableSubmitting = false;
		if (DEBUG)
			System.out.println("Client: Canceled the submission & chooser timers, since a phrase has been chosen");
	}

	public void onRoundClose(HashMap<String, Integer> pointList) {
		choosing = false;
		if (DEBUG)
			System.out.println("Client: Got Round Close Event");
		for (String user : pointList.keySet()) {
			if (user.equalsIgnoreCase(gameData.thisUser)) {
				// update this user's score display
				// score
				lblScoreBox.setText("" + pointList.get(user));
				if (DEBUG)
					System.out.println("Updated user's current score to: " + pointList.get(user));
			}
			if (DEBUG)
				System.out.print("Users in room: " + user);
		}
		disableChoosing();
		disableSubmitting();
	}

	public void onGameEnd(String winner) {
		gameData.winner = winner;
		if (DEBUG)
			System.out.println(winner + " has won the game!");
		lblCurrentChooserName.setText("");
		lblCurrentChooserName.setText(winner + " has won the game!!");
		gameData.story += "\n Authors:";
		for (String username : gameData.users) {
			gameData.story += "\n " + username;
		}
		storyBox.setText(gameData.story);
		storyBox.setCursorPos(storyBox.getText().length());
		disableChoosing();
		disableSubmitting();
	}

	public void onChatMessage(String message) {
		if (DEBUG)
			System.out.println("Client: Got UpdateGameRoomChatWindowEvent with the message '" + message + "'");
		if (chatArea.getText().equalsIgnoreCase("")) {
			chatArea.setText(chatArea.getText() + message);
		} else {
			chatArea.setText(chatArea.getText() + "\n" + message);
		}
		chatArea.setCursorPos(chatArea.getText().length());
	}

	public void onPlaceChange(HashMap<String, Integer> placeList) {
		if (DEBUG)
			System.out.println("Client: Got UpdatePlaceEvent for user: " + gameData.thisUser);
		lblPlace.setText("Place: " + (placeList.get(gameData.thisUser) + 1));
	}

	public void enableChoosing() {
		swapHolderPanelAndSubmittedPhrases.remove(phraseSubmissionPanelForSwap);
		swapHolderPanelAndSubmittedPhrases.remove(choosePanelForSwap);
		swapHolderPanelAndSubmittedPhrases.remove(timePanel);
		swapHolderPanelAndSubmittedPhrases.add(choosePanelForSwap);
		swapHolderPanelAndSubmittedPhrases.add(timePanel);
		timeLeftChoose = gameData.mastersTimer;
	}

	public void enableSubmitting() {
		swapHolderPanelAndSubmittedPhrases.remove(choosePanelForSwap);
		swapHolderPanelAndSubmittedPhrases.remove(phraseSubmissionPanelForSwap);
		swapHolderPanelAndSubmittedPhrases.remove(timePanel);
		swapHolderPanelAndSubmittedPhrases.add(phraseSubmissionPanelForSwap);
		swapHolderPanelAndSubmittedPhrases.add(timePanel);
		timeLeftSubmission = gameData.submissionTimer;
		phraseSubmitBox.setFocus(true);
	}

	public void disableChoosing() {
		swapHolderPanelAndSubmittedPhrases.remove(choosePanelForSwap);
		swapHolderPanelAndSubmittedPhrases.remove(timePanel);
		chooseTimer.cancel();
		lblTime.setText("0");
	}

	public void disableSubmitting() {
		swapHolderPanelAndSubmittedPhrases.remove(phraseSubmissionPanelForSwap);
		swapHolderPanelAndSubmittedPhrases.remove(timePanel);
		submitTimer.cancel();
		lblTime.setText("0");
	}

	public void getAndSetStartGameChooser() {
		storyTimeService.getStartGameChooser(gameData.domain.getName(), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String result) {
				if (DEBUG)
					gameData.currentChooser = result;
				System.out.println("Client: Got & set the first phrase chooser: " + result + " for user: " + gameData.currentChooser);
				if (result.equalsIgnoreCase(gameData.thisUser)) {
					choosing = true;
				}
				if (DEBUG)
					System.out.println("Client: Trying to initialize the game room view");
				// load this view
				initialize();
				if (DEBUG)
					System.out.println("Client: Game room view initialized");
			}
		});
	}

	public void getMyUsernameAndStart() {
		storyTimeService.getMyUsername(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String result) {

				if (DEBUG)
					System.out.println("Client: Got my username: " + result + " and set gamedata.thisUser");
				gameData.thisUser = result;

				if (DEBUG)
					System.out.println("Client: Trying to get and the start-of-the-game chooser's name");
				getAndSetStartGameChooser();
			}
		});
	}

	public void setReady() {
		if (DEBUG)
			System.out.println("Client: Sending the server: " + gameData.thisUser + " is ready, and resides in the " + gameData.domain.getName() + " room.");
		storyTimeService.setReady(gameData.thisUser, gameData.domain.getName(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Void result) {
				if (DEBUG)
					System.out.println("Client: Told the server that this client is ready");
			}

		});
	}

	public void timerHasElapsed() {
		storyTimeService.setTimerElapsed(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (DEBUG)
					System.out.println("Client: Failed to tell the server that " + gameData.thisUser + "'s submitTimer has elapsed");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Void result) {
				if (DEBUG)
					System.out.println("Client: Successfully told the server that " + gameData.thisUser + "'s submitTimer has elapsed");
			}
		});
	}

	public Widget asWidget() {
		return this;
	}

	public void deactivateExtraneousListeners() {
		Set<Domain> domains = theRemoteEventService.getActiveDomains();
		for (Domain domain : domains) {
			theRemoteEventService.removeListeners(domain);
		}
	}

	public void getAndSetGameData() {
		storyTimeService.getGameData(new AsyncCallback<GameData>() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Failed to retrieve game data");
			}

			@Override
			public void onSuccess(GameData result) {
				System.out.println("Got game data for room: " + result.domain.getName());
				gameData = result;
				//continue with init
				if (DEBUG)
					System.out.println("Client: Setting the game room's maximum point cap to: " + gameData.pointCap);
				if (DEBUG)
					System.out.println("Client: Domain for this in-game room is: " + gameData.domain.getName());
				for (String user : LobbyRoomView.roomData.users) {
					// initialize the user's scores to 0
					gameData.scoreList.put(user, 0);
				}
				if (DEBUG)
					System.out.println("Client: Trying to get and set this client's username and start the game");
				getMyUsernameAndStart();
			}

		});
	}
}
