package org.keenusa.connect.utilities;

import org.keenusa.connect.R;

import android.widget.ImageView;

public class SetSessionImage {

	public static void changeBackgroundImage(String name, ImageView image) {

		image.setImageResource(0);

		if (name.equals("Sports 1") || name.equals("Kids Sports and Young Adult Sports - East Bay")) {
			image.setImageResource(R.drawable.sports1);
		} else if (name.equals("Sports 2")) {
			image.setImageResource(R.drawable.sports2);
		} else if (name.equals("Sports 1 & 2")) {
			image.setImageResource(R.drawable.sports1tennis);
		} else if (name.equals("Basketball") || name.equals("Basketball Clinic - Hoops")
				|| name.equals("Summer Family Pool Party or Basketball & Picnic")) {
			image.setImageResource(R.drawable.basketballclinic);
		} else if (name.equals("KEENquatics") || name.equals("KEENquatics - SWIM")) {
			image.setImageResource(R.drawable.quatics);
		} else if (name.equals("Summer Family Picnic & Games")) {
			image.setImageResource(R.drawable.picnic);
		} else if (name.equals("Kids Sports & Tennis - SFUHS")) {
			image.setImageResource(R.drawable.sports1tennis);
		} else if (name.equals("Kids and Young Adult Sports at YMCA")) {
			image.setImageResource(R.drawable.ymca);
		} else if (name.equals("Holiday Party 2012")) {
			image.setImageResource(R.drawable.holidayparty);
		} else if (name.equals("KEENGala: Pre-Event") || name.equals("KEENGala - Event Volunteer Shift 1")
				|| name.equals("KEENGala - Event Volunteer Shift 2")) {
			image.setImageResource(R.drawable.holidayparty);
		} else {
			image.setImageResource(R.drawable.sports1);
		}
	}
}
