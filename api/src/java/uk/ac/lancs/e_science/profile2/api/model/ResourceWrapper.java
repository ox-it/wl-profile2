package uk.ac.lancs.e_science.profile2.api.model;

/**
 * This is a simple wrapper object to wrap a byte[] so we can store some other metadata along with it:
 * <p>
 * resourceID - id of resource in CHS or if isExternal then this will be populated with the URL to the resource as the identifier<br />
 * bytes[]	- content<br />
 * mimeType	- content type<br />
 * length	- contetn length<br />
 * isExternal - if the resource is external, therefore the resourceID will be the external URL.<br />
 * </p>
 * 
 * We could possibly use net.sf.ehcache.MimeTypeByteArray
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 *
 */
public class ResourceWrapper {

	private String resourceID;
	private byte[] bytes;
	private String mimeType;
	private int length;
	private boolean isExternal;
	
	/**
	 * No-arg constructor
	 */
	public ResourceWrapper() {
	}
	
	/**
	 * Full constructor
	 * @param resourceID
	 * @param bytes
	 * @param mimeType
	 * @param isExternal
	 */
	public ResourceWrapper(String resourceID, byte[] bytes, String mimeType, int length, boolean isExternal) {
		super();
		this.resourceID = resourceID;
		this.bytes = bytes;
		this.mimeType = mimeType;
		this.length = length;
		this.isExternal = isExternal;
	}

	public String getResourceID() {
		return resourceID;
	}

	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	
	
}
