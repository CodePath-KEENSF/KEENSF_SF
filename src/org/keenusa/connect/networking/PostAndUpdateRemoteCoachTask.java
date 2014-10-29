package org.keenusa.connect.networking;

import org.keenusa.connect.data.daos.CoachDAO;
import org.keenusa.connect.models.Coach;

import android.content.Context;

public class PostAndUpdateRemoteCoachTask extends PostAndUpdateRemoteDataTask<Coach> {

	private final Coach coachDTO;

	public PostAndUpdateRemoteCoachTask(Context context, PostAndUpdateRemoteDataTaskListener<Coach> listener, Coach coachDTO) {
		super(context, listener);
		this.coachDTO = coachDTO;
	}

	@Override
	public Coach getDTOObject() {
		return coachDTO;
	}

	@Override
	public boolean postData() throws Exception {
		Coach coach = client.updateCoachProfileRecord(getDTOObject());
		if (coach != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateDataLocally() {
		CoachDAO coachDAO = new CoachDAO(context);
		return coachDAO.updateCoachRecord(getDTOObject());
	}

}
