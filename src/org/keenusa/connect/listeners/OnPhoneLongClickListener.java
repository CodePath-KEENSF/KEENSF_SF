package org.keenusa.connect.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class OnPhoneLongClickListener implements OnLongClickListener {

	private Context context;

	public OnPhoneLongClickListener(Context context) {
		this.context = context;
	}

	@Override
	public boolean onLongClick(View v) {
		String phone = ((TextView) v).getText().toString();
		if (phone != null && !phone.isEmpty()) {
			Log.i("EMAIL_INTENT", "Call is to be initialted to " + phone);
			Intent i = new Intent(Intent.ACTION_CALL);
			// TODO change for actual phone string
			i.setData(Uri.parse("tel:" + "111-000-00"));
			context.startActivity(i);
			return true;
		}
		return false;
	}
}
