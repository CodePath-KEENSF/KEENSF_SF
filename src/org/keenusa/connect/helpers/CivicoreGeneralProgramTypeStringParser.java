package org.keenusa.connect.helpers;

import org.keenusa.connect.models.KeenProgram;

public class CivicoreGeneralProgramTypeStringParser {

	public static KeenProgram.GeneralProgramType parseGenralProgramTypeString(String generalProgramType) {
		KeenProgram.GeneralProgramType gpt = null;
		if (generalProgramType != null) {
			if (generalProgramType.equalsIgnoreCase("sports")) {
				gpt = KeenProgram.GeneralProgramType.SPORTS;
			}
			if (generalProgramType.equalsIgnoreCase("recreation")) {
				gpt = KeenProgram.GeneralProgramType.RECREATION;
			}
			if (generalProgramType.equalsIgnoreCase("special events")) {
				gpt = KeenProgram.GeneralProgramType.SPECIAL_EVENTS;
			}
		}
		return gpt;
	}
}
