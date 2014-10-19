package org.keenusa.connect.helpers;

import android.util.Patterns;

public class EmailAddressValidator {

	public static boolean isValidEmail(String email) {
		if (email == null) {
			return false;
		}
		return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}
