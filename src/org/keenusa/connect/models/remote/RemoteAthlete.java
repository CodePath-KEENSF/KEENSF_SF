package org.keenusa.connect.models.remote;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteAthlete {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	@Element(name = "firstName")
	private String firstName;

	@Element(name = "lastName")
	private String lastName;

	@Element(name = "nickName", required = false)
	private String nickName;

	@Element(name = "dateOfBirth", required = false)
	private String dob;

	@Element(name = "gender", required = false)
	private String gender;

	@Element(name = "email", required = false)
	private String email;

	@Element(name = "homePhone", required = false)
	private String homePhone;

	@Element(name = "city", required = false)
	private String city;

	@Element(name = "state", required = false)
	private String state;

	@Element(name = "zipCode", required = false)
	private String zipCode;

	@Element(name = "status", required = false)
	private String status;

	@Element(name = "parentGuardianCellPhone", required = false)
	private String parentGuardianCellPhone;

	@Element(name = "parentGuardianEmailAddress", required = false)
	private String parentGuardianEmailAddress;

	@Element(name = "parentGuardianHomePhone", required = false)
	private String parentGuardianHomePhone;

	@Element(name = "parentGuardianRelationship", required = false)
	private String parentGuardianRelationship;

	@Element(name = "primaryLanguageAtHome", required = false)
	private String primaryLanguageAtHome;

	@Element(name = "primaryParentGuardianFirstName", required = false)
	private String primaryParentGuardianFirstName;

	@Element(name = "primaryParentGuardianLastName", required = false)
	private String primaryParentGuardianLastName;

	public RemoteAthlete() {
		super();
	}

	public RemoteAthlete(String remoteId, String created, String updated, String firstName, String lastName, String nickName, String dob,
			String gender, String email, String homePhone, String city, String state, String zipCode, String status, String parentGuardianCellPhone,
			String parentGuardianEmailAddress, String parentGuardianHomePhone, String parentGuardianRelationship, String primaryLanguageAtHome,
			String primaryParentGuardianFirstName, String primaryParentGuardianLastName) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.dob = dob;
		this.gender = gender;
		this.email = email;
		this.homePhone = homePhone;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.status = status;
		this.parentGuardianCellPhone = parentGuardianCellPhone;
		this.parentGuardianEmailAddress = parentGuardianEmailAddress;
		this.parentGuardianHomePhone = parentGuardianHomePhone;
		this.parentGuardianRelationship = parentGuardianRelationship;
		this.primaryLanguageAtHome = primaryLanguageAtHome;
		this.primaryParentGuardianFirstName = primaryParentGuardianFirstName;
		this.primaryParentGuardianLastName = primaryParentGuardianLastName;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

	public String getNickName() {
		return nickName;
	}

	public String getDob() {
		return dob;
	}

	public String getGender() {
		return gender;
	}

	public String getEmail() {
		return email;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getStatus() {
		return status;
	}

	public String getParentGuardianCellPhone() {
		return parentGuardianCellPhone;
	}

	public String getParentGuardianEmailAddress() {
		return parentGuardianEmailAddress;
	}

	public String getParentGuardianHomePhone() {
		return parentGuardianHomePhone;
	}

	public String getParentGuardianRelationship() {
		return parentGuardianRelationship;
	}

	public String getPrimaryLanguageAtHome() {
		return primaryLanguageAtHome;
	}

	public String getPrimaryParentGuardianFirstName() {
		return primaryParentGuardianFirstName;
	}

	public String getPrimaryParentGuardianLastName() {
		return primaryParentGuardianLastName;
	}

}