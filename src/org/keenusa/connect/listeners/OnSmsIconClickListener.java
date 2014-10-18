package org.keenusa.connect.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OnSmsIconClickListener implements OnClickListener {

	private Context context;

	public OnSmsIconClickListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		String phone = v.getTag().toString();
		if (phone != null && !phone.isEmpty()) {
			// TODO change for actual phone string
			Log.i("SMS_INTENT", "SMS is to be initialted to " + phone);
			Uri smsUri = Uri.parse("tel:" + "111-000-00");
			Intent i = new Intent(Intent.ACTION_VIEW, smsUri);
			i.putExtra("address", "111-000-00");
			i.putExtra("sms_body", "Hello");
			i.setType("vnd.android-dir/mms-sms");
			context.startActivity(i);
		} else {
			Toast.makeText(context, "Can not send sms. The phone number is invalid", Toast.LENGTH_SHORT).show();
		}
	}
}
