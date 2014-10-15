package org.keenusa.connect.models;

public class Parent extends ContactPerson {

	//parentGuardianRelationship in the source
	//often not populated in old records but mostly present in new ones
	private ParentRelationship parentRelationship;
	private boolean isPrimary;

	//in remote source lookup 97
	public enum ParentRelationship {
		MOTHER("mother"), FATHER("father"), GRANDPARENT("grandparent"), OTHER("parent/guardian"), PARENTS("parents"), FOSTER_PARENT("foster parent");

		private final String displayName;

		private ParentRelationship(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	public Parent() {
		super();
	}

	public Parent(ParentRelationship parentRelationship, boolean isPrimary, String firstName, String middleName, String lastName, String email,
			String phone, String cellPhone, Gender gender) {
		super(firstName, middleName, lastName, email, phone, cellPhone, gender);
		this.parentRelationship = parentRelationship;
		this.isPrimary = isPrimary;
	}

	public ParentRelationship getParentRelationship() {
		return parentRelationship;
	}

	public void setParentRelationship(ParentRelationship parentRelationship) {
		this.parentRelationship = parentRelationship;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

}
