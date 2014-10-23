package org.keenusa.connect.models;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.helpers.CivicoreTimestampStringParser;
import org.keenusa.connect.models.remote.RemoteAffiliate;

public class Affiliate {

	private long remoteId;
	private String name;
	// optional if want to display "about" or "info"
	private String contactName;
	private String email;
	private String website;
	private long remoteCreateTimestamp;
	private long remoteUpdatedTimestamp;

	public Affiliate() {
	}

	public Affiliate(long remoteId, String name, String contactName, String email, String website) {
		this.remoteId = remoteId;
		this.name = name;
		this.contactName = contactName;
		this.email = email;
		this.website = website;
	}

	public static Affiliate fromRemoteAffiliate(RemoteAffiliate remoteAffiliate) {
		Affiliate affiliate = null;
		if (remoteAffiliate != null) {
			affiliate = new Affiliate();
			affiliate.setRemoteId(Long.valueOf(remoteAffiliate.getRemoteId()));
			affiliate.setName(remoteAffiliate.getAffiliateName());
			affiliate.setEmail(remoteAffiliate.getEmail());
			affiliate.setContactName(remoteAffiliate.getContactName());
			affiliate.setWebsite(remoteAffiliate.getWebsite());
			affiliate.setRemoteCreateTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteAffiliate.getCreated()).getMillis());
			affiliate.setRemoteUpdatedTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteAffiliate.getUpdated()).getMillis());
		}
		return affiliate;
	}

	public static List<Affiliate> fromRemoteAffiliateList(List<RemoteAffiliate> remoteAffiliateList) {
		List<Affiliate> affiliates = null;
		if (remoteAffiliateList != null) {
			affiliates = new ArrayList<Affiliate>(remoteAffiliateList.size());
			for (RemoteAffiliate remoteAffiliate : remoteAffiliateList) {
				Affiliate affiliate = fromRemoteAffiliate(remoteAffiliate);
				affiliates.add(affiliate);
			}

		} else {
			affiliates = new ArrayList<Affiliate>();
		}
		return affiliates;
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

	public long getRemoteCreateTimestamp() {
		return remoteCreateTimestamp;
	}

	public void setRemoteCreateTimestamp(long remoteCreateTimestamp) {
		this.remoteCreateTimestamp = remoteCreateTimestamp;
	}

	public long getRemoteUpdatedTimestamp() {
		return remoteUpdatedTimestamp;
	}

	public void setRemoteUpdatedTimestamp(long remoteUpdatedTimestamp) {
		this.remoteUpdatedTimestamp = remoteUpdatedTimestamp;
	}

}
