package org.keenusa.connect.models.remote;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "success")
public class RemoteUpdateSuccessResult {

	@Text
	String remoteId;

	public String getRemoteId() {
		return remoteId;
	}

}