package org.keenusa.connect.helpers;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class PersonNameFormatter {

	public static String getFormatedNameString(String name) {
		if (name != null) {
			String nameCopy = name.replaceAll(" ", "");
			if (StringUtils.isAllUpperCase(nameCopy)) {
				name = name.toLowerCase(Locale.ENGLISH);
			}
			return WordUtils.capitalize(name);
		}
		return null;
	}
}
