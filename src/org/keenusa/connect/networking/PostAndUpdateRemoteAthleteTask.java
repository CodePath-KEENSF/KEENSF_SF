package org.keenusa.connect.networking;

import org.keenusa.connect.data.daos.AthleteDAO;
import org.keenusa.connect.models.Athlete;

import android.content.Context;

public class PostAndUpdateRemoteAthleteTask extends PostAndUpdateRemoteDataTask<Athlete> {

	final Athlete athleteDTO;

	public PostAndUpdateRemoteAthleteTask(Context context, PostAndUpdateRemoteDataTaskListener<Athlete> listener, Athlete athleteDTO) {
		super(context, listener);
		this.athleteDTO = athleteDTO;
	}

	@Override
	public Athlete getDTOObject() {
		return athleteDTO;
	}

	@Override
	public boolean postData() throws Exception {
		Athlete athlete = client.updateAthleteProfileRecord(getDTOObject());
		if (athlete != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateDataLocally() {
		AthleteDAO athleteDAO = new AthleteDAO(context);
		return athleteDAO.updateAthleteRecord(getDTOObject());
	}

}
