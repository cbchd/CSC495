package com.storytime.client.mobile;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.animation.AnimationHelper;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;



public class MGWTEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MGWT.applySettings(MGWTSettings.getAppSetting());
		AnimationHelper animationHelper = new AnimationHelper();
		RootPanel.get().add(animationHelper, 0, 0);
		animationHelper.setSize("580px", "440px");
		LayoutPanel layoutPanel = new LayoutPanel();
		Button button = new Button("Hello mgwt");
		layoutPanel.add(button);
		animationHelper.goTo(layoutPanel, Animation.SLIDE);
	}

	
}
