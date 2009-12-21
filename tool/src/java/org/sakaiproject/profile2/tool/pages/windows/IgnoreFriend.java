package org.sakaiproject.profile2.tool.pages.windows;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.sakaiproject.profile2.logic.ProfileLogic;
import org.sakaiproject.profile2.logic.SakaiProxy;
import org.sakaiproject.profile2.model.ProfilePrivacy;
import org.sakaiproject.profile2.tool.Locator;
import org.sakaiproject.profile2.tool.components.FocusOnLoadBehaviour;
import org.sakaiproject.profile2.tool.components.ProfileImageRenderer;
import org.sakaiproject.profile2.tool.models.FriendAction;
import org.sakaiproject.profile2.util.ProfileConstants;
import org.sakaiproject.util.FormattedText;

public class IgnoreFriend extends Panel {

	private static final long serialVersionUID = 1L;
	private transient SakaiProxy sakaiProxy;
	private transient ProfileLogic profileLogic;

	/*
	 * userX is the current user
	 * userY is the user who's request we are ignoring
	 */
	
	public IgnoreFriend(String id, final ModalWindow window, final FriendAction friendActionModel, final String userX, final String userY){
        super(id);

        //get API's
		sakaiProxy = getSakaiProxy();
		profileLogic = getProfileLogic();
        
        //get friendName
        final String friendName = FormattedText.processFormattedText(sakaiProxy.getUserDisplayName(userY), new StringBuffer());
                
        //window setup
		window.setTitle(new ResourceModel("title.friend.ignore")); 
		window.setInitialHeight(150);
		window.setInitialWidth(500);
		window.setResizable(false);
		
		//privacy
		ProfilePrivacy privacy = profileLogic.getPrivacyRecordForUser(userY);
		boolean isProfileImageAllowed = profileLogic.isUserXProfileImageVisibleByUserY(userY, privacy, userX, false);
		
		//image
		add(new ProfileImageRenderer("image", userY, isProfileImageAllowed, ProfileConstants.PROFILE_IMAGE_THUMBNAIL, true));
		
        //text
		final Label text = new Label("text", new StringResourceModel("text.friend.ignore", null, new Object[]{ friendName } ));
        text.setEscapeModelStrings(false);
        text.setOutputMarkupId(true);
        add(text);
                   
        //setup form		
		Form form = new Form("form");
		form.setOutputMarkupId(true);
		
		//submit button
		AjaxFallbackButton submitButton = new AjaxFallbackButton("submit", new ResourceModel("button.friend.ignore"), form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form form) {
				
				/* double checking */
				
				//must exist a pending friend request FROM userY to userX in order to ignore it
				boolean friendRequestFromThisPerson = profileLogic.isFriendRequestPending(userY, userX);
				
				if(!friendRequestFromThisPerson) {
					text.setDefaultModel(new StringResourceModel("error.friend.not.pending.ignore", null, new Object[]{ friendName } ));
					this.setEnabled(false);
					this.add(new AttributeModifier("class", true, new Model("disabled")));
					target.addComponent(text);
					target.addComponent(this);
					return;
				}
				
				
				//if ok, ignore friend request
				if(profileLogic.ignoreFriendRequest(userY, userX)) {
					friendActionModel.setIgnored(true);
					
					//post event
					sakaiProxy.postEvent(ProfileConstants.EVENT_FRIEND_IGNORE, "/profile/"+userY, true);
					
					window.close(target);
				} else {
					text.setDefaultModel(new StringResourceModel("error.friend.ignore.failed", null, new Object[]{ friendName } ));
					this.setEnabled(false);
					this.add(new AttributeModifier("class", true, new Model("disabled")));
					target.addComponent(text);
					target.addComponent(this);
					return;
				}
				
            }
		};
		submitButton.add(new FocusOnLoadBehaviour());
		form.add(submitButton);
		
        
		//cancel button
		AjaxFallbackButton cancelButton = new AjaxFallbackButton("cancel", new ResourceModel("button.cancel"), form) {
            private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form form) {
				friendActionModel.setIgnored(false);
            	window.close(target);
            }
        };
        cancelButton.setDefaultFormProcessing(false);
        form.add(cancelButton);
        
        //add form
        add(form);
        
    }

	private SakaiProxy getSakaiProxy() {
		return Locator.getSakaiProxy();
	}

	private ProfileLogic getProfileLogic() {
		return Locator.getProfileLogic();
	}
	
	
	
}



