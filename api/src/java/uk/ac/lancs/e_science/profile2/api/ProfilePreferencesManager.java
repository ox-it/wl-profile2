package uk.ac.lancs.e_science.profile2.api;


public class ProfilePreferencesManager {

	//setup the email preferences values
	public static final int EMAIL_OPTION_ALL = 0; 
	public static final int EMAIL_OPTION_REQUESTS_ONLY = 1; 
	public static final int EMAIL_OPTION_CONFIRMS_ONLY = 2;
	public static final int EMAIL_OPTION_NONE = 3;



	//these values are used when creating a default privacy record for a user
	public static final int DEFAULT_EMAIL_SETTING = EMAIL_OPTION_ALL;
	public static final boolean DEFAULT_TWITTER_SETTING = false;
	
}
