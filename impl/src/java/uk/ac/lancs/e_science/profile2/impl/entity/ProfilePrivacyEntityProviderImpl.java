package uk.ac.lancs.e_science.profile2.impl.entity;

import java.util.List;
import java.util.Map;

import org.sakaiproject.entitybroker.DeveloperHelperService;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.exception.EntityNotFoundException;

import uk.ac.lancs.e_science.profile2.api.ProfilePrivacyService;
import uk.ac.lancs.e_science.profile2.api.ProfileService;
import uk.ac.lancs.e_science.profile2.api.entity.ProfilePrivacyEntityProvider;
import uk.ac.lancs.e_science.profile2.api.entity.model.UserProfile;

public class ProfilePrivacyEntityProviderImpl implements ProfilePrivacyEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider, RESTful {

	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}
		
	public boolean entityExists(String eid) {
		//check the user is valid. if it is then return true as everyone has a privacy record, even if its a default one.
		//note that we DO NOT check if they have an actual privacy record, just if they exist.
		return profileService.checkUserExists(eid);
	}

	public Object getSampleEntity() {
		//PrivacyEntity entity = profileService.getPrototype();
		//return entity;
		return null;
	}
	
	public Object getEntity(EntityReference ref) {
	
		//get the full profile for the user. takes care of privacy checks against the current user
		UserProfile entity = profileService.getFullUserProfile(ref.getId());
		if(entity == null) {
			throw new EntityNotFoundException("Profile could not be retrieved for " + ref.getId(), ref.getReference());
		}
		return entity;
	}
	
	
	
	
	public void updateEntity(EntityReference ref, Object entity, Map<String, Object> params) {
		/*
		String userId = ref.getId();
		if (StringUtils.isBlank(userId)) {
			throw new IllegalArgumentException("Cannot update, No userId in provided reference: " + ref);
		}
		
		if (entity.getClass().isAssignableFrom(UserProfile.class)) {
			UserProfile userProfile = (UserProfile) entity;
			profileService.save(userProfile);
		} else {
			 throw new IllegalArgumentException("Invalid entity for update, must be UserProfile object");
		}
		*/
	}
	
	
	public String createEntity(EntityReference ref, Object entity, Map<String, Object> params) {
		
		//reference will be the userUuid, which comes from the UserProfile
		String userUuid = null;

		/*
		if (entity.getClass().isAssignableFrom(UserProfile.class)) {
			UserProfile userProfile = (UserProfile) entity;
			
			if(profileService.create(userProfile)) {
				userUuid = userProfile.getUserUuid();
			}
			if(userUuid == null) {
				throw new EntityException("Could not create entity", ref.getReference());
			}
		} else {
			 throw new IllegalArgumentException("Invalid entity for create, must be UserProfile object");
		}
		*/
		return userUuid;
	}

	

	
	
	public void deleteEntity(EntityReference ref, Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	public List<?> getEntities(EntityReference ref, Search search) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getHandledOutputFormats() {
		return new String[] {Formats.XML, Formats.JSON, Formats.FORM};
	}

	public String[] getHandledInputFormats() {
		return new String[] {Formats.XML, Formats.JSON, Formats.HTML, Formats.FORM};
	}
	
		
	private DeveloperHelperService developerHelperService;
	public void setDeveloperHelperService(DeveloperHelperService developerHelperService) {
		this.developerHelperService = developerHelperService;
	}
	
	private ProfilePrivacyService privacyService;
	public void setProfilePrivacyService(ProfilePrivacyService privacyService) {
		this.privacyService = privacyService;
	}
	
	private ProfileService profileService;
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}

	
	

}
