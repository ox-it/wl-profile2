package uk.ac.lancs.e_science.profile2.api;

import java.util.Date;
import java.util.List;

import uk.ac.lancs.e_science.profile2.hbm.ProfilePrivacy;
import uk.ac.lancs.e_science.profile2.hbm.ProfileStatus;


public interface Profile {
	
		
	public boolean checkContentTypeForProfileImage(String contentType);

	public byte[] scaleImage (byte[] imageData, int maxSize);
	
	public Date convertStringToDate(String dateStr);
	
	public String convertDateToString(Date date);

	/**
	 * Get a list of uuid's that are friends with a given user
	 *
	 * @param userId		uuid of the user to retrieve the list of friends for
	 * @param confirmed		toggles list between confirmed and pending friends
	 */
	public List getFriendsForUser(String userId, boolean confirmed);
	
	/**
	 * Make a request that friendId be a friend of userId
	 *
	 * @param userId		uuid of the user making the request
	 * @param friendId		uuid of the user that userId wants to be a friend of
	 */
	public boolean addFriend(String userId, String friendId);
	
	/**
	 * Confirm that userId is a friend of friendId (from a pending friend request)
	 *
	 * @param friendId		uuid of the user that received the friend request
	 * @param userId		uuid of the user that made the original friend request
	 * 
	 * Note that userId will ALWAYS be the one making the friend request, and friendId
	 * will ALWAYS be the one who receives the request.
	 */
	public boolean confirmFriend(String friendId, String userId);
	
	
	/**
	 * Get the number of unread messages
	 *
	 * @param userId		uuid of the user to retrieve the count for
	 */
	public int getUnreadMessagesCount(String userId);
	
	
		
	/**
	 * Get the latest status of a user
	 *
	 * @param userId		uuid of the user to get their status for
	 */
	public ProfileStatus getLatestUserStatus(String userId);
	
	/**
	 * Set user status
	 *
	 * @param userId		uuid of the user 
	 * @param status		status to be set
	 */
	public boolean setUserStatus(String userId, String status);
	
	
	/**
	 * Convert a date into a field like (today, yesterday, last week, etc)
	 *
	 * @param data		date to convert
	 * 
	 * convert the date out into a better format:
		 * if today = 'today'
		 * if yesterday = 'yesterday'
		 * if this week = 'on Weekday'
		 * if last week = 'last week'
		 * past a week ago we don't display the status last updated
		
	 * 
	 * 
	 */
	public String convertDateForStatus(Date date);
	
	/**
	 * Truncate a string and pad it with ... at the end
	 *
	 * @param string	the string to be manipulated
	 * @param size		size the string should be truncated to (not including the ...)
	 */
	public String truncateAndPadStringToSize(String string, int size);
	
	
	
	/**
	 * Create a default privacy record where everything is public
	 *
	 * @param userId		uuid of the user to create the record for
	 */
	public ProfilePrivacy createDefaultPrivacyRecord(String userId);
	
	/**
	 * Retrieve the profile privacy record from the database for this user
	 *
	 * @param userId	uuid of the user to retrieve the record for
	 */
	public ProfilePrivacy getPrivacyRecordForUser(String userId);
	
	/**
	 * Save the profile privacy record to the database for this user
	 *
	 * @param profilePrivacy	the record for the user
	 */
	public boolean savePrivacyRecordForUser(ProfilePrivacy profilePrivacy);
	
}
