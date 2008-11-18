package uk.ac.lancs.e_science.profile2.tool.pages.panels.views;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;


public class TestPanelFullReplace2 extends Panel {
	
	
	public TestPanelFullReplace2(String id) {
		super(id);
		
		final Component thisPanel = this;
		
		
		AjaxFallbackLink editButton = new AjaxFallbackLink("editButton", new Model("click me again")) {
			public void onClick(AjaxRequestTarget target) {
				/*
				Component newPanel = new TestPanelFullReplace1("fullReplace");
				newPanel.setOutputMarkupId(true);
				thisPanel.replaceWith(newPanel);
				if(target != null) {
					target.addComponent(newPanel);
				}
				*/
			}
						
		};
		editButton.setOutputMarkupId(true);
		add(editButton);
		
	}
	
}
