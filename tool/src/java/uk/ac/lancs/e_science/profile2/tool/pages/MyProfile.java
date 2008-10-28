package uk.ac.lancs.e_science.profile2.tool.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.sakaiproject.api.common.edu.person.SakaiPerson;

import uk.ac.lancs.e_science.profile2.tool.models.UserProfile;
import uk.ac.lancs.e_science.profile2.tool.pages.panels.MyInfoDisplay;
import uk.ac.lancs.e_science.profile2.tool.pages.panels.MyStatusPanel;


public class MyProfile extends BasePage {

	private transient Logger log = Logger.getLogger(MyProfile.class);
	private static final String UNAVAILABLE_IMAGE = "images/no_image.gif";
	private transient byte[] pictureBytes;

	
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
		//if this user does not have a sakaiPerson entry, get a prototype
		if(sakaiPerson == null) {
			sakaiPerson = sakaiProxy.getSakaiPersonPrototype();
		} 
		
		//get some values from SakaiPerson or SakaiProxy if empty
		//SakaiPerson returns NULL strings if value is not set, not blank ones
	
		String userDisplayName = sakaiPerson.getDisplayName();
		if(userDisplayName == null) {
			log.info("userDisplayName for " + userId + " was null in SakaiPerson");
			userDisplayName = sakaiProxy.getUserDisplayName(userId);
		}
		
		String userEmail = sakaiPerson.getMail();
		if(userEmail == null) {
			log.info("userEmail for " + userId + " was null in SakaiPerson");
			userEmail = sakaiProxy.getUserEmail(userId);
		}
		
		//create instance of the UserProfile class
		UserProfile userProfile = new UserProfile();
		
		//get rest of values from SakaiPerson and set into UserProfile
		userProfile.setNickname(sakaiPerson.getNickname());
		userProfile.setDateOfBirth(sakaiPerson.getDateOfBirth());
		userProfile.setDisplayName(userDisplayName);
		userProfile.setEmail(userEmail);

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
			add(new ContextImage("photo",new Model(UNAVAILABLE_IMAGE)));
		}
		
		//configure userProfile as the model for our page
		//we then pass the userProfileModel in the constructor to the child panels
		CompoundPropertyModel userProfileModel = new CompoundPropertyModel(userProfile);
		
		//status panel
		Panel myStatusPanel = new MyStatusPanel("myStatusPanel", userProfileModel);
		add(myStatusPanel);
		
		//info panel - load the display version by default
		Panel myInfoDisplay = new MyInfoDisplay("myInfo", userProfileModel);
		myInfoDisplay.setOutputMarkupId(true);
		add(myInfoDisplay);
		
		
	}
	
	
	
	
}
