package org.keenusa.connect.helpers;

public class CivicorePhoneNumberFormatConverter {

	public static String toCivicorePhoneNumberFormat(String phone) {
		if (phone != null) {
			if (phone.isEmpty()) {
				return "";
			}
			if (PhoneNumberValidator.isValidPhoneNumber(phone)) {
				phone = phone.replaceAll("[^\\d]", "");
				StringBuilder sbphone = new StringBuilder(phone);
				sbphone.insert(3, "-");
				sbphone.insert(7, "-");
				return sbphone.toString();
			}

		}
		return null;
	}
}
