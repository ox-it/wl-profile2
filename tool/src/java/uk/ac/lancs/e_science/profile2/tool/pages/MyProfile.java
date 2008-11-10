package uk.ac.lancs.e_science.profile2.tool.pages;


import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.sakaiproject.api.common.edu.person.SakaiPerson;

import uk.ac.lancs.e_science.profile2.tool.models.UserProfile;
import uk.ac.lancs.e_science.profile2.tool.pages.panels.ChangeProfilePicture;
import uk.ac.lancs.e_science.profile2.tool.pages.panels.MyContactDisplay;
import uk.ac.lancs.e_science.profile2.tool.pages.panels.MyInfoDisplay;
import uk.ac.lancs.e_science.profile2.tool.pages.panels.MyStatusPanel;


public class MyProfile extends BasePage {

	private transient Logger log = Logger.getLogger(MyProfile.class);
	private static final String UNAVAILABLE_IMAGE = "images/no_image.gif";
	private transient byte[] pictureBytes;
	private final ChangeProfilePicture changePicture;

	
	public MyProfile() {
		
		if(log.isDebugEnabled()) log.debug("MyProfile()");
		
		//add the feedback panel for any error messages
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		add(feedbackPanel);
		feedbackPanel.setVisible(false); //hide by default

		//get current user
		String userId = sakaiProxy.getCurrentUserId();

		//get SakaiPerson for this user
		SakaiPerson sakaiPerson = sakaiProxy.getSakaiPerson(userId);
		//if null, create one.
		if(sakaiPerson == null) {
			log.warn("No SakaiPerson for " + userId + ". Creating one.");
			sakaiPerson = sakaiProxy.createSakaiPerson(userId);
			//if its still null, log an error - ideally, don't proceed.
			if(sakaiPerson == null) {
				log.error("Couldn't create a SakaiPerson for " + userId + ". Given up.");
			}
		} 
		
		
		//get some values from SakaiPerson or SakaiProxy if empty
		//SakaiPerson returns NULL strings if value is not set, not blank ones
	
		String userDisplayName = sakaiPerson.getDisplayName();
		if(userDisplayName == null) {
			log.info("userDisplayName for " + userId + " was null in SakaiPerson. Using UDP value.");
			userDisplayName = sakaiProxy.getUserDisplayName(userId);
		}
		
		String userEmail = sakaiPerson.getMail();
		if(userEmail == null) {
			log.info("userEmail for " + userId + " was null in SakaiPerson. Using UDP value.");
			userEmail = sakaiProxy.getUserEmail(userId);
		}
		
		//create instance of the UserProfile class
		UserProfile userProfile = new UserProfile();
		
		//configure userProfile as the model for our page
		//we then pass the userProfileModel in the constructor to the child panels
		CompoundPropertyModel userProfileModel = new CompoundPropertyModel(userProfile);
		
		//get rest of values from SakaiPerson and set into UserProfile
		userProfile.setNickname(sakaiPerson.getNickname());
		userProfile.setDateOfBirth(sakaiPerson.getDateOfBirth());
		userProfile.setDisplayName(userDisplayName);
		userProfile.setEmail(userEmail);
		userProfile.setHomepage(sakaiPerson.getLabeledURI());
		userProfile.setHomephone(sakaiPerson.getHomePhone());
		userProfile.setWorkphone(sakaiPerson.getTelephoneNumber());
		userProfile.setMobilephone(sakaiPerson.getMobile());
		
		
		
		//get photo and add to page, otherwise add default image
		pictureBytes = sakaiPerson.getJpegPhoto();
		
		if(pictureBytes != null && pictureBytes.length > 0){
		
			BufferedDynamicImageResource photoResource = new BufferedDynamicImageResource(){
				protected byte[] getImageData() {
					return pictureBytes;
				}
			};
		
			add(new Image("photo",photoResource));
		} else {
			log.info("No photo for " + userId + ". Using blank image.");
			add(new ContextImage("photo",new Model(UNAVAILABLE_IMAGE)));
		}
		
		//change picture panel
		changePicture = new ChangeProfilePicture("changePicture", userProfile);
		changePicture.setOutputMarkupPlaceholderTag(true);
		//changePicture.setOutputMarkupId(true);
		changePicture.setVisible(false);
		add(changePicture);
		
		//change profile image button
		AjaxFallbackLink changePictureLink = new AjaxFallbackLink("changePictureLink", new ResourceModel("link.change.profile.picture")) {
			public void onClick(AjaxRequestTarget target) {
				
				//add the full changePicture component to the page dynamically
				target.addComponent(changePicture);
				changePicture.setVisible(true);
				String js = "$('#" + changePicture.getMarkupId() + "').fadeIn('slow');";
				target.appendJavascript(js);

				
				//when the editImageButton is clicked, show the panel
				//its possible this will push the content lower than the iframe, so make sure the iframe size is good.
				//String js = "$('#" + changePicture.getMarkupId() + "').slideToggle()";
				//target.appendJavascript(js);
				//target.appendJavascript("alert('" + changePicture.getMarkupId() + "')");
				
			}
						
		};
		add(changePictureLink);
		
		//dynamic javascript
		StringBuilder js = new StringBuilder();
		//js.append("<script type=\"text/javascript\">");
		//js.append("$(document).ready(function(){");
		//js.append("$('#" + changePicture.getMarkupId() +"').hide();");
		//js.append("});");
		//js.append("</script>");	
					
		Label dynamicJavascript = new Label("dynamicJavascript", js.toString());
		dynamicJavascript.setEscapeModelStrings(false);
		add(dynamicJavascript);
		
		//status panel
		Panel myStatusPanel = new MyStatusPanel("myStatusPanel", userProfile);
		add(myStatusPanel);
		
		//info panel - load the display version by default
		Panel myInfoDisplay = new MyInfoDisplay("myInfo", userProfile);
		myInfoDisplay.setOutputMarkupId(true);
		add(myInfoDisplay);
		
		//contact panel - load the display version by default
		Panel myContactDisplay = new MyContactDisplay("myContact", userProfile);
		myContactDisplay.setOutputMarkupId(true);
		add(myContactDisplay);
		

		
	}
	
		
	
	
	
}
