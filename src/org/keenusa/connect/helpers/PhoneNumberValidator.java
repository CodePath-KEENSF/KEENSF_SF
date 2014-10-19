package org.keenusa.connect.helpers;

public class PhoneNumberValidator {

	public static boolean isValidPhoneNumber(String phone) {
		if (phone == null) {
			return false;
		}
		phone = phone.replaceAll("[^\\d]", "");
		if (phone.length() != 10) {
			return false;
		} else {
			return true;
		}
	}
}
