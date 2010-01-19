/**
 * Copyright (c) 2008-2010 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.sakaiproject.profile2.tool.pages.MyFriends;
import org.sakaiproject.profile2.util.ProfileConstants;
import org.sakaiproject.util.FormattedText;

public class AddFriend extends Panel {

	private static final long serialVersionUID = 1L;
	private transient SakaiProxy sakaiProxy;
	private transient ProfileLogic profileLogic;
	
	/*
	 * userX is the current user
	 * userY is the user to add
	 */
	
	public AddFriend(String id, final ModalWindow window, final FriendAction friendActionModel, final String userX, final String userY){
        super(id);

      //get API's
		sakaiProxy = getSakaiProxy();
		profileLogic = getProfileLogic();
        
        //get friendName
        final String friendName = FormattedText.processFormattedText(sakaiProxy.getUserDisplayName(userY), new StringBuffer());
        
        //window setup
		window.setTitle(new StringResourceModel("title.friend.add", null, new Object[]{ friendName } )); 
		window.setInitialHeight(150);
		window.setInitialWidth(500);
		window.setResizable(false);
		
		//privacy
		ProfilePrivacy privacy = profileLogic.getPrivacyRecordForUser(userY);
		boolean isProfileImageAllowed = profileLogic.isUserXProfileImageVisibleByUserY(userY, privacy, userX, false);
		
		//image
		add(new ProfileImageRenderer("image", userY, isProfileImageAllowed, ProfileConstants.PROFILE_IMAGE_THUMBNAIL, true));
		
        //text
		final Label text = new Label("text", new StringResourceModel("text.friend.add", null, new Object[]{ friendName } ));
        text.setEscapeModelStrings(false);
        text.setOutputMarkupId(true);
        add(text);
                   
        //setup form		
		Form form = new Form("form");
		form.setOutputMarkupId(true);
				
		//submit button
		AjaxFallbackButton submitButton = new AjaxFallbackButton("submit", new ResourceModel("button.friend.add"), form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form form) {
				
				/* double checking */
				
				//friend?
				if(profileLogic.isUserXFriendOfUserY(userX, userY)) {
					text.setDefaultModel(new StringResourceModel("error.friend.already.confirmed", null, new Object[]{ friendName } ));
					this.setEnabled(false);
					this.add(new AttributeModifier("class", true, new Model("disabled")));
					target.addComponent(text);
					target.addComponent(this);
					return;
				}
				
				//has a friend request already been made to this person?
				if(profileLogic.isFriendRequestPending(userX, userY)) {
					text.setDefaultModel(new StringResourceModel("error.friend.already.pending", null, new Object[]{ friendName } ));
					this.setEnabled(false);
					this.add(new AttributeModifier("class", true, new Model("disabled")));
					target.addComponent(text);
					target.addComponent(this);
					return;
				}
				
				//has a friend request been made from this person to the current user?
				if(profileLogic.isFriendRequestPending(userY, userX)) {
					text.setDefaultModel(new StringResourceModel("error.friend.already.pending", null, new Object[]{ friendName } ));
					this.setEnabled(false);
					this.add(new AttributeModifier("class", true, new Model("disabled")));
					target.addComponent(text);
					target.addComponent(this);
					return;
				}
				
				//if ok, request friend
				if(profileLogic.requestFriend(userX, userY)) {
					friendActionModel.setRequested(true);
					
					//post event
					sakaiProxy.postEvent(ProfileConstants.EVENT_FRIEND_REQUEST, "/profile/"+userY, true);
					
					window.close(target);
				} else {
					text.setDefaultModel(new StringResourceModel("error.friend.add.failed", null, new Object[]{ friendName } ));
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
				friendActionModel.setRequested(false);
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



