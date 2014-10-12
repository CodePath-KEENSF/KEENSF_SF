package org.keenusa.connect.networking;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "results")
public class RemoteAthleteList {

	@ElementList(inline = true)
	List<RemoteAthlete> athletes;

	@Attribute(name = "totalCount")
	private String totalCount;

	@Attribute(name = "count")
	private String count;

	@Attribute(name = "page")
	private String page;

	public String getTotalCount() {
		return totalCount;
	}

	public List<RemoteAthlete> getAthletes() {
		return athletes;
	}

}
