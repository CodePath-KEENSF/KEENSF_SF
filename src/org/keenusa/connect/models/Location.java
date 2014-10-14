package org.keenusa.connect.models;

import java.io.Serializable;

public class Location implements Serializable {

	private long id;
	private String city;
	private String zipCode;
	private String state;
	private String address1;
	private String address2;

	public Location() {

	}

	public Location(String city, String zipCode, String state, String address1, String address2) {
		super();
		this.city = city;
		this.zipCode = zipCode;
		this.state = state;
		this.address1 = address1;
		this.address2 = address2;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getLocationString() {

		StringBuilder sb = new StringBuilder();
		if (getAddress1() != null && !getAddress1().isEmpty()) {
			sb.append(getAddress1());
		}
		if (getAddress2() != null && !getAddress2().isEmpty()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(getAddress2());
		}
		if (getCity() != null && !getCity().isEmpty()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(getCity());
		}
		if (getState() != null && !getState().isEmpty()) {
			if (getCity() != null && !getCity().isEmpty()) {
				sb.append(", ");
			} else if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(getState());
		}
		if (getZipCode() != null && !getZipCode().isEmpty()) {
			if ((getState() != null && !getState().isEmpty()) || (getCity() != null && !getCity().isEmpty())) {
				sb.append(" ");
			} else if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(getZipCode());
		}
		return sb.toString();
	}

}
