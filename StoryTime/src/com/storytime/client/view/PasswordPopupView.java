package com.storytime.client.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class PasswordPopupView extends PopupPanel {
	Label lblPassword = new Label("Enter The Password");
	HorizontalPanel horizontalPanel = new HorizontalPanel();
	TextBox textBox = new TextBox();
	Button btnEnter = new Button("Enter");
	Label lblIncorrectPassword = new Label("Incorrect Password");
	
	
	public PasswordPopupView() {
		super(true);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidget(verticalPanel);
		verticalPanel.setSize("422px", "137px");
		
		lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblPassword);
		
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("100%", "71px");
		
		horizontalPanel.add(textBox);
		textBox.setWidth("100%");
		btnEnter.setStyleName("gwt-LoginAsNewUserButton");
		horizontalPanel.add(btnEnter);
		btnEnter.setSize("100%", "27px");
		lblIncorrectPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblIncorrectPassword.setVisible(false);
		
		verticalPanel.add(lblIncorrectPassword);
		this.center();
		setGlassEnabled(true);
	}
}
