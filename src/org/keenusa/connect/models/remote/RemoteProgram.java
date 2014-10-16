package org.keenusa.connect.models.remote;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteProgram {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	@Element(name = "address1", required = false)
	private String address1;

	@Element(name = "address2", required = false)
	private String address2;

	@Element(name = "approvalEmailMessage", required = false)
	private String approvalEmailMessage;

	@Element(name = "city", required = false)
	private String city;

	@Element(name = "classEndDate", required = false)
	private String classEndDate;

	@Element(name = "classStartDate", required = false)
	private String classStartDate;

	@Element(name = "className", required = false)
	private String className;

	@Element(name = "generalProgramType", required = false)
	private String generalProgramType;

	@Element(name = "registrationConfirmation", required = false)
	private String registrationConfirmation;

	@Element(name = "state", required = false)
	private String state;

	@Element(name = "times", required = false)
	private String times;

	@Element(name = "zipCode", required = false)
	private String zipCode;

	public RemoteProgram() {
		super();
	}

	public RemoteProgram(String remoteId, String created, String updated, String address1, String address2, String approvalEmailMessage, String city,
			String classEndDate, String classStartDate, String className, String generalProgramType, String registrationConfirmation, String state,
			String times, String zipCode) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.address1 = address1;
		this.address2 = address2;
		this.approvalEmailMessage = approvalEmailMessage;
		this.city = city;
		this.classEndDate = classEndDate;
		this.classStartDate = classStartDate;
		this.className = className;
		this.generalProgramType = generalProgramType;
		this.registrationConfirmation = registrationConfirmation;
		this.state = state;
		this.times = times;
		this.zipCode = zipCode;
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

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getApprovalEmailMessage() {
		return approvalEmailMessage;
	}

	public String getCity() {
		return city;
	}

	public String getClassEndDate() {
		return classEndDate;
	}

	public String getClassStartDate() {
		return classStartDate;
	}

	public String getClassName() {
		return className;
	}

	public String getGeneralProgramType() {
		return generalProgramType;
	}

	public String getRegistrationConfirmation() {
		return registrationConfirmation;
	}

	public String getState() {
		return state;
	}

	public String getTimes() {
		return times;
	}

	public String getZipCode() {
		return zipCode;
	}

}
