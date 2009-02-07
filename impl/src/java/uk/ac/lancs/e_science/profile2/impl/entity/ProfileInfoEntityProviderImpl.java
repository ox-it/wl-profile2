package uk.ac.lancs.e_science.profile2.impl.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sakaiproject.entitybroker.DeveloperHelperService;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Search;

import uk.ac.lancs.e_science.profile2.api.ProfileManager;
import uk.ac.lancs.e_science.profile2.api.entity.ProfileInfoEntityProvider;
import uk.ac.lancs.e_science.profile2.api.entity.model.ProfileInfo;

/**
 * Provider for profile info entities
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 */
public class ProfileInfoEntityProviderImpl implements ProfileInfoEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider, RESTful  {
	
	//get API's
	private DeveloperHelperService developerHelperService;
	public void setDeveloperHelperService(DeveloperHelperService developerHelperService) {
		this.developerHelperService = developerHelperService;
	}
	private ProfileManager profileManager;
	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}

	
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}
	
	public boolean entityExists(String id) {
		//everyone has a profile, even if they haven't filled it out
		return true;
	}

	/**
	 * This is my working case where I will establish how EB need sto work for the privacy setings etc. they are wrapped in
	 * ProfileManagerImpl.
	 * 
	 * THIS IS ONLY CALLED WHEN WE NEED TO CREATE A NEW PROFILE OBJECT. ie same as in myProfile
	 */
	public String createEntity(EntityReference ref, Object entity) {
	    //get incoming entity
		ProfileInfo incoming = (ProfileInfo) entity;
		//check it's got at least a userId
		if(incoming.getUserId() == null) {
			throw new IllegalArgumentException("The profile.userId must be set in order to create a profile");
		}
		
		//get current userId
		String currentUserId = developerHelperService.getUserIdFromRef(developerHelperService.getCurrentUserReference());
		/*
		//get profile for the incoming userId but only bits that are visible by currentUserId
		ProfileInfo profileInfo = profileManager.getProfileForUserXVisibleByUserY(incoming.getUserId(), currentUserId);
	      BlogWowBlog blog = blogLogic.getBlogById(incoming.getBlog().getId());
	      BlogWowEntry entry = new BlogWowEntry(blog, userId, incoming.getTitle(), incoming.getText(), incoming.getPrivacySetting(), new Date());
	      entryLogic.saveEntry(entry, null);
	      return entry.getId();
	      */
		
		//return profileInfo.getUserId();
		
		return null;
	}
	
	public Object getSampleEntity() {
		return new ProfileInfo();
	}
	

	public void updateEntity(EntityReference ref, Object entity) {
		//save a given profileInfo object
		//String userUuid = ref.getId();
		
	}

	public Object getEntity(EntityReference ref) {
		String userUuid = ref.getId();
		if (userUuid == null) {
			return new ProfileInfo();
		}
		
		//get a ProfileInfo object
		//ProfileInfo profileInfo = profileManager.getProfile(userUuid);
		//if (profileInfo == null) {
		//	throw new IllegalArgumentException("No profile found with this id: " + userUuid);
		//}
	    //return profileInfo;
		return null;
	}

	public void deleteEntity(EntityReference ref) {
		String userUuid = ref.getId();
		//check if profile exists and delete it.
		//if (!entryLogic.entryExists(entryId)) {
		//	throw new IllegalArgumentException("Cannot find a blog entry to delete with this reference: " + ref);
		//}
		//entryLogic.removeEntry(entryId, null);
	}

	public List<?> getEntities(EntityReference ref, Search search) {
		if (search == null || search.isEmpty()) {
			throw new IllegalArgumentException("Must specify at least one userId in the 'userId' param to get entries from.");
		}
		return new ArrayList();
		
	}

	public String[] getHandledOutputFormats() {
		return new String[] { Formats.XML, Formats.JSON };
	}

	public String[] getHandledInputFormats() {
		return new String[] { Formats.HTML, Formats.XML, Formats.JSON };
	}
	
	
	// Added for compatibility
	public String createEntity(EntityReference ref, Object entity, Map<String, Object> params) {
		return createEntity(ref, entity);
	}

	public void updateEntity(EntityReference ref, Object entity, Map<String, Object> params) {
		updateEntity(ref, entity);
	}

	public void deleteEntity(EntityReference ref, Map<String, Object> params) {
		deleteEntity(ref);
	}

}
