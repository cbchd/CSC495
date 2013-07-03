package com.storytime.client.view;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.CheckBox;

public class GameInProgressRoomPowerMenuView {
	
	public GameInProgressRoomPowerMenuView() {
		RootPanel rp = RootPanel.get();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		//verticalPanel.setStyleName("AttackAndDefendPage");
		rp.setStyleName("AttackAndDefendPage");
		verticalPanel.setBorderWidth(0);
		rp.add(verticalPanel, 172, 124);
		verticalPanel.setSize("748px", "226px");
		Label lblAttacksDefenses = new Label("Attacks & Defenses");
		lblAttacksDefenses.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblAttacksDefenses);
		lblAttacksDefenses.setHeight("48px");
		
		DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		verticalPanel.add(decoratedTabPanel);
		decoratedTabPanel.setSize("100%", "196px");
		
		StackPanel decoratedTabPanel_1 = new StackPanel();
		decoratedTabPanel.add(decoratedTabPanel_1, "Offense", false);
		decoratedTabPanel_1.setSize("100%", "100%");
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		decoratedTabPanel_1.add(verticalPanel_3, "Letter Removal", false);
		verticalPanel_3.setSize("100%", "100%");
		
		Grid grid_2 = new Grid(1, 2);
		verticalPanel_3.add(grid_2);
		grid_2.setSize("100%", "100%");
		
		Label lblRemoveAll = new Label("Remove All:");
		grid_2.setWidget(0, 0, lblRemoveAll);
		
		TextBox removalTextBox = new TextBox();
		grid_2.setWidget(0, 1, removalTextBox);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_3.add(horizontalPanel_2);
		horizontalPanel_2.setSize("100%", "100%");
		
		ToggleButton tglbtnArmLetterRemoval = new ToggleButton("Arm");
		tglbtnArmLetterRemoval.getDownFace().setText("Armed");
		horizontalPanel_2.add(tglbtnArmLetterRemoval);
		tglbtnArmLetterRemoval.setHTML("Arm");
		verticalPanel_3.setCellHorizontalAlignment(tglbtnArmLetterRemoval, HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnArmLetterRemoval.setWidth("50%");
		
		Button btnActivate_1 = new Button("Activate");
		btnActivate_1.setStyleName("gwt-LoginAsNewUserButton");
		horizontalPanel_2.add(btnActivate_1);
		btnActivate_1.setWidth("50%");
		
		VerticalPanel verticalPanel_4 = new VerticalPanel();
		decoratedTabPanel_1.add(verticalPanel_4, "Letter Substitution", false);
		verticalPanel_4.setSize("100%", "100%");
		
		Grid grid = new Grid(1, 4);
		verticalPanel_4.add(grid);
		verticalPanel_4.setCellHorizontalAlignment(grid, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setSize("100%", "100%");
		
		Label lblSubstituteThis = new Label("Substitute This:");
		grid.setWidget(0, 0, lblSubstituteThis);
		
		TextBox substituteThisTextBox = new TextBox();
		substituteThisTextBox.setAlignment(TextAlignment.LEFT);
		grid.setWidget(0, 1, substituteThisTextBox);
		
		Label lblForThis = new Label("For This:");
		grid.setWidget(0, 2, lblForThis);
		
		TextBox substituteForThisTextBox = new TextBox();
		grid.setWidget(0, 3, substituteForThisTextBox);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		
		ToggleButton tglbtnArmLetterSubstitution = new ToggleButton("Arm");
		tglbtnArmLetterSubstitution.getDownFace().setHTML("Armed");
		verticalPanel_4.add(tglbtnArmLetterSubstitution);
		tglbtnArmLetterSubstitution.setWidth("10%");
		verticalPanel_4.setCellWidth(tglbtnArmLetterSubstitution, "100%");
		verticalPanel_4.setCellHorizontalAlignment(tglbtnArmLetterSubstitution, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel verticalPanel_5 = new VerticalPanel();
		decoratedTabPanel_1.add(verticalPanel_5, "Letter Addition", false);
		verticalPanel_5.setSize("100%", "100%");
		
		Grid grid_3 = new Grid(1, 4);
		verticalPanel_5.add(grid_3);
		grid_3.setSize("100%", "100%");
		
		Label lblAddThis = new Label("Add This:");
		lblAddThis.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_3.setWidget(0, 0, lblAddThis);
		
		TextBox letterAdditionAddThisTextBox = new TextBox();
		grid_3.setWidget(0, 1, letterAdditionAddThisTextBox);
		
		Label lblEveryTime = new Label("After This:");
		lblEveryTime.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_3.setWidget(0, 2, lblEveryTime);
		
		TextBox letterAdditionAfterThisTextBox = new TextBox();
		grid_3.setWidget(0, 3, letterAdditionAfterThisTextBox);
		
		ToggleButton tglbtnArmLetterAddition = new ToggleButton("Arm");
		tglbtnArmLetterAddition.getDownFace().setHTML("Armed");
		verticalPanel_5.add(tglbtnArmLetterAddition);
		verticalPanel_5.setCellHorizontalAlignment(tglbtnArmLetterAddition, HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnArmLetterAddition.setWidth("10%");
		
		VerticalPanel verticalPanel_6 = new VerticalPanel();
		verticalPanel_6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		decoratedTabPanel_1.add(verticalPanel_6, "Scramble", false);
		verticalPanel_6.setSize("100%", "100%");
		
		ToggleButton tglbtnArmScramble = new ToggleButton("Arm");
		tglbtnArmScramble.getDownFace().setText("Armed");
		verticalPanel_6.add(tglbtnArmScramble);
		tglbtnArmScramble.setWidth("10%");
		
		StackPanel decoratedTabPanel_2 = new StackPanel();
		decoratedTabPanel.add(decoratedTabPanel_2, "Defense", false);
		decoratedTabPanel_2.setSize("100%", "100%");
		
		VerticalPanel verticalPanel_7 = new VerticalPanel();
		decoratedTabPanel_2.add(verticalPanel_7, "Protect", false);
		verticalPanel_7.setSize("100%", "100%");
		
		Grid grid_4 = new Grid(1, 2);
		verticalPanel_7.add(grid_4);
		grid_4.setSize("100%", "100%");
		
		Label lblProtectThis = new Label("Protect This:");
		lblProtectThis.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_4.setWidget(0, 0, lblProtectThis);
		grid_4.getCellFormatter().setHeight(0, 0, "51px");
		lblProtectThis.setSize("168px", "");
		
		ListBox protectThisSubmissionComboBox = new ListBox();
		grid_4.setWidget(0, 1, protectThisSubmissionComboBox);
		protectThisSubmissionComboBox.setWidth("451px");
		
		ToggleButton tglbtnArmProtect = new ToggleButton("Arm");
		tglbtnArmProtect.getDownFace().setText("Armed");
		verticalPanel_7.add(tglbtnArmProtect);
		tglbtnArmProtect.setWidth("10%");
		verticalPanel_7.setCellHorizontalAlignment(tglbtnArmProtect, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel verticalPanel_8 = new VerticalPanel();
		decoratedTabPanel_2.add(verticalPanel_8, "Cleanse", false);
		verticalPanel_8.setSize("100%", "100%");
		
		Grid grid_5 = new Grid(1, 2);
		verticalPanel_8.add(grid_5);
		grid_5.setSize("100%", "100%");
		
		Label lblNeutralizeTheAttack = new Label("Cleanse Submission:");
		grid_5.setWidget(0, 0, lblNeutralizeTheAttack);
		grid_5.getCellFormatter().setHeight(0, 0, "51px");
		
		ListBox cleanseSubmissionComboBox = new ListBox();
		grid_5.setWidget(0, 1, cleanseSubmissionComboBox);
		cleanseSubmissionComboBox.setWidth("367px");
		
		ToggleButton tglbtnArmCleanse = new ToggleButton("Arm");
		tglbtnArmCleanse.getDownFace().setHTML("Armed");
		verticalPanel_8.add(tglbtnArmCleanse);
		tglbtnArmCleanse.setWidth("10%");
		verticalPanel_8.setCellHorizontalAlignment(tglbtnArmCleanse, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_8.setCellWidth(tglbtnArmCleanse, "100%");
		
		VerticalPanel verticalPanel_9 = new VerticalPanel();
		decoratedTabPanel_2.add(verticalPanel_9, "Trap [Counter]", false);
		verticalPanel_9.setSize("100%", "100%");
		
		Grid grid_6 = new Grid(1, 2);
		verticalPanel_9.add(grid_6);
		grid_6.setSize("100%", "100%");
		
		Label lblTrapForThis = new Label("Trap For This Attack Type:");
		lblTrapForThis.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_6.setWidget(0, 0, lblTrapForThis);
		grid_6.getCellFormatter().setHeight(0, 0, "51px");
		lblTrapForThis.setHeight("");
		
		ListBox trapForThisAttackTypeComboBox = new ListBox();
		grid_6.setWidget(0, 1, trapForThisAttackTypeComboBox);
		trapForThisAttackTypeComboBox.setWidth("268px");
		
		ToggleButton tglbtnArmTrap = new ToggleButton("Arm");
		tglbtnArmTrap.getDownFace().setText("Armed");
		verticalPanel_9.add(tglbtnArmTrap);
		verticalPanel_9.setCellHorizontalAlignment(tglbtnArmTrap, HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnArmTrap.setSize("10%", "100%");
		
		VerticalPanel verticalPanel_11 = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel_11, "Activate", false);
		verticalPanel_11.setSize("100%", "100%");
		
		Grid grid_1 = new Grid(8, 3);
		verticalPanel_11.add(grid_1);
		grid_1.setSize("100%", "100%");
		grid_1.setBorderWidth(0);
		
		Label lblRandomName = new Label("Random Name");
		lblRandomName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(1, 1, lblRandomName);
		lblRandomName.setWidth("100%");
		
		CheckBox checkBox = new CheckBox("");
		grid_1.setWidget(1, 2, checkBox);
		
		Label lblRandomMessage = new Label("Random Message");
		grid_1.setWidget(2, 1, lblRandomMessage);
		lblRandomMessage.setWidth("100%");
		
		CheckBox checkBox_1 = new CheckBox("");
		grid_1.setWidget(2, 2, checkBox_1);
		
		Label lblName = new Label("Attack Name");
		lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(3, 1, lblName);
		lblName.setWidth("100%");
		
		TextBox nameTextBox = new TextBox();
		grid_1.setWidget(3, 2, nameTextBox);
		nameTextBox.setWidth("62%");
		
		Label lblWords = new Label("Message");
		lblWords.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(4, 1, lblWords);
		lblWords.setWidth("100%");
		
		TextBox messageTextBox = new TextBox();
		grid_1.setWidget(4, 2, messageTextBox);
		grid_1.getCellFormatter().setWidth(4, 2, "");
		messageTextBox.setWidth("62%");
		
		Label lblCost = new Label("Total Cost");
		lblCost.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(5, 1, lblCost);
		lblCost.setWidth("100%");
		
		Label lblTotalCost = new Label("");
		grid_1.setWidget(5, 2, lblTotalCost);
		
		Label lblTarget = new Label("Target");
		lblTarget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.setWidget(6, 1, lblTarget);
		lblTarget.setWidth("100%");
		
		ListBox targetComboBox = new ListBox();
		grid_1.setWidget(6, 2, targetComboBox);
		targetComboBox.setWidth("246px");
		grid_1.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		Label lblPreview = new Label("Preview");
		grid_1.setWidget(7, 1, lblPreview);
		grid_1.getCellFormatter().setHeight(7, 1, "");
		lblPreview.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		lblPreview.setSize("100%", "100%");
		
		TextBox textBox = new TextBox();
		grid_1.setWidget(7, 2, textBox);
		textBox.setWidth("62%");
		grid_1.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_11.add(horizontalPanel);
		horizontalPanel.setSize("100%", "100%");
		
		Button btnResetDisarm = new Button("Reset & Disarm");
		horizontalPanel.add(btnResetDisarm);
		btnResetDisarm.setStyleName("gwt-LoginAsNewUserButton");
		
		Button btnActivate = new Button("Activate");
		horizontalPanel.add(btnActivate);
		btnActivate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		btnActivate.setStyleName("gwt-LoginAsNewUserButton");
		btnActivate.setWidth("132px");
		
		Button btnSave = new Button("Save");
		horizontalPanel.add(btnSave);
		btnSave.setStyleName("gwt-LoginAsNewUserButton");
		btnSave.setWidth("132px");
	}
}
