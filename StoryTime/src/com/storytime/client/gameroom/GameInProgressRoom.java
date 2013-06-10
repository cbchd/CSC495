package com.storytime.client.gameroom;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.storytime.client.StoryTimeOldEntryPoint;
import com.storytime.client.StoryTimeService;
import com.storytime.client.StoryTimeServiceAsync;
import com.storytime.client.lobbyroom.LobbyRoom;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;

public class GameInProgressRoom {

	RootPanel rootPanel;
	boolean DEBUG;
	static GameData gameData = new GameData();
	StoryTimeServiceAsync storyTimeService = StoryTimeService.Util.getInstance();
	boolean choosing = false;
	int turnPosition = 0;

	public GameInProgressRoom() {
		DEBUG = StoryTimeOldEntryPoint.DEBUG;
	}

	public void initialize() {
		if (DEBUG)
			System.out.println("Initializing the game room");
		gameData.domain = LobbyRoom.roomData.domain;
		gameData.pointCap = LobbyRoom.roomData.pointCap;
		gameData.users = LobbyRoom.roomData.users;
		gameData.timer = LobbyRoom.roomData.timer;
		gameData.theme = LobbyRoom.roomData.theme;
		gameData.messages = new ArrayList<String>();
		for (String user : LobbyRoom.roomData.users) {
			// initialize the user's scores to 0
			gameData.scoreList.put(user, 0);
		}
		getAndSetMyUsername();
		getAndSetStartGameChooser();
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
					System.out.println("Got & set the first phrase chooser: " + result);
				gameData.currentChooser = result;
				onModuleLoad();
			}
		});
	}

	public void getAndSetMyUsername() {
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
			}
		});
	}

	public void setReady() {
		if (DEBUG)
			System.out.println("Client: Sending the server: " + gameData.thisUser + " is ready, and resides in the "
					+ gameData.domain.getName() + " room.");
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

	/**
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad() {
		rootPanel = StoryTimeOldEntryPoint.rootPanel;
		System.out.println("GameInProgressRoom!");
		rootPanel.clear();

		final RemoteEventService theRemoteEventService = StoryTimeOldEntryPoint.theRemoteEventService;
		// rootPanel = RootPanel.get();
		final String username = gameData.thisUser;

		Label lblTitle = new Label(gameData.theme);
		rootPanel.add(lblTitle, 10, 10);

		final TextArea storyBox = new TextArea();
		storyBox.setTextAlignment(TextBoxBase.ALIGN_LEFT);
		storyBox.setReadOnly(true);
		rootPanel.add(storyBox, 326, 40);
		storyBox.setSize("350px", "257px");

		FlowPanel flowPanel = new FlowPanel();
		rootPanel.add(flowPanel, 92, 67);
		flowPanel.setSize("217px", "166px");

		final ListBox phraseListBox = new ListBox();
		flowPanel.add(phraseListBox);
		phraseListBox.setSize("216px", "166px");
		phraseListBox.setVisibleItemCount(5);

		Label lblWordList = new Label("Choose A Phrase");
		lblWordList.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblWordList, 92, 40);
		lblWordList.setSize("217px", "18px");

		final Button btnSubmitPhrase = new Button("Submit Phrase");
		btnSubmitPhrase.setStyleName("gwt-LoginExistingButton");

		final TextBox phraseSubmitBox = new TextBox();
		phraseSubmitBox.setVisible(false);
		phraseSubmitBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (!phraseSubmitBox.getText().equalsIgnoreCase("")) {
						phraseSubmitBox.setVisible(false);
						btnSubmitPhrase.setVisible(false);
						storyTimeService.submitPhrase(phraseSubmitBox.getText(), gameData.domain.getName(),
								new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(Void result) {
										if (DEBUG)
											System.out.println("Client: Got confirmation of phrase submission");
									}
								});
					}
				}
			}
		});
		rootPanel.add(phraseSubmitBox, 82, 309);
		phraseSubmitBox.setSize("482px", "16px");

		Label label = new Label("");
		rootPanel.add(label, 10, 309);

		// Look above the box for the initialization
		btnSubmitPhrase.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!phraseSubmitBox.getText().equalsIgnoreCase("")) {
					btnSubmitPhrase.setVisible(false);
					phraseSubmitBox.setVisible(false);
					storyTimeService.submitPhrase(phraseSubmitBox.getText(), gameData.domain.getName(),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Void result) {
									if (DEBUG)
										System.out.println("Client: Got confirmation of phrase submission");
								}
							});
				}
			}
		});
		btnSubmitPhrase.setVisible(false);
		rootPanel.add(btnSubmitPhrase, 583, 309);

		final Button btnChoosePhrase = new Button("Choose Phrase");
		btnChoosePhrase.setStyleName("gwt-LoginExistingButton");
		btnChoosePhrase.setVisible(false);
		btnChoosePhrase.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				final String chosenPhrase = phraseListBox.getItemText(phraseListBox.getSelectedIndex());
				if (!chosenPhrase.equalsIgnoreCase("")) {
					btnChoosePhrase.setVisible(false);
					storyTimeService.choosePhrase(chosenPhrase, gameData.domain.getName(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Void result) {
							if (DEBUG)
								System.out.println("Client: Got confirmation of phrase chosen call for the phrase: "
										+ chosenPhrase);
						}

					});
				}
			}
		});
		rootPanel.add(btnChoosePhrase, 139, 239);

		Label lblStory = new Label("Story");
		lblStory.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblStory, 316, 16);
		lblStory.setSize("373px", "18px");

		Label lblYourScore = new Label("Your Score:");
		rootPanel.add(lblYourScore, 10, 350);

		final Label lblScorebox = new Label("");
		rootPanel.add(lblScorebox, 82, 350);

		Label lblUsers = new Label("Users");
		lblUsers.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblUsers, 10, 43);
		lblUsers.setSize("66px", "18px");

		ListBox userListBox = new ListBox();
		rootPanel.add(userListBox, 10, 67);
		userListBox.setSize("72px", "166px");
		userListBox.setVisibleItemCount(8);

		Label lblPointsNeededTo = new Label("Points Needed To Win:");
		rootPanel.add(lblPointsNeededTo, 10, 380);

		Label lblMaxPoints = new Label("" + gameData.pointCap);
		rootPanel.add(lblMaxPoints, 150, 380);

		final Label lblCurrentChooser = new Label("Current Chooser:");
		rootPanel.add(lblCurrentChooser, 306, 380);

		final Label lblChoosername = new Label("ChooserName");
		rootPanel.add(lblChoosername, 410, 380);

		for (String user : gameData.users) {
			if (user.equalsIgnoreCase(username)) {
				lblScorebox.setText(Integer.toString(gameData.scoreList.get(username)));
				if (DEBUG)
					System.out.println("Client: Set his own score to: " + lblScorebox.getText());
			}
			userListBox.addItem(user);
		}
		lblScorebox.setText("0");
		lblChoosername.setText(gameData.currentChooser);

		final Label lblGameStartingIn = new Label("GAME STARTING IN:");
		rootPanel.add(lblGameStartingIn, 348, 436);

		final Timer submissionTimer = new Timer() {

			@Override
			public void run() {
				btnChoosePhrase.setVisible(false);
				btnSubmitPhrase.setVisible(false);
				phraseSubmitBox.setVisible(false);
				if (DEBUG)
					System.out.println("Client: This client has not submitted a word within the time limit of: "
							+ gameData.timer + " seconds");
				lblGameStartingIn.setText("Ran out of time!");
			}
		};

		theRemoteEventService.addListener(gameData.domain, new RemoteEventListener() {

			@Override
			public void apply(Event anEvent) {
				if (anEvent instanceof RoundStartEvent) {
					if (DEBUG)
						System.out.println("Client: Got Round Start Event");
					RoundStartEvent roundEvent = (RoundStartEvent) anEvent;
					gameData.currentChooser = roundEvent.chooser;
					lblChoosername.setText(gameData.currentChooser);

					if (DEBUG)
						System.out.println("Client: Got current chooser: " + gameData.currentChooser);
					if (gameData.currentChooser.equalsIgnoreCase(username)) {
						if (DEBUG)
							System.out.println("Client: This user (" + username
									+ ") is currently in charge of choosing");
						choosing = true;
						lblChoosername.setText("ME!");
						btnChoosePhrase.setVisible(false);
						btnSubmitPhrase.setVisible(false);
						phraseSubmitBox.setVisible(false);
					} else {
						btnChoosePhrase.setVisible(false);
						btnSubmitPhrase.setVisible(true);
						phraseSubmitBox.setVisible(true);
						phraseSubmitBox.setText("");
						// disableSubmissionBoxTimer.schedule(gameData.timer); //TIMER FOR
						// ROUNDS
					}
				} else if (anEvent instanceof PhraseSubmittedEvent) {
					PhraseSubmittedEvent phraseEvent = (PhraseSubmittedEvent) anEvent;
					phraseListBox.addItem(phraseEvent.phrase);
					if (choosing)
						btnChoosePhrase.setVisible(true);
					if (DEBUG)
						System.out.println("Got Phrase Submitted Event for phrase " + phraseEvent.phrase);
				} else if (anEvent instanceof AllPhrasesSubmittedEvent) {
					if (DEBUG)
						System.out.println("Client: Recieved All Phrases Submitted Event");
					Window.alert("All submitting users have submitted phrases!");
				} else if (anEvent instanceof PhraseChosenEvent) {
					PhraseChosenEvent phraseChosenEvent = (PhraseChosenEvent) anEvent;
					if (DEBUG)
						System.out.println("Client: Recieved Phrase Chosen Event");
					gameData.story += phraseChosenEvent.phraseChosen;
					storyBox.setText(gameData.story);
					phraseListBox.clear();
				} else if (anEvent instanceof RoundCloseEvent) {
					RoundCloseEvent roundCloseEvent = (RoundCloseEvent) anEvent;

					choosing = false;
					if (DEBUG)
						System.out.println("Client: Got Round Close Event");
					for (String user : roundCloseEvent.pointList.keySet()) {
						if (user.equalsIgnoreCase(username)) {
							// update this user's score display
							// score
							lblScorebox.setText("" + roundCloseEvent.pointList.get(user));
							if (DEBUG)
								System.out.println("Updated user's current score to: "
										+ roundCloseEvent.pointList.get(user));
						}
						if (DEBUG)
							System.out.print("Users in room: " + user);
					}
				} else if (anEvent instanceof GameEndEvent) {
					GameEndEvent gameEndEvent = (GameEndEvent) anEvent;
					gameData.winner = gameEndEvent.winner;
					if (DEBUG)
						System.out.println(gameData.winner + " has won the game!");
					lblChoosername.setText("");
					lblCurrentChooser.setText(gameData.winner + " has won the game!!");
					btnChoosePhrase.setVisible(false);
					btnSubmitPhrase.setVisible(false);
					phraseSubmitBox.setVisible(false);
				}
			}
		});
		Timer countdownTimer = new Timer() {
			int count = 5;

			public void run() {
				if (count == -1) {
					lblGameStartingIn.setVisible(false);
				} else if (count == 0) {
					lblGameStartingIn.setText("Game Started!");
					if (choosing) {
						btnChoosePhrase.setVisible(false);
						btnSubmitPhrase.setVisible(false);
						phraseSubmitBox.setVisible(false);
					} else {
						btnChoosePhrase.setVisible(false);
						btnSubmitPhrase.setVisible(true);
						phraseSubmitBox.setVisible(true);
					}
				} else if (count != 0 && count != -1) {
					lblGameStartingIn.setText("GAME STARTING IN: " + count);
				}
				count--;
			}
		};
		Timer timer1 = new Timer() {
			public void run() {
				setReady();
				if (DEBUG)
					System.out.println("Setting the client as ready.. after 2 seconds");
				this.cancel();
			}
		};
		timer1.schedule(3000);
		countdownTimer.scheduleRepeating(1000);

		if (DEBUG)
			System.out.println("Client: Started listeners");
	}

}
