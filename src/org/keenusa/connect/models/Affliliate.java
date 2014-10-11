package org.keenusa.connect.models;

public class Affliliate {

	private long remoteId;
	private String name;
	// optional if want to display "about" or "info"
	private String contactName;
	private String email;
	private String website;

	public Affliliate() {
	}

	public Affliliate(long remoteId, String name, String contactName, String email, String website) {
		this.remoteId = remoteId;
		this.name = name;
		this.contactName = contactName;
		this.email = email;
		this.website = website;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
