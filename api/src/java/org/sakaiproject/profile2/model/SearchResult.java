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

package org.sakaiproject.profile2.model;

import java.io.Serializable;

/**
 * SearchResult.java
 * 
 * This is a model for storing information returned from a search about a user
 * Because people can limit searches on their profile, we need to return an object like this in order
 * to minimise the number of database queries.
 * 
 * When we get the list of users, we then need to see if the user that searched for them is a friend,
 * then determine if the settings the person has on their profile limits searches to friends or everyone.
 * If the privileges aren't high enough they need to be removed from the list.
 * 
 * But, when this data is being consumed, we also need the information about their friend status, privacy settings etc,
 * so this wraps that up so its already available.
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 */


public class SearchResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userUuid;
	private String displayName;
	private String userType;
	private boolean friend;
	private boolean profileImageAllowed;
	private boolean statusAllowed;
	private boolean friendsListVisible;
	private boolean friendRequestToThisPerson;
	private boolean friendRequestFromThisPerson;
	private boolean connectionAllowed;
	
	/* 
	 * Constructor to create a SearchResult object in one go
	 */
	public SearchResult(String userUuid, String displayName, String userType, boolean friend, boolean profileImageAllowed, boolean statusAllowed, boolean friendsListVisible, boolean friendRequestToThisPerson, boolean friendRequestFromThisPerson, boolean connectionAllowed) {
		super();
		this.userUuid = userUuid;
		this.displayName = displayName;
		this.userType = userType;
		this.friend = friend;
		this.profileImageAllowed = profileImageAllowed;
		this.statusAllowed = statusAllowed;
		this.friendsListVisible = friendsListVisible;
		this.friendRequestToThisPerson = friendRequestToThisPerson;
		this.friendRequestFromThisPerson = friendRequestFromThisPerson;
		this.connectionAllowed = connectionAllowed;
	}
	
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public boolean isFriend() {
		return friend;
	}
	public void setFriend(boolean friend) {
		this.friend = friend;
	}
	public boolean isProfileImageAllowed() {
		return profileImageAllowed;
	}
	public void setProfileImageAllowed(boolean profileImageAllowed) {
		this.profileImageAllowed = profileImageAllowed;
	}
	public void setStatusAllowed(boolean statusAllowed) {
		this.statusAllowed = statusAllowed;
	}
	public boolean isStatusAllowed() {
		return statusAllowed;
	}
	public void setFriendsListVisible(boolean friendsListVisible) {
		this.friendsListVisible = friendsListVisible;
	}
	public boolean isFriendsListVisible() {
		return friendsListVisible;
	}
	public boolean isFriendRequestToThisPerson() {
		return friendRequestToThisPerson;
	}
	public void setFriendRequestToThisPerson(boolean friendRequestToThisPerson) {
		this.friendRequestToThisPerson = friendRequestToThisPerson;
	}
	public boolean isFriendRequestFromThisPerson() {
		return friendRequestFromThisPerson;
	}
	public void setFriendRequestFromThisPerson(boolean friendRequestFromThisPerson) {
		this.friendRequestFromThisPerson = friendRequestFromThisPerson;
	}
	public boolean isConnectionAllowed() {
		return connectionAllowed;
	}
	public void setConnectionAllowed(boolean connectionAllowed) {
		this.connectionAllowed = connectionAllowed;
	}
	
}
