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

package org.sakaiproject.profile2.tool.pages.panels;



import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.profile2.logic.SakaiProxy;
import org.sakaiproject.profile2.model.SocialNetworkingInfo;
import org.sakaiproject.profile2.model.UserProfile;
import org.sakaiproject.profile2.tool.Locator;
import org.sakaiproject.profile2.tool.components.TextareaTinyMceSettings;
import org.sakaiproject.profile2.util.ProfileConstants;
import org.sakaiproject.profile2.util.ProfileUtils;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.ajax.TinyMceAjaxSubmitModifier;

public class MyInterestsEdit extends Panel {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(MyInterestsEdit.class);
    private transient SakaiProxy sakaiProxy;
	
	public MyInterestsEdit(final String id, final UserProfile userProfile) {
		super(id);
		
		log.debug("MyInterestsEdit()");
		
		//get API's
		sakaiProxy = getSakaiProxy();
		
		//this panel
		final Component thisPanel = this;
			
		//get userId
		final String userId = userProfile.getUserUuid();
		
		//heading
		add(new Label("heading", new ResourceModel("heading.interests.edit")));
		
		//setup form		
		Form form = new Form("form", new Model(userProfile));
		form.setOutputMarkupId(true);
		
		//form submit feedback
		final Label formFeedback = new Label("formFeedback");
		formFeedback.setOutputMarkupPlaceholderTag(true);
		form.add(formFeedback);
		
		//add warning message if superUser and not editing own profile
		Label editWarning = new Label("editWarning");
		editWarning.setVisible(false);
		if(sakaiProxy.isSuperUserAndProxiedToUser(userId)) {
			editWarning.setDefaultModel(new StringResourceModel("text.edit.other.warning", null, new Object[]{ userProfile.getDisplayName() } ));
			editWarning.setEscapeModelStrings(false);
			editWarning.setVisible(true);
		}
		form.add(editWarning);
		
		//We don't need to get the info from userProfile, we load it into the form with a property model
	    //just make sure that the form element id's match those in the model
	   	
		//social networking
		
		//facebook
		WebMarkupContainer facebookContainer = new WebMarkupContainer("facebookContainer");
		facebookContainer.add(new Label("facebookLabel", new ResourceModel("profile.socialnetworking.facebook.edit")));
		facebookContainer.add(new TextField("facebookUsername", new PropertyModel(userProfile, "facebookUsername")));
		form.add(facebookContainer);
		
		//linkedin
		WebMarkupContainer linkedinContainer = new WebMarkupContainer("linkedinContainer");
		linkedinContainer.add(new Label("linkedinLabel", new ResourceModel("profile.socialnetworking.linkedin.edit")));
		linkedinContainer.add(new TextField("linkedinUsername", new PropertyModel(userProfile, "linkedinUsername")));
		form.add(linkedinContainer);
		
		//myspace
		WebMarkupContainer myspaceContainer = new WebMarkupContainer("myspaceContainer");
		myspaceContainer.add(new Label("myspaceLabel", new ResourceModel("profile.socialnetworking.myspace.edit")));
		myspaceContainer.add(new TextField("myspaceUsername", new PropertyModel(userProfile, "myspaceUsername")));
		form.add(myspaceContainer);
		
		//twitter
		WebMarkupContainer twitterContainer = new WebMarkupContainer("twitterContainer");
		twitterContainer.add(new Label("twitterLabel", new ResourceModel("profile.socialnetworking.twitter.edit")));
		twitterContainer.add(new TextField("twitterUsername", new PropertyModel(userProfile, "twitterUsername")));
		form.add(twitterContainer);
		
		//skype
		WebMarkupContainer skypeContainer = new WebMarkupContainer("skypeContainer");
		skypeContainer.add(new Label("skypeLabel", new ResourceModel("profile.socialnetworking.skype.edit")));
		skypeContainer.add(new TextField("skypeUsername", new PropertyModel(userProfile, "skypeUsername")));
		form.add(skypeContainer);
		
		//favourite books
		WebMarkupContainer booksContainer = new WebMarkupContainer("booksContainer");
		booksContainer.add(new Label("booksLabel", new ResourceModel("profile.favourite.books")));
		TextArea favouriteBooks = new TextArea("favouriteBooks", new PropertyModel(userProfile, "favouriteBooks"));
		booksContainer.add(favouriteBooks);
		form.add(booksContainer);
		
		//favourite tv shows
		WebMarkupContainer tvContainer = new WebMarkupContainer("tvContainer");
		tvContainer.add(new Label("tvLabel", new ResourceModel("profile.favourite.tv")));
		TextArea favouriteTvShows = new TextArea("favouriteTvShows", new PropertyModel(userProfile, "favouriteTvShows"));
		tvContainer.add(favouriteTvShows);
		form.add(tvContainer);
		
		//favourite movies
		WebMarkupContainer moviesContainer = new WebMarkupContainer("moviesContainer");
		moviesContainer.add(new Label("moviesLabel", new ResourceModel("profile.favourite.movies")));
		TextArea favouriteMovies = new TextArea("favouriteMovies", new PropertyModel(userProfile, "favouriteMovies"));
		moviesContainer.add(favouriteMovies);
		form.add(moviesContainer);
		
		//favourite quotes
		WebMarkupContainer quotesContainer = new WebMarkupContainer("quotesContainer");
		quotesContainer.add(new Label("quotesLabel", new ResourceModel("profile.favourite.quotes")));
		TextArea favouriteQuotes = new TextArea("favouriteQuotes", new PropertyModel(userProfile, "favouriteQuotes"));
		quotesContainer.add(favouriteQuotes);
		form.add(quotesContainer);
		
		//other information
		WebMarkupContainer otherContainer = new WebMarkupContainer("otherContainer");
		otherContainer.add(new Label("otherLabel", new ResourceModel("profile.other")));
		TextArea otherInformation = new TextArea("otherInformation", new PropertyModel(userProfile, "otherInformation"));
		
		//add TinyMCE control
		otherInformation.add(new TinyMceBehavior(new TextareaTinyMceSettings()));
		
		otherContainer.add(otherInformation);
		form.add(otherContainer);
		
		//submit button
		AjaxFallbackButton submitButton = new AjaxFallbackButton("submit", new ResourceModel("button.save.changes"), form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form form) {
				//save() form, show message, then load display panel
				if(save(form)) {

					//post update event
					sakaiProxy.postEvent(ProfileConstants.EVENT_PROFILE_INTERESTS_UPDATE, "/profile/"+userId, true);
					
					//repaint panel
					Component newPanel = new MyInterestsDisplay(id, userProfile);
					newPanel.setOutputMarkupId(true);
					thisPanel.replaceWith(newPanel);
					if(target != null) {
						target.addComponent(newPanel);
						//resize iframe
						target.appendJavascript("setMainFrameHeight(window.name);");
					}
				
				} else {
					//String js = "alert('Failed to save information. Contact your system administrator.');";
					//target.prependJavascript(js);
					
					formFeedback.setDefaultModel(new ResourceModel("error.profile.save.interests.failed"));
					formFeedback.add(new AttributeModifier("class", true, new Model<String>("save-failed-error")));	
					target.addComponent(formFeedback);
				}
            }
		};
		submitButton.add(new TinyMceAjaxSubmitModifier());
		form.add(submitButton);
		
        
		//cancel button
		AjaxFallbackButton cancelButton = new AjaxFallbackButton("cancel", new ResourceModel("button.cancel"), form) {
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form form) {
            	Component newPanel = new MyInterestsDisplay(id, userProfile);
				newPanel.setOutputMarkupId(true);
				thisPanel.replaceWith(newPanel);
				if(target != null) {
					target.addComponent(newPanel);
					//resize iframe
					target.appendJavascript("setMainFrameHeight(window.name);");
					//need a scrollTo action here, to scroll down the page to the section
				}
            	
            }
        };
        cancelButton.setDefaultFormProcessing(false);
        form.add(cancelButton);
		
        //feedback stuff - make this a class and insance it with diff params
        //WebMarkupContainer formFeedback = new WebMarkupContainer("formFeedback");
		//formFeedback.add(new Label("feedbackMsg", "some message"));
		//formFeedback.add(new AjaxIndicator("feedbackImg"));
		//form.add(formFeedback);
        
        
		
		//add form to page
		add(form);
		
	}
	
	//called when the form is to be saved
	private boolean save(Form form) {
		
		//get the backing model
		UserProfile userProfile = (UserProfile) form.getModelObject();
		
		//get SakaiProxy, get userId from the UserProfile (because admin could be editing), then get existing SakaiPerson for that userId
		SakaiProxy sakaiProxy = getSakaiProxy();
		
		String userId = userProfile.getUserUuid();
		SakaiPerson sakaiPerson = sakaiProxy.getSakaiPerson(userId);

		// save social networking information
		SocialNetworkingInfo socialNetworkingInfo = new SocialNetworkingInfo(userProfile.getUserUuid());
		socialNetworkingInfo.setFacebookUsername(userProfile.getFacebookUsername());
		socialNetworkingInfo.setLinkedinUsername(userProfile.getLinkedinUsername());
		socialNetworkingInfo.setMyspaceUsername(userProfile.getMyspaceUsername());
		socialNetworkingInfo.setSkypeUsername(userProfile.getSkypeUsername());
		socialNetworkingInfo.setTwitterUsername(userProfile.getTwitterUsername());
		
		if (!Locator.getProfileLogic().saveSocialNetworkingInfo(socialNetworkingInfo)) {
			return false;
		}
		
		//get values and set into SakaiPerson
		sakaiPerson.setFavouriteBooks(userProfile.getFavouriteBooks());
		sakaiPerson.setFavouriteTvShows(userProfile.getFavouriteTvShows());
		sakaiPerson.setFavouriteMovies(userProfile.getFavouriteMovies());
		sakaiPerson.setFavouriteQuotes(userProfile.getFavouriteQuotes());
		sakaiPerson.setNotes(ProfileUtils.processHtml(userProfile.getOtherInformation()));
		//update SakaiPerson
		if(sakaiProxy.updateSakaiPerson(sakaiPerson)) {
			log.info("Saved SakaiPerson for: " + userId );
			return true;
		} else {
			log.info("Couldn't save SakaiPerson for: " + userId);
			return false;
		}
	}

	/* reinit for deserialisation (ie back button) */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		log.debug("MyInterestsEdit has been deserialized.");
		//re-init our transient objects
		sakaiProxy = getSakaiProxy();
	}
	
	private SakaiProxy getSakaiProxy() {
		return Locator.getSakaiProxy();
	}

	
}
