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

package org.sakaiproject.profile2.tool;


import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.sakaiproject.profile2.logic.ProfileLogic;
import org.sakaiproject.profile2.logic.SakaiProxy;
import org.sakaiproject.profile2.service.ProfileImageService;
import org.sakaiproject.profile2.tool.pages.MyMessageView;
import org.sakaiproject.profile2.tool.pages.errors.InternalErrorPage;
import org.sakaiproject.profile2.tool.pages.errors.SessionExpiredPage;


public class ProfileApplication extends WebApplication {    
    	
	private transient SakaiProxy sakaiProxy;
	private transient ProfileLogic profileLogic;
	private transient ProfileImageService profileImageService;

	protected void init(){
		
		getResourceSettings().setThrowExceptionOnMissingResource(true);
		
		/* if Session expires, show this error instead */
		getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);
		
		/* if internal error occur, show this page */
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
		
		//TODO on requestcycle.onruntimeexception you can redirect to your error page passing in the exception so we can make a more Sakai-like error page
		
		//super.init();
	
		/* strip the wicket:id tags from the output HTML */
		getMarkupSettings().setStripWicketTags(true);
		
		/* a component that is disabled by Wicket will normally have <em> surrounding it. This makes it null */
		getMarkupSettings().setDefaultBeforeDisabledLink(null);
		getMarkupSettings().setDefaultAfterDisabledLink(null);
		
		
		/* mount pages so we can make nice aliased URLs to them */
		//mountBookmarkablePage("/myFriends", MyFriends.class);
		//mountBookmarkablePage("/viewProfile", ViewProfile.class);
		//mount(new QueryStringUrlCodingStrategy("/myfriends", MyFriends.class));
		//mount(new QueryStringUrlCodingStrategy("/viewprofile", ViewProfile.class));

		
		//getRequestLoggerSettings().setRequestLoggerEnabled(true);
		
		  /*
         * In the following code example we will create a Jasypt byte 
         * encryptor by hand, but in real world we can get it from Spring, 
         * configure it via Web PBE configuration... whatever we want to. 
         */
		/*
        StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("jasypt");
        FixedStringSaltGenerator generator = new FixedStringSaltGenerator();
        generator.setSalt("wicketwicketwicketwicketwicket");
        encryptor.setSaltGenerator(generator);
        */
        /*
         * Create the Jasypt Crypt Factory with the desired encryptor,
         * which will return org.jasypt.wicket.JasyptCrypt objects implementing
         * the org.apache.wicket.util.crypt.ICrypt interface.
         */
		/*
        ICryptFactory jasyptCryptFactory = new JasyptCryptFactory(encryptor);
        */
        /*
         * Set the Jasypt Crypt Factory into the application configuration.
         */
		/*
        getSecuritySettings().setCryptFactory(jasyptCryptFactory);
		*/
	}
	
	public ProfileApplication() {
	}
	
	//setup homepage		
	public Class<Dispatcher> getHomePage() {
		return Dispatcher.class;
	}
	
	//expose ProfileApplication itself
	public static ProfileApplication get() {
		return (ProfileApplication) Application.get();
	}

	//expose SakaiProxy API
	public void setSakaiProxy(SakaiProxy sakaiProxy) {
		this.sakaiProxy = sakaiProxy;
	}

	public SakaiProxy getSakaiProxy() {
		return sakaiProxy;
	}
	
	//expose Profile API
	public void setProfileLogic(ProfileLogic profileLogic) {
		this.profileLogic = profileLogic;
	}

	public ProfileLogic getProfileLogic() {
		return profileLogic;
	}
		
	//expose Profile API
	public void setProfileImageService(ProfileImageService profileImageService) {
		this.profileImageService = profileImageService;
	}

	public ProfileImageService getProfileImageService() {
		return profileImageService;
	}
	

}
