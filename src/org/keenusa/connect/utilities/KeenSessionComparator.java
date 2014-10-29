package org.keenusa.connect.utilities;

import java.util.Comparator;

import org.keenusa.connect.models.KeenSession;

public class KeenSessionComparator  implements Comparator<KeenSession>{

	@Override
	public int compare(KeenSession s1, KeenSession s2) {
		return s1.getDate().compareTo(s2.getDate());
	}

}
