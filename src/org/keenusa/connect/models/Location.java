package org.keenusa.connect.models;

public class Location {

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

}
