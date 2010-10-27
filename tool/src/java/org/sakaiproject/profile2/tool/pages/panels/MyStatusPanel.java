package org.sakaiproject.profile2.tool.pages.panels;


import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.sakaiproject.profile2.logic.ProfileLogic;
import org.sakaiproject.profile2.logic.SakaiProxy;
import org.sakaiproject.profile2.model.UserProfile;
import org.sakaiproject.profile2.tool.Locator;
import org.sakaiproject.profile2.tool.components.ProfileStatusRenderer;
import org.sakaiproject.profile2.tool.models.SimpleText;
import org.sakaiproject.profile2.util.ProfileConstants;

public class MyStatusPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(MyStatusPanel.class);
    private transient SakaiProxy sakaiProxy;
    private transient ProfileLogic profileLogic;
    private ProfileStatusRenderer status;
    
    //get default text that fills the textField
	String defaultStatus = new ResourceModel("text.no.status", "Say something").getObject().toString();

	public MyStatusPanel(String id, UserProfile userProfile) {
		super(id);
		
		log.debug("MyStatusPanel()");

		//get API's
		sakaiProxy = getSakaiProxy();
		profileLogic = getProfileLogic();
				
		//get info
		final String displayName = userProfile.getDisplayName();
		final String userId = userProfile.getUserUuid();
		final String currentUserId = sakaiProxy.getCurrentUserId();
		
		//if superUser and proxied, can't update
		boolean editable = true;
		if(sakaiProxy.isSuperUserAndProxiedToUser(userId)) {
			editable = false;
		}
		
		
		
		//name
		Label profileName = new Label("profileName", displayName);
		add(profileName);
		
		//status component
		status = new ProfileStatusRenderer("status", userId, currentUserId, null, "tiny");
		status.setOutputMarkupId(true);
		add(status);
		
		WebMarkupContainer statusFormContainer = new WebMarkupContainer("statusFormContainer");
		
				
		//setup SimpleText object to back the single form field 
		SimpleText simpleText = new SimpleText();
				
		//status form
		Form form = new Form("form", new Model(simpleText));
		form.setOutputMarkupId(true);
        		
		//status field
		final TextField statusField = new TextField("message", new PropertyModel(simpleText, "text"));
        statusField.setOutputMarkupPlaceholderTag(true);
        form.add(statusField);
        
        //link the status textfield field with the focus/blur function via this dynamic js 
		StringHeaderContributor statusJavascript = new StringHeaderContributor(
				"<script type=\"text/javascript\">" +
					"$(document).ready( function(){" +
					"autoFill('#" + statusField.getMarkupId() + "', '" + defaultStatus + "');" +
					"countChars('#" + statusField.getMarkupId() + "');" +
					"});" +
				"</script>");
		add(statusJavascript);

        
        //clear link
		final AjaxFallbackLink clearLink = new AjaxFallbackLink("clearLink") {
			private static final long serialVersionUID = 1L;

			public void onClick(AjaxRequestTarget target) {
				//clear status, hide and repaint
				if(profileLogic.clearUserStatus(userId)) {
					status.setVisible(false); //hide status
					this.setVisible(false); //hide clear link
					target.addComponent(status);
					target.addComponent(this);
				}
			}
		};
		clearLink.setOutputMarkupPlaceholderTag(true);
		clearLink.add(new Label("clearLabel",new ResourceModel("link.status.clear")));
	
		//set visibility of clear link based on status and if it's editable
		if(!status.isVisible() || !editable) {
			clearLink.setVisible(false);
		}
		add(clearLink);
        
        
        
        
        
        //submit button
		IndicatingAjaxButton submitButton = new IndicatingAjaxButton("submit", form) {

			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form form) {
				
				//get the backing model
        		SimpleText simpleText = (SimpleText) form.getModelObject();
				
				//get userId from sakaiProxy
				String userId = sakaiProxy.getCurrentUserId();
				
				//get the status. if its the default text, do not update, although we should clear the model
				String statusMessage = StringUtils.trim(simpleText.getText());
				if(StringUtils.isBlank(statusMessage) || StringUtils.equals(statusMessage, defaultStatus)) {
					log.warn("Status for userId: " + userId + " was not updated because they didn't enter anything.");
					return;
				}

				//save status from userProfile
				if(profileLogic.setUserStatus(userId, statusMessage)) {
					log.info("Saved status for: " + userId);
					
					//post update event
					sakaiProxy.postEvent(ProfileConstants.EVENT_STATUS_UPDATE, "/profile/"+userId, true);

					//update twitter
					profileLogic.sendMessageToTwitter(userId, statusMessage);
					
					//repaint status component
					ProfileStatusRenderer newStatus = new ProfileStatusRenderer("status", userId, currentUserId, null, "tiny");
					newStatus.setOutputMarkupId(true);
					status.replaceWith(newStatus);
					newStatus.setVisible(true);
					
					//also show the clear link
					clearLink.setVisible(true);
					
					if(target != null) {
						target.addComponent(newStatus);
						target.addComponent(clearLink);
						status=newStatus; //update reference
						
						//reset the field
						target.appendJavascript("autoFill('#" + statusField.getMarkupId() + "', '" + defaultStatus + "');");
						
						//reset the counter
						target.appendJavascript("countChars('#" + statusField.getMarkupId() + "');");

					}
					
				} else {
					log.error("Couldn't save status for: " + userId);
					String js = "alert('Failed to save status. If the problem persists, contact your system administrator.');";
					target.prependJavascript(js);	
				}
				
            }
		};
		submitButton.setModel(new ResourceModel("button.sayit"));
		form.add(submitButton);
		
        //add form to container
		statusFormContainer.add(form);
		
		//if not editable, hide the entire form
		if(!editable) {
			statusFormContainer.setVisible(false);
		}
		
		
		add(statusFormContainer);
		
		
	}
	
	
	
	/* reinit for deserialisation (ie back button) */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		log.debug("MyStatusPanel has been deserialized");
		//re-init our transient objects
		profileLogic = getProfileLogic();
		sakaiProxy = getSakaiProxy();
	}
	
	private SakaiProxy getSakaiProxy() {
		return Locator.getSakaiProxy();
	}

	private ProfileLogic getProfileLogic() {
		return Locator.getProfileLogic();
	}
	
}
