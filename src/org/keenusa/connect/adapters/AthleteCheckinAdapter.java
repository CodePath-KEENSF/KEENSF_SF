package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.models.Athlete;

import android.content.Context;
import android.widget.ArrayAdapter;

public class AthleteCheckinAdapter extends ArrayAdapter<Athlete> {

	public AthleteCheckinAdapter(Context context, int resource,
			List<Athlete> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

}
