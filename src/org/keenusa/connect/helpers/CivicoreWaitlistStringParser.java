package org.keenusa.connect.helpers;

public class CivicoreWaitlistStringParser {

	public static boolean parseWaitlistString(String waitlist) {
		boolean isInWaitlist = false;
		if (waitlist != null) {
			if (waitlist.equalsIgnoreCase("yes")) {
				isInWaitlist = true;
			}
		}
		return isInWaitlist;
	}

}
