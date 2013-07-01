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

public class GameInProgressRoomPowerMenuView {
	
	public GameInProgressRoomPowerMenuView() {
		RootPanel rp = RootPanel.get();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setBorderWidth(0);
		rp.add(verticalPanel, 10, 10);
		verticalPanel.setSize("748px", "388px");
		
		Label lblAttacksDefenses = new Label("Attacks & Defenses");
		lblAttacksDefenses.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblAttacksDefenses);
		
		DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		verticalPanel.add(decoratedTabPanel);
		decoratedTabPanel.setSize("100%", "196px");
		
		DecoratedTabPanel decoratedTabPanel_1 = new DecoratedTabPanel();
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
		
		TextBox textBox_4 = new TextBox();
		grid_2.setWidget(0, 1, textBox_4);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		ToggleButton tglbtnUpText = new ToggleButton("Up text");
		tglbtnUpText.getDownFace().setText("Armed");
		tglbtnUpText.setHTML("Arm");
		verticalPanel_3.add(tglbtnUpText);
		verticalPanel_3.setCellHorizontalAlignment(tglbtnUpText, HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnUpText.setWidth("10%");
		
		VerticalPanel verticalPanel_4 = new VerticalPanel();
		decoratedTabPanel_1.add(verticalPanel_4, "Letter Substitution", false);
		verticalPanel_4.setSize("100%", "100%");
		
		Grid grid = new Grid(1, 4);
		verticalPanel_4.add(grid);
		verticalPanel_4.setCellHorizontalAlignment(grid, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setSize("100%", "100%");
		
		Label lblSubstituteThis = new Label("Substitute This:");
		grid.setWidget(0, 0, lblSubstituteThis);
		
		TextBox textBox = new TextBox();
		textBox.setAlignment(TextAlignment.LEFT);
		grid.setWidget(0, 1, textBox);
		
		Label lblForThis = new Label("For This:");
		grid.setWidget(0, 2, lblForThis);
		
		TextBox textBox_1 = new TextBox();
		grid.setWidget(0, 3, textBox_1);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		
		ToggleButton tglbtnUpText_1 = new ToggleButton("Arm");
		tglbtnUpText_1.getDownFace().setHTML("Armed");
		verticalPanel_4.add(tglbtnUpText_1);
		tglbtnUpText_1.setWidth("10%");
		verticalPanel_4.setCellWidth(tglbtnUpText_1, "100%");
		verticalPanel_4.setCellHorizontalAlignment(tglbtnUpText_1, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel verticalPanel_5 = new VerticalPanel();
		decoratedTabPanel_1.add(verticalPanel_5, "Letter Addition", false);
		verticalPanel_5.setSize("100%", "100%");
		
		Grid grid_3 = new Grid(1, 4);
		verticalPanel_5.add(grid_3);
		grid_3.setSize("100%", "100%");
		
		Label lblAddThis = new Label("Add This:");
		lblAddThis.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_3.setWidget(0, 0, lblAddThis);
		
		TextBox textBox_5 = new TextBox();
		grid_3.setWidget(0, 1, textBox_5);
		
		Label lblEveryTime = new Label("After This:");
		lblEveryTime.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_3.setWidget(0, 2, lblEveryTime);
		
		TextBox textBox_6 = new TextBox();
		grid_3.setWidget(0, 3, textBox_6);
		
		ToggleButton tglbtnUpText_2 = new ToggleButton("Arm");
		tglbtnUpText_2.getDownFace().setHTML("Armed");
		verticalPanel_5.add(tglbtnUpText_2);
		verticalPanel_5.setCellHorizontalAlignment(tglbtnUpText_2, HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnUpText_2.setWidth("10%");
		
		VerticalPanel verticalPanel_6 = new VerticalPanel();
		verticalPanel_6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		decoratedTabPanel_1.add(verticalPanel_6, "Scramble", false);
		verticalPanel_6.setSize("100%", "100%");
		
		ToggleButton tglbtnArm = new ToggleButton("Arm");
		tglbtnArm.getDownFace().setText("Armed");
		verticalPanel_6.add(tglbtnArm);
		tglbtnArm.setWidth("10%");
		
		DecoratedTabPanel decoratedTabPanel_2 = new DecoratedTabPanel();
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
		lblProtectThis.setSize("168px", "51px");
		
		ListBox comboBox_1 = new ListBox();
		grid_4.setWidget(0, 1, comboBox_1);
		comboBox_1.setWidth("451px");
		
		ToggleButton tglbtnUpText_3 = new ToggleButton("Arm");
		tglbtnUpText_3.getDownFace().setText("Armed");
		verticalPanel_7.add(tglbtnUpText_3);
		tglbtnUpText_3.setWidth("10%");
		verticalPanel_7.setCellHorizontalAlignment(tglbtnUpText_3, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel verticalPanel_8 = new VerticalPanel();
		decoratedTabPanel_2.add(verticalPanel_8, "Cleanse", false);
		verticalPanel_8.setSize("100%", "100%");
		
		Grid grid_5 = new Grid(1, 2);
		verticalPanel_8.add(grid_5);
		grid_5.setSize("100%", "100%");
		
		Label lblNeutralizeTheAttack = new Label("Cleanse Submission:");
		grid_5.setWidget(0, 0, lblNeutralizeTheAttack);
		grid_5.getCellFormatter().setHeight(0, 0, "51px");
		
		ListBox comboBox_2 = new ListBox();
		grid_5.setWidget(0, 1, comboBox_2);
		comboBox_2.setWidth("367px");
		
		ToggleButton tglbtnArm_1 = new ToggleButton("Arm");
		tglbtnArm_1.getDownFace().setHTML("Armed");
		verticalPanel_8.add(tglbtnArm_1);
		tglbtnArm_1.setWidth("10%");
		verticalPanel_8.setCellHorizontalAlignment(tglbtnArm_1, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_8.setCellWidth(tglbtnArm_1, "100%");
		
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
		
		ListBox comboBox_3 = new ListBox();
		grid_6.setWidget(0, 1, comboBox_3);
		comboBox_3.setWidth("268px");
		
		ToggleButton tglbtnUpText_4 = new ToggleButton("Arm");
		tglbtnUpText_4.getDownFace().setText("Armed");
		verticalPanel_9.add(tglbtnUpText_4);
		verticalPanel_9.setCellHorizontalAlignment(tglbtnUpText_4, HasHorizontalAlignment.ALIGN_CENTER);
		tglbtnUpText_4.setSize("10%", "100%");
		
		Grid grid_1 = new Grid(4, 4);
		grid_1.setBorderWidth(0);
		verticalPanel.add(grid_1);
		grid_1.setSize("100%", "100%");
		
		Label lblName = new Label("Name:");
		grid_1.setWidget(0, 0, lblName);
		lblName.setWidth("132px");
		
		TextBox textBox_2 = new TextBox();
		grid_1.setWidget(0, 1, textBox_2);
		
		Label lblWords = new Label("Message:");
		grid_1.setWidget(0, 2, lblWords);
		
		TextBox textBox_3 = new TextBox();
		grid_1.setWidget(0, 3, textBox_3);
		
		Label lblCost = new Label("Cost:");
		grid_1.setWidget(1, 1, lblCost);
		
		Label lblTotalCost = new Label("");
		grid_1.setWidget(1, 2, lblTotalCost);
		
		Label lblTarget = new Label("Target:");
		lblTarget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_1.setWidget(2, 1, lblTarget);
		
		ListBox comboBox = new ListBox();
		grid_1.setWidget(2, 2, comboBox);
		comboBox.setWidth("100%");
		grid_1.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		verticalPanel_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_1.setWidget(3, 1, verticalPanel_2);
		verticalPanel_2.setSize("100%", "100%");
		
		Button btnNewButton_1 = new Button("Save");
		btnNewButton_1.setStyleName("gwt-LoginAsNewUserButton");
		verticalPanel_2.add(btnNewButton_1);
		btnNewButton_1.setWidth("132px");
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid_1.setWidget(3, 2, verticalPanel_1);
		verticalPanel_1.setSize("100%", "100%");
		
		Button btnNewButton = new Button("Activate");
		btnNewButton.setStyleName("gwt-LoginAsNewUserButton");
		verticalPanel_1.add(btnNewButton);
		btnNewButton.setWidth("132px");
	}
}
