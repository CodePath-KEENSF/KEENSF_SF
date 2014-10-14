package org.keenusa.connect.helpers;

import org.keenusa.connect.models.Parent;

public class CivicoreParentRelationshipStringParser {

	public static Parent.ParentRelationship parseParentRelationshipString(String parentRelationshipString) {
		Parent.ParentRelationship pr = null;
		if (parentRelationshipString != null) {
			if (parentRelationshipString.equalsIgnoreCase("MOTHER")) {
				pr = Parent.ParentRelationship.MOTHER;
			}
			if (parentRelationshipString.equalsIgnoreCase("FATHER")) {
				pr = Parent.ParentRelationship.FATHER;
			}
			if (parentRelationshipString.equalsIgnoreCase("GRANDPARENT")) {
				pr = Parent.ParentRelationship.GRANDPARENT;
			}
			if (parentRelationshipString.equalsIgnoreCase("OTHER")) {
				pr = Parent.ParentRelationship.OTHER;
			}
			if (parentRelationshipString.equalsIgnoreCase("PARENTS")) {
				pr = Parent.ParentRelationship.PARENTS;
			}
			if (parentRelationshipString.equalsIgnoreCase("FOSTER_PARENT")) {
				pr = Parent.ParentRelationship.FOSTER_PARENT;
			}
		}
		return pr;
	}
}
