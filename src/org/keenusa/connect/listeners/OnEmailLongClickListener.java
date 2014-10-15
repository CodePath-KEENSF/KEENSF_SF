package org.keenusa.connect.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class OnEmailLongClickListener implements OnLongClickListener {

	private Context context;

	public OnEmailLongClickListener(Context context) {
		this.context = context;
	}

	@Override
	public boolean onLongClick(View v) {
		String email = ((TextView) v).getText().toString();
		if (email != null && !email.isEmpty()) {
			Log.i("EMAIL_INTENT", "Email to be send to email " + email);
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("plain/text");
			// TODO change for actual email string
			i.putExtra(Intent.EXTRA_EMAIL, new String[] { "fake@testmail.com" });
			i.putExtra(Intent.EXTRA_SUBJECT, "some subject");
			i.putExtra(Intent.EXTRA_TEXT, "mail body");
			context.startActivity(Intent.createChooser(i, ""));
			return true;
		}
		return false;
	}
}
