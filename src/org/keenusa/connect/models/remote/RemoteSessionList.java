package org.keenusa.connect.models.remote;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "results")
public class RemoteSessionList {

	@ElementList(inline = true)
	List<RemoteSession> sessions;

	@Attribute(name = "totalCount")
	private String totalCount;

	@Attribute(name = "count")
	private String count;

	@Attribute(name = "page")
	private String page;

	public String getTotalCount() {
		return totalCount;
	}

	public String getCount() {
		return count;
	}

	public String getPage() {
		return page;
	}

	public List<RemoteSession> getRemoteSessions() {
		return sessions;
	}

}
