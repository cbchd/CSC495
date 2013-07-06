package com.storytime.client.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.storytime.client.skill.MultiSkill;
import com.storytime.client.skill.Skill;
import com.storytime.client.skill.offense.LetterAdditionAttack;

public class GameInProgressRoomPowerMenuView extends PopupPanel {

	boolean DEBUG = true;
	
	VerticalPanel verticalPanel = new VerticalPanel();
	Label lblAttacksDefenses = new Label("Attacks & Defenses");
	DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
	StackPanel decoratedTabPanel_1 = new StackPanel();
	VerticalPanel verticalPanel_4 = new VerticalPanel();
	Grid grid = new Grid(1, 4);

	Label lblSubstituteThis = new Label("Substitute This:");
	TextBox substituteThisTextBox = new TextBox();
	Label lblForThis = new Label("For This:");
	TextBox substituteForThisTextBox = new TextBox();
	HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
	ToggleButton tglbtnArmLetterSubstitution = new ToggleButton("Arm");
	Button btnActivate_2 = new Button("Activate");
	VerticalPanel verticalPanel_5 = new VerticalPanel();
	Grid grid_3 = new Grid(1, 4);

	Label lblAddThis = new Label("Add This:");
	TextBox letterAdditionAddThisTextBox = new TextBox();
	Label lblEveryTime = new Label("After This:");
	TextBox letterAdditionAfterThisTextBox = new TextBox();
	HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
	ToggleButton tglbtnArmLetterAddition = new ToggleButton("Arm");
	Button btnActivateLetterAddition = new Button("Activate");
	VerticalPanel verticalPanel_3 = new VerticalPanel();

	Grid grid_2 = new Grid(1, 2);
	Label lblRemoveAll = new Label("Remove All:");
	TextBox removalTextBox = new TextBox();
	HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
	ToggleButton tglbtnArmLetterRemoval = new ToggleButton("Arm");
	Button btnActivate_1 = new Button("Activate");
	StackPanel decoratedTabPanel_2 = new StackPanel();
	VerticalPanel verticalPanel_7 = new VerticalPanel();
	Grid grid_4 = new Grid(1, 2);
	Label lblProtectThis = new Label("Protect This:");
	ListBox protectThisSubmissionComboBox = new ListBox();
	VerticalPanel verticalPanel_8 = new VerticalPanel();
	Grid grid_5 = new Grid(1, 2);
	Label lblNeutralizeTheAttack = new Label("Cleanse Submission:");
	ListBox cleanseSubmissionComboBox = new ListBox();
	VerticalPanel verticalPanel_9 = new VerticalPanel();
	Grid grid_6 = new Grid(1, 2);
	Label lblTrapForThis = new Label("Trap For This Attack Type:");
	ListBox trapForThisAttackTypeComboBox = new ListBox();
	VerticalPanel verticalPanel_11 = new VerticalPanel();
	Grid grid_1 = new Grid(8, 3);
	Label lblRandomName = new Label("Random Name");
	CheckBox checkBox = new CheckBox("");
	Label lblRandomMessage = new Label("Random Message");
	CheckBox checkBox_1 = new CheckBox("");
	Label lblName = new Label("Attack Name");
	TextBox nameTextBox = new TextBox();
	Label lblWords = new Label("Message");
	TextBox messageTextBox = new TextBox();
	Label lblCost = new Label("Total Cost");
	Label lblTotalCost = new Label("");
	Label lblTarget = new Label("Target");
	ListBox targetComboBox = new ListBox();
	Label lblPreview = new Label("Preview");
	TextBox textBox = new TextBox();
	HorizontalPanel horizontalPanel = new HorizontalPanel();
	Button btnResetDisarm = new Button("Reset & Disarm");
	Button btnActivate = new Button("Activate");
	Button btnSave = new Button("Save");
	HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
	ToggleButton tglbtnArmScramble = new ToggleButton("Arm");
	Button btnActivate_4 = new Button("Activate");
	HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
	ToggleButton tglbtnArmProtect = new ToggleButton("Arm");
	Button btnActivate_5 = new Button("Activate");
	HorizontalPanel horizontalPanel_6 = new HorizontalPanel();
	ToggleButton tglbtnArmCleanse = new ToggleButton("Arm");
	Button btnActivate_6 = new Button("Activate");
	HorizontalPanel horizontalPanel_7 = new HorizontalPanel();
	ToggleButton tglbtnArmTrap = new ToggleButton("Arm");
	Button btnActivate_7 = new Button("Activate");
	private final VerticalPanel verticalPanel_1 = new VerticalPanel();
	private final Grid overallArmedSkillsGrid = new Grid(2, 4);
	private final Label lblType = new Label("Type");
	private final Label lblCost_1 = new Label("Cost");
	private final Label lblTemptype = new Label("TempType");
	private final Label lblTempcost = new Label("TempCost");
	ArrayList<Label> labelsForCurrentArmedPowersDisplay = new ArrayList<Label>();
	MultiSkill multiSkill = new MultiSkill();
	int currentRow = 0;
	int currentColumn = 0;

	int letterAdditionIndex = 0;
	int letterRemovalIndex = 1;
	int letterSubstitutionAttack = 2;
	int muteAttack = 3;

	public GameInProgressRoomPowerMenuView() {
		super(true);
		if (DEBUG) System.out.println("Client: Trying to initialize the GameInProgressRoomPowerMenuPopup");
		this.center();
		setWidget(verticalPanel);
		setGlassEnabled(false);
		setAnimationEnabled(true);
		populateCurrentArmedSkillGrid();
		setPanelOrder();
		setCharacteristics();
		setHandlers();
		
		//verticalPanel.setStyleName("AttackAndDefendPage");

		verticalPanel.add(verticalPanel_1);
		verticalPanel_1.setSize("100%", "152px");

		verticalPanel_1.add(overallArmedSkillsGrid);
		overallArmedSkillsGrid.setSize("100%", "100%");

		overallArmedSkillsGrid.setWidget(0, 1, lblType);

		overallArmedSkillsGrid.setWidget(0, 3, lblCost_1);
		lblTemptype.setStyleName("spell-page-label");

		overallArmedSkillsGrid.setWidget(1, 1, lblTemptype);
		lblTempcost.setStyleName("spell-page-label");

		overallArmedSkillsGrid.setWidget(1, 3, lblTempcost);
	}

	public void setPanelOrder() {
		verticalPanel.add(lblAttacksDefenses);
		verticalPanel.add(decoratedTabPanel);
		decoratedTabPanel.add(decoratedTabPanel_1, "Offense", false);
		verticalPanel_4.add(grid);
		horizontalPanel_1
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_1.add(tglbtnArmLetterSubstitution);
		horizontalPanel_1.add(btnActivate_2);
		verticalPanel_4.add(horizontalPanel_1);
		decoratedTabPanel_1.add(verticalPanel_5, "Letter Addition", false);
		verticalPanel_5.add(grid_3);
		horizontalPanel_3
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_3.add(tglbtnArmLetterAddition);
		verticalPanel_5.add(horizontalPanel_3);
		horizontalPanel_3.add(btnActivateLetterAddition);
		decoratedTabPanel_1.add(verticalPanel_3, "Letter Removal", false);
		verticalPanel_3.add(grid_2);
		horizontalPanel_2
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_3.add(horizontalPanel_2);
		horizontalPanel_2.add(tglbtnArmLetterRemoval);
		horizontalPanel_2.add(btnActivate_1);
		decoratedTabPanel.add(decoratedTabPanel_2, "Defense", false);
		decoratedTabPanel_2.add(verticalPanel_7, "Protect", false);
		verticalPanel_7.add(grid_4);
		decoratedTabPanel_2.add(verticalPanel_8, "Cleanse", false);
		verticalPanel_8.add(grid_5);
		decoratedTabPanel_2.add(verticalPanel_9, "Trap [Counter]", false);
		verticalPanel_9.add(grid_6);
		decoratedTabPanel.add(verticalPanel_11, "Activate", false);
		verticalPanel_11.add(grid_1);
		verticalPanel_11.add(horizontalPanel);
		horizontalPanel.add(btnResetDisarm);
		horizontalPanel.add(btnActivate);
		horizontalPanel_4
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		decoratedTabPanel_1.add(horizontalPanel_4, "Scramble", false);
		horizontalPanel.add(btnSave);
		horizontalPanel_4.add(tglbtnArmScramble);
		horizontalPanel_5
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_7.add(horizontalPanel_5);
		horizontalPanel_5.add(tglbtnArmProtect);
		horizontalPanel_4.add(btnActivate_4);
		horizontalPanel_5.add(btnActivate_5);
		horizontalPanel_6
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_8.add(horizontalPanel_6);
		horizontalPanel_6.add(tglbtnArmCleanse);
		horizontalPanel_6.add(btnActivate_6);
		horizontalPanel_7
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_9.add(horizontalPanel_7);
		horizontalPanel_7.add(tglbtnArmTrap);
		horizontalPanel_7.add(btnActivate_7);
	}

	public void setCharacteristics() {
		verticalPanel.setBorderWidth(0);
		verticalPanel.setSize("748px", "456px");
		lblAttacksDefenses
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblAttacksDefenses.setHeight("39px");

		decoratedTabPanel.setSize("100%", "196px");
		decoratedTabPanel_1.setSize("100%", "100%");
		horizontalPanel_4.setSize("100%", "100%");

		tglbtnArmScramble.getDownFace().setText("Armed");
		tglbtnArmScramble.setWidth("40%");

		btnActivate_4.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate_4.setWidth("40%");
		decoratedTabPanel_1.add(verticalPanel_4, "Letter Substitution", false);

		verticalPanel_4.setSize("100%", "100%");
		verticalPanel_4.setCellHorizontalAlignment(grid,
				HasHorizontalAlignment.ALIGN_CENTER);
		grid.setSize("100%", "100%");
		grid.setWidget(0, 0, lblSubstituteThis);
		substituteThisTextBox.setAlignment(TextAlignment.LEFT);
		grid.setWidget(0, 1, substituteThisTextBox);
		grid.setWidget(0, 2, lblForThis);
		grid.setWidget(0, 3, substituteForThisTextBox);
		grid.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_1.setSize("100%", "100%");
		tglbtnArmLetterSubstitution.getDownFace().setHTML("Armed");
		tglbtnArmLetterSubstitution.setWidth("50%");
		verticalPanel_4.setCellWidth(tglbtnArmLetterSubstitution, "100%");
		verticalPanel_4.setCellHorizontalAlignment(tglbtnArmLetterSubstitution,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnActivate_2.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate_2.setWidth("50%");

		verticalPanel_5.setSize("100%", "100%");

		grid_3.setSize("100%", "100%");

		lblAddThis.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_3.setWidget(0, 0, lblAddThis);

		grid_3.setWidget(0, 1, letterAdditionAddThisTextBox);

		lblEveryTime
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_3.setWidget(0, 2, lblEveryTime);

		grid_3.setWidget(0, 3, letterAdditionAfterThisTextBox);
		horizontalPanel_3.setSize("100%", "100%");

		tglbtnArmLetterAddition.getDownFace().setHTML("Armed");
		verticalPanel_5.setCellHorizontalAlignment(tglbtnArmLetterAddition,
				HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnArmLetterAddition.setWidth("40%");

		btnActivateLetterAddition.setStyleName("gwt-LoginAsNewUserButton");
		btnActivateLetterAddition.setWidth("40%");

		verticalPanel_3.setSize("100%", "100%");
		//
		verticalPanel_3.setCellVerticalAlignment(grid_2,
				HasVerticalAlignment.ALIGN_MIDDLE);
		grid_2.setSize("100%", "100%");

		grid_2.setWidget(0, 0, lblRemoveAll);

		grid_2.setWidget(0, 1, removalTextBox);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_2.setSize("100%", "100%");

		tglbtnArmLetterRemoval.getDownFace().setText("Armed");
		tglbtnArmLetterRemoval.setHTML("Arm");
		verticalPanel_3.setCellHorizontalAlignment(tglbtnArmLetterRemoval,
				HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnArmLetterRemoval.setWidth("50%");

		btnActivate_1.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate_1.setWidth("50%");

		decoratedTabPanel_2.setSize("100%", "100%");

		verticalPanel_7.setSize("100%", "100%");

		grid_4.setSize("100%", "100%");

		lblProtectThis
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_4.setWidget(0, 0, lblProtectThis);
		grid_4.getCellFormatter().setHeight(0, 0, "51px");
		lblProtectThis.setSize("168px", "");

		grid_4.setWidget(0, 1, protectThisSubmissionComboBox);
		protectThisSubmissionComboBox.setWidth("451px");

		verticalPanel_8.setSize("100%", "100%");

		grid_5.setSize("100%", "100%");

		grid_5.setWidget(0, 0, lblNeutralizeTheAttack);
		grid_5.getCellFormatter().setHeight(0, 0, "51px");

		grid_5.setWidget(0, 1, cleanseSubmissionComboBox);
		cleanseSubmissionComboBox.setWidth("367px");

		verticalPanel_9.setSize("100%", "100%");
		//
		grid_6.setSize("100%", "100%");

		lblTrapForThis
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_6.setWidget(0, 0, lblTrapForThis);
		grid_6.getCellFormatter().setHeight(0, 0, "51px");
		lblTrapForThis.setHeight("");

		grid_6.setWidget(0, 1, trapForThisAttackTypeComboBox);
		trapForThisAttackTypeComboBox.setWidth("268px");

		verticalPanel_11.setSize("100%", "100%");

		grid_1.setSize("100%", "100%");
		grid_1.setBorderWidth(0);

		lblRandomName
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(1, 1, lblRandomName);
		lblRandomName.setWidth("100%");

		grid_1.setWidget(1, 2, checkBox);

		grid_1.setWidget(2, 1, lblRandomMessage);
		lblRandomMessage.setWidth("100%");

		grid_1.setWidget(2, 2, checkBox_1);

		lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(3, 1, lblName);
		lblName.setWidth("100%");

		grid_1.setWidget(3, 2, nameTextBox);
		nameTextBox.setWidth("62%");

		lblWords.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(4, 1, lblWords);
		lblWords.setWidth("100%");

		grid_1.setWidget(4, 2, messageTextBox);
		grid_1.getCellFormatter().setWidth(4, 2, "");
		messageTextBox.setWidth("62%");

		lblCost.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(5, 1, lblCost);
		lblCost.setWidth("100%");

		grid_1.setWidget(5, 2, lblTotalCost);

		lblTarget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(6, 1, lblTarget);
		lblTarget.setWidth("100%");

		grid_1.setWidget(6, 2, targetComboBox);
		targetComboBox.setWidth("246px");
		grid_1.getCellFormatter().setHorizontalAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_CENTER);

		grid_1.setWidget(7, 1, lblPreview);
		grid_1.getCellFormatter().setHeight(7, 1, "");
		lblPreview.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		lblPreview.setSize("100%", "100%");

		grid_1.setWidget(7, 2, textBox);
		textBox.setWidth("62%");
		grid_1.getCellFormatter().setHorizontalAlignment(2, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setSize("100%", "100%");
		btnResetDisarm.setStyleName("gwt-LoginAsNewUserButton");

		btnActivate.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate.setWidth("132px");

		btnSave.setStyleName("gwt-LoginAsNewUserButton");
		btnSave.setWidth("132px");
		horizontalPanel_5.setSize("100%", "100%");

		tglbtnArmProtect.getDownFace().setText("Armed");
		tglbtnArmProtect.setWidth("40%");
		verticalPanel_7.setCellHorizontalAlignment(tglbtnArmProtect,
				HasHorizontalAlignment.ALIGN_CENTER);

		btnActivate_5.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate_5.setWidth("40%");
		horizontalPanel_6.setSize("100%", "100%");

		tglbtnArmCleanse.getDownFace().setHTML("Armed");
		tglbtnArmCleanse.setWidth("40%");
		verticalPanel_8.setCellHorizontalAlignment(tglbtnArmCleanse,
				HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_8.setCellWidth(tglbtnArmCleanse, "100%");

		btnActivate_6.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate_6.setWidth("40%");
		horizontalPanel_7.setSize("100%", "100%");

		tglbtnArmTrap.getDownFace().setText("Armed");
		verticalPanel_9.setCellHorizontalAlignment(tglbtnArmTrap,
				HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnArmTrap.setSize("40%", "");

		btnActivate_7.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate_7.setWidth("40%");
	}

	public void setHandlers() {
		tglbtnArmLetterAddition.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (tglbtnArmLetterAddition.isDown()) {
					armLetterAddition();
				} else  {
					disarmLetterAddition();
				}
			}
		});
	}

	private void armLetterAddition() {
		System.out.println("Client: Arming the letter addition attack");
		LetterAdditionAttack letterAdditionAttack = new LetterAdditionAttack();
		letterAdditionAttack.setName(nameTextBox.getText());
		letterAdditionAttack.setAddThis(letterAdditionAddThisTextBox.getText());
		letterAdditionAttack.setAfterThis(letterAdditionAfterThisTextBox
				.getText());
		multiSkill.skillList.add(letterAdditionIndex, letterAdditionAttack);
		populateCurrentArmedSkillGrid();
	}

	private void disarmLetterAddition() {
		System.out.println("Client: Disarming the letter addition attack");
		multiSkill.skillList.remove(letterAdditionIndex);
		populateCurrentArmedSkillGrid();
	}
	
	private void disarmLetterRemoval() {
		System.out.println("Client: Disarming the letter removal attack");
		multiSkill.skillList.remove(letterRemovalIndex);
		populateCurrentArmedSkillGrid();
	}
	
	private void disarmSubstitutionAttack() {
		System.out.println("Client: Disarming the letter substitution attack");
		multiSkill.skillList.remove(letterSubstitutionAttack);
		populateCurrentArmedSkillGrid();
	}

	private void populateCurrentArmedSkillGrid() {
		currentRow = 0;
		multiSkill.skillList.clear();
		for (Skill skill : multiSkill.skillList) {
			currentColumn = 0;
			Label type = new Label(skill.getType());
			Label cost = new Label(skill.getCost() + "");
			overallArmedSkillsGrid.setWidget(currentRow, currentColumn, type);
			currentColumn++;
			overallArmedSkillsGrid.setWidget(currentRow, currentColumn, cost);
			currentRow++;
		}
	}

}
