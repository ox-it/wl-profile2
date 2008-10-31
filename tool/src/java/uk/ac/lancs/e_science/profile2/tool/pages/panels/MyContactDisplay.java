package uk.ac.lancs.e_science.profile2.tool.pages.panels;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import uk.ac.lancs.e_science.profile2.tool.models.UserProfile;

public class MyContactDisplay extends Panel {
	
	private transient Logger log = Logger.getLogger(MyInfoDisplay.class);
	
	private String nickname = null;
	private Date dateOfBirth = null;
	private String birthday = null;

	
	public MyContactDisplay(String id, final IModel userProfileModel) {
		super(id, userProfileModel);
		
		//this panel stuff
		final Component thisPanel = this;
		final String thisPanelId = "myContact"; //wicket:id not markupId
		
		//get userProfile from userProfileModel
		UserProfile userProfile = (UserProfile) this.getModelObject();
		
		//get info from userProfile since we need to validate it and turn things off if not set.
		//otherwise we could just use a propertymodel
		String email = userProfile.getEmail();
		String homepage = userProfile.getHomepage();
		String workphone = userProfile.getWorkphone();
		String homephone = userProfile.getHomephone();
		String mobilephone = userProfile.getMobilephone();


		//heading
		add(new Label("heading", new ResourceModel("heading.contact")));
		
		//nickname
		WebMarkupContainer emailContainer = new WebMarkupContainer("emailContainer");
		emailContainer.add(new Label("emailLabel", new ResourceModel("profile.email")));
		emailContainer.add(new Label("email", email));
		add(emailContainer);
		if("".equals(email) || email == null) {
			emailContainer.setVisible(false);
		}
		
		//homepage
		WebMarkupContainer homepageContainer = new WebMarkupContainer("homepageContainer");
		homepageContainer.add(new Label("homepageLabel", new ResourceModel("profile.homepage")));
		homepageContainer.add(new Label("homepage", homepage));
		add(homepageContainer);
		if("".equals(homepage) || homepage == null) {
			homepageContainer.setVisible(false);
		}
		
		//work phone
		WebMarkupContainer workphoneContainer = new WebMarkupContainer("workphoneContainer");
		workphoneContainer.add(new Label("workphoneLabel", new ResourceModel("profile.phone.work")));
		workphoneContainer.add(new Label("workphone", workphone));
		add(workphoneContainer);
		if("".equals(workphone) || workphone == null) {
			workphoneContainer.setVisible(false);
		}
		
		//home phone
		WebMarkupContainer homephoneContainer = new WebMarkupContainer("homephoneContainer");
		homephoneContainer.add(new Label("homephoneLabel", new ResourceModel("profile.phone.home")));
		homephoneContainer.add(new Label("homephone", homephone));
		add(homephoneContainer);
		if("".equals(homephone) || homephone == null) {
			homephoneContainer.setVisible(false);
		}
		
		//mobile phone
		WebMarkupContainer mobilephoneContainer = new WebMarkupContainer("mobilephoneContainer");
		mobilephoneContainer.add(new Label("mobilephoneLabel", new ResourceModel("profile.phone.mobile")));
		mobilephoneContainer.add(new Label("mobilephone", mobilephone));
		add(mobilephoneContainer);
		if("".equals(mobilephone) || mobilephone == null) {
			mobilephoneContainer.setVisible(false);
		}
			
		
		
		
		//edit button
		AjaxFallbackLink editButton = new AjaxFallbackLink("editButton", new ResourceModel("button.edit")) {
			public void onClick(AjaxRequestTarget target) {
				Component newPanel = new MyContactEdit(thisPanelId, userProfileModel);
				newPanel.setOutputMarkupId(true);
				thisPanel.replaceWith(newPanel);
				if(target != null) {
					target.addComponent(newPanel);
				}
			}
						
		};
		editButton.setOutputMarkupId(true);
		add(editButton);
		
	}
	
}
