package org.keenusa.connect.models.remote;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteAffiliate {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	@Element(name = "affiliateName", required = false)
	private String affiliateName;

	@Element(name = "contactName", required = false)
	private String contactName;

	@Element(name = "email", required = false)
	private String email;

	@Element(name = "website", required = false)
	private String website;

	public RemoteAffiliate() {
		super();
	}

	public RemoteAffiliate(String remoteId, String created, String updated, String affiliateName, String contactName, String email, String website) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.affiliateName = affiliateName;
		this.contactName = contactName;
		this.email = email;
		this.website = website;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

	public String getAffiliateName() {
		return affiliateName;
	}

	public String getContactName() {
		return contactName;
	}

	public String getEmail() {
		return email;
	}

	public String getWebsite() {
		return website;
	}

}