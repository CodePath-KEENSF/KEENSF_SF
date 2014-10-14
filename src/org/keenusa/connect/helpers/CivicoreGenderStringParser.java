package org.keenusa.connect.helpers;

import org.keenusa.connect.models.ContactPerson;

public class CivicoreGenderStringParser {

	public static ContactPerson.Gender parseGenderString(String genderString) {
		ContactPerson.Gender gender = ContactPerson.Gender.UNKNOWN;
		if (genderString != null) {
			if (genderString.equalsIgnoreCase("female")) {
				gender = ContactPerson.Gender.FEMALE;
			}
			if (genderString.equalsIgnoreCase("male")) {
				gender = ContactPerson.Gender.MALE;
			}
		}
		return gender;
	}

}
