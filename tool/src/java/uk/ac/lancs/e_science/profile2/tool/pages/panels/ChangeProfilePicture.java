package uk.ac.lancs.e_science.profile2.tool.pages.panels;

import org.apache.log4j.Logger;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import uk.ac.lancs.e_science.profile2.api.Profile;
import uk.ac.lancs.e_science.profile2.api.ProfileImageManager;
import uk.ac.lancs.e_science.profile2.api.SakaiProxy;
import uk.ac.lancs.e_science.profile2.tool.ProfileApplication;
import uk.ac.lancs.e_science.profile2.tool.components.AjaxIndicator;
import uk.ac.lancs.e_science.profile2.tool.components.CloseButton;
import uk.ac.lancs.e_science.profile2.tool.components.ComponentVisualErrorBehavior;
import uk.ac.lancs.e_science.profile2.tool.components.ErrorLevelsFeedbackMessageFilter;
import uk.ac.lancs.e_science.profile2.tool.components.FeedbackLabel;
import uk.ac.lancs.e_science.profile2.tool.models.UserProfile;
import uk.ac.lancs.e_science.profile2.tool.pages.MyProfile;

public class ChangeProfilePicture extends Panel{
    
    private FileUploadField uploadField;
    private transient SakaiProxy sakaiProxy;
    private transient Profile profile;
	private transient Logger log = Logger.getLogger(ChangeProfilePicture.class);

	

	
	public ChangeProfilePicture(String id, UserProfile userProfile) {  
        super(id);  
        
		//get SakaiProxy API
		sakaiProxy = ProfileApplication.get().getSakaiProxy();
		
		//get Profile API
		profile = ProfileApplication.get().getProfile();
		   
        //setup form	
		Form form = new Form("form") {
			public void onSubmit(){
				
				//get userid and sakaiperson for this user
				String userId = sakaiProxy.getCurrentUserId();
				//SakaiPerson sakaiPerson = sakaiProxy.getSakaiPerson(userId);
				
				//get file that was uploaded
				FileUpload upload = uploadField.getFileUpload();
								
				
				if (upload == null) {
					log.error("upload was null.");
					error(new StringResourceModel("error.no.file.uploaded", this, null).getString());
				    return;
				} else if (upload.getSize() == 0) {
				    log.error("upload was empty.");
					error(new StringResourceModel("error.empty.file.uploaded", this, null).getString());
					return;
				} else if (!profile.checkContentTypeForProfileImage(upload.getContentType())) {
					log.error("invalid file type uploaded for profile picture");
					error(new StringResourceModel("error.invalid.image.type", this, null).getString());
				    return;
				} else {
					
					String mimeType = upload.getContentType();
					String fileName = upload.getClientFileName();
					
					//get bytes, set into sakaiperson directly
					byte[] imageBytes = upload.getBytes();
					
					//scale image for the main profile pic
					imageBytes = profile.scaleImage(imageBytes, ProfileImageManager.MAX_IMAGE_XY);
					 
					//save the main profile image to CHS and get the resource_id back
					String mainResourceId = sakaiProxy.getProfileImageResourcePath(userId, SakaiProxy.PROFILE_IMAGE_MAIN, fileName);
					String thumbnailResourceId = sakaiProxy.getProfileImageResourcePath(userId, SakaiProxy.PROFILE_IMAGE_THUMBNAIL, fileName);

					System.out.println("mainResourceId:" + mainResourceId);
					System.out.println("thumbnailResourceId:" + thumbnailResourceId);

					boolean result = sakaiProxy.saveFile(mainResourceId, userId, fileName, mimeType, imageBytes);
					
					//save
					if(profile.addNewProfileImage(userId, mainResourceId, thumbnailResourceId)) {
						log.info("Updated profile image");
						setResponsePage(new MyProfile()); //to refresh the image data
					} else {
						log.error("Profile image update failed");
					}
					
					 
				}
				
				
				
			}
		};
		
		//get the max upload size from Sakai
		//form.setMaxSize(Bytes.megabytes(sakaiProxy.getMaxProfilePictureSize()));	

		form.setOutputMarkupId(true);
		form.setMultiPart(true);
       
        
        //close button
        CloseButton closeButton = new CloseButton("closeButton", this);
        closeButton.setOutputMarkupId(true);
		form.add(closeButton);
      
        //text
		Label textSelectImage = new Label("textSelectImage", new ResourceModel("text.upload.image.file"));
		form.add(textSelectImage);
		
		//feedback
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		form.add(feedback);
		
		// filteredErrorLevels will not be shown in the FeedbackPanel
		//this way we can control them. see the onSubmit method for the form
        int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        feedback.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));
		
		//upload
		uploadField = new FileUploadField("picture");
		form.add(uploadField);
		
		//file feedback will be redirected here
        final FeedbackLabel fileFeedback = new FeedbackLabel("fileFeedback", form);
        fileFeedback.setOutputMarkupId(true);
        form.add(fileFeedback);
		
		//submit button
		Button submitButton = new Button("submit", new ResourceModel("button.upload"));
			
		
		form.add(submitButton);
		
		//form indicator - show when the submit button has been clicked - TODO
		//requires AJAX submit button but we can't use it since Ajax cant do file uploads
		AjaxIndicator indicator = new AjaxIndicator("indicator");
		indicator.setOutputMarkupId(true);
		indicator.setVisible(false);
		form.add(indicator);
		
		//add form to page
		add(form);
    }

}


