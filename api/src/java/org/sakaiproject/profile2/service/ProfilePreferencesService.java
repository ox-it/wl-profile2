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

package org.sakaiproject.profile2.service;

import org.sakaiproject.profile2.model.ProfilePreferences;


/**
 * <p>This is the outward facing service that should be used by anyone or anything implementing Profile2 preferences methods.</p>
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 *
 */
public interface ProfilePreferencesService {
	
	/**
	 * Create a blank ProfilePreferences object.
	 * <p>DO NOT USE THIS METHOD.</p>
	 * @return
	 */
	public ProfilePreferences getPrototype();
		
	/**
	 * Get the ProfilePreferences object for the given user. Will be the full record if own, or the desensitised record if other
	 * @param userId
	 * @return ProfilePreferences record for the user, default if they haven't got one, or null if invalid user
	 */
	public ProfilePreferences getProfilePreferencesRecord(String userId);
	
	/**
	 * Save the given ProfilePreferences object. Checks currentUser against the ProfilePreferences object supplied. 
	 * A user can update only their own, no one elses.
	 * 
	 * @param ProfilePreferences object
	 * @return true/false for success
	 */
	public boolean save(ProfilePreferences obj);
	
	/**
	 * Create a ProfilePreferences object for the given user and persist it to the database.
	 * @param userId - either internal user id (6ec73d2a-b4d9-41d2-b049-24ea5da03fca) or eid (jsmith26)
	 * @return true/false for success
	 */
	public boolean create(String userId);
	
	/**
	 * Persist the given ProfilePreferences object to the database
	 * @param obj - ProfilePreferences obj that you want persisted
	 * @return true/false for success
	 */
	public boolean create(ProfilePreferences obj);
	
	/**
	 * Checks whether a user exists. 
	 * <p>This actually just checks for the existence of a user in the system as every user has a ProfilePreferences object, even if its a default one.</p>
	 * 
	 * @param userId - either internal user id (6ec73d2a-b4d9-41d2-b049-24ea5da03fca) or eid (jsmith26)
	 * @return true if exists, false otherwise
	 */
	public boolean checkUserExists(String userId);
	
}
