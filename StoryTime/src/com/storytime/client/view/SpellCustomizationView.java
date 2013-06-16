package com.storytime.client.view;

import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpellCustomizationView extends Composite implements com.storytime.client.presenters.SpellCustomizationPresenter.Display {
	VerticalPanel verticalPanel = new VerticalPanel();
	Label lblMagicalMagic = new Label("Magical Magic");
	Label lblSpellBooks = new Label("Spell Books");
	TabBar tabBar = new TabBar();
	Grid grid = new Grid(3, 5);
	Label lblName = new Label("Name");
	Label lblType = new Label("Type");
	Label lblMagicWords = new Label("Magic Words");
	Label lblCost = new Label("Cost");
	Label lblTempname = new Label("Temp Name");
	Label lblTempType = new Label("Temp Type");
	Label lblTempWords = new Label("Temp Words");
	Label lblTempCost = new Label("Temp Cost");
	Button btnRemoveSpell = new Button("Remove Spell");
	TextBox nameOfSpellTextBox = new TextBox();
	ValueListBox valueListBox = new ValueListBox(IntegerRenderer.instance());
	TextBox magicWordsTextBox = new TextBox();
	Label lblCostbasedontype = new Label("CostBasedOnType");
	Button btnAddSpell = new Button("Add Spell");

	public SpellCustomizationView() {
		verticalPanel.setStyleName("SpellCustomizationPage");
		initWidget(verticalPanel);
		setPanelOrder();
		setCharacteristics();
		// RootPanel rootPanel = RootPanel.get();
		// rootPanel.add(verticalPanel, 10, 10);
	}
	
	public void setCharacteristics() {
		verticalPanel.setBorderWidth(1);
		verticalPanel.setSize("752px", "620px");

		lblMagicalMagic.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		lblMagicalMagic.setHeight("60px");

		lblSpellBooks.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		verticalPanel.setCellVerticalAlignment(tabBar, HasVerticalAlignment.ALIGN_BOTTOM);
		
		tabBar.setHeight("59px");

		grid.setCellPadding(2);
		grid.setBorderWidth(1);
		grid.setSize("100%", "468px");
		grid.setWidget(0, 0, lblName);
		grid.setWidget(0, 1, lblType);
		grid.setWidget(0, 2, lblMagicWords);
		grid.setWidget(0, 3, lblCost);

		lblTempname.setStyleName("spell-page-label");
		grid.setWidget(1, 0, lblTempname);

		lblTempType.setStyleName("spell-page-label");
		grid.setWidget(1, 1, lblTempType);

		lblTempWords.setStyleName("spell-page-label");
		grid.setWidget(1, 2, lblTempWords);

		lblTempCost.setStyleName("spell-page-label");
		grid.setWidget(1, 3, lblTempCost);

		btnRemoveSpell.setStyleName("gwt-LoginExistingButton");
		grid.setWidget(1, 4, btnRemoveSpell);
		btnRemoveSpell.setWidth("75%");

		nameOfSpellTextBox.setAlignment(TextAlignment.LEFT);
		grid.setWidget(2, 0, nameOfSpellTextBox);
		nameOfSpellTextBox.setWidth("90%");

		grid.setWidget(2, 1, valueListBox);
		valueListBox.setWidth("100%");

		grid.setWidget(2, 2, magicWordsTextBox);
		magicWordsTextBox.setWidth("90%");

		lblCostbasedontype.setStyleName("spell-page-label");
		grid.setWidget(2, 3, lblCostbasedontype);

		btnAddSpell.setStyleName("gwt-LoginExistingButton");
		grid.setWidget(2, 4, btnAddSpell);
		btnAddSpell.setWidth("75%");
	}
	public void setPanelOrder() {
		verticalPanel.add(lblMagicalMagic);
		verticalPanel.add(lblSpellBooks);
		verticalPanel.add(tabBar);
		tabBar.addTab("Book 1");
		tabBar.addTab("Book 2");
		tabBar.addTab("Book 3");
		verticalPanel.add(grid);		
	}

	public Widget asWidget() {
		return this;
	}
}
