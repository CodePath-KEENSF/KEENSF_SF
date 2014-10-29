package org.keenusa.connect.helpers;

public class CivicoreSessionOpenToRegStringParser {

	public static boolean parseSessionOpenToRegString(String openToPublicRegistration) {
		boolean isOpen = false;
		if (openToPublicRegistration != null) {
			if (openToPublicRegistration.equalsIgnoreCase("yes")) {
				isOpen = true;
			}
		}
		return isOpen;
	}

}
