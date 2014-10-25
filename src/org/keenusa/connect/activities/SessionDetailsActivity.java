package org.keenusa.connect.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.fragments.MassMessageFragment;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.utilities.CheckinEditMode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SessionDetailsActivity extends FragmentActivity {
	// private TextView tvProgramNameLabel, tvLocationLabel, tvDateLabel,
	// tvProgramTypeLabel, tvProgramActiveDateLabel, tvAttCoachesLabel,
	// tvAttAthletesLabel;
	private TextView tvProgramName, tvLocation, tvDate, tvAttCoach, tvAttAthlete, tvProgramType, tvProgramTimes, tvAttCoaches, tvAttAthletes;
	private Button btnCoachChkIn, btnAthleteChkIn;
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	private ImageView image;
	ProgressBar _progressBarCoach, _progressBarAthlete;

	KeenSession session;
	KeenProgram program;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.activity_session_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setView();
		setData();
//		applyBlur();
	}

//    private void applyBlur() {
//		Bitmap tempbg = BitmapFactory.decodeResource(getResources(), R.drawable.sports_two);
//		Bitmap final_Bitmap = BlurImage(tempbg);
//	}
//
//    @SuppressLint("NewApi")
//	Bitmap BlurImage(Bitmap input) {
//		try {
//			RenderScript rs = RenderScript.create(getApplicationContext());
//			Allocation alloc = Allocation.createFromBitmap(rs, input);
//			
//			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//			blur.setRadius(21);
//			blur.setInput(alloc);
//			
//			Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
//			Allocation outAlloc = Allocation.createFromBitmap(rs, result);
//			
//			blur.forEach(outAlloc);
//			outAlloc.copyTo(result);
//			rs.destroy();
//			
//			return result;
//		}catch (Exception e) {
//			return input;
//		}
//	}

	private void setData() {
		String address = "";
		session = (KeenSession) getIntent().getSerializableExtra("session");
		program = (KeenProgram) getIntent().getSerializableExtra("program");
		
		tvDate.setText(formamtDate(session.getDate()));
		tvProgramName.setText(program.getName());
		changeBackgroundImage(program.getName());
		if (program.getLocation() != null) {
			tvLocation.setText(program.getLocation().getLocationString());
		} else {
			tvLocation.setVisibility(View.GONE);
		}
		if (program.getGeneralProgramType() != null) {
			tvProgramType.setText(program.getGeneralProgramType().toString());
		} else {
			tvProgramType.setVisibility(View.GONE);
		}
		if (session.getProgram().getProgramTimes() != null) {
			tvProgramTimes.setText(session.getProgram().getProgramTimes());
		} else {
			tvProgramTimes.setVisibility(View.GONE);
		}
		//		tvAttCoaches.setText(session.getRegisteredCoachCount() + "");
		//		tvAttAthletes.setText(session.getRegisteredAthleteCount() + "");

		// TODO - fix java.lang.ClassCastException: android.widget.TextView cannot be cast to android.widget.ProgressBar

		_progressBarCoach.setProgress(session.getRegisteredCoachCount());
		tvAttCoach.setText(session.getRegisteredCoachCount() + "");

		_progressBarAthlete.setProgress(session.getRegisteredAthleteCount());
		tvAttAthlete.setText(session.getRegisteredAthleteCount() + "");
		
		
	}

	private void changeBackgroundImage(String name) {
		if (name.equals("Sports 1")){
			image.setImageResource(R.drawable.sport1);
		} else if (name.equals("Sports 2")) {
			image.setImageResource(R.drawable.sport2);
		} else if (name.equals("Sports 1 & 2")) {
			image.setImageResource(R.drawable.sport12);
		} else if (name.equals("Basketball") || name.equals("Basketball Clinic - Hoops")) {
			image.setImageResource(R.drawable.basketball);
		} else if (name.equals("KEENquatics") || name.equals("KEENquatics - SWIM")) {
			image.setImageResource(R.drawable.quatics);
		} else if (name.equals("Summer Family Picnic & Games")) {
			image.setImageResource(R.drawable.picnic);
		} else if (name.equals("Kids Sports & Tennis - SFUHS")) {
			image.setImageResource(R.drawable.kidssport);
		} else if (name.equals("Kids and Young Adult Sports at YMCA") || name.equals("Kids Sports and Young Adult Sports - East Bay")) {
			image.setImageResource(R.drawable.ymca);
		} else if (name.equals("Basketball") || name.equals("Summer Family Pool Party or Basketball & Picnic")) {
			image.setImageResource(R.drawable.basketball);
		} else if (name.equals("Holiday Party 2012")) {
			image.setImageResource(R.drawable.holidayparty);
		} else if (name.equals("KEENGala: Pre-Event") || name.equals("KEENGala - Event Volunteer Shift 1") || name.equals("KEENGala - Event Volunteer Shift 2")) {
			image.setImageResource(R.drawable.holidayparty);
		} 
	}

	private CharSequence formamtDate(DateTime date) {
		DateTime dateTime = session.getDate();
		String result = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(dateTime.toDate());
		return result;
	}

	private void setView() {
		// tvProgramNameLabel = (TextView)
		// findViewById(R.id.tvProgramNameLabel);
		// tvLocationLabel = (TextView) findViewById(R.id.tvLocationLabel);
		// tvDateLabel = (TextView) findViewById(R.id.tvDateLabel);
		// tvProgramActiveDateLabel = (TextView)
		// findViewById(R.id.tvProgramActiveDateLabel);
		// tvProgramTypeLabel = (TextView)
		// findViewById(R.id.tvProgramTypeLabel);
		// tvAttCoachesLabel = (TextView) findViewById(R.id.tvAttCoachesLabel);
		// tvAttAthletesLabel = (TextView)
		// findViewById(R.id.tvAttAthletesLabel);
		//		btnCoachChkIn = (Button) findViewById(R.id.btnCoachChkIn);
		//		btnAthleteChkIn = (Button) findViewById(R.id.btnAthleteChkIn);

		tvProgramName = (TextView) findViewById(R.id.tvProgramName);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvProgramTimes = (TextView) findViewById(R.id.tvProgramTimes);
		tvProgramType = (TextView) findViewById(R.id.tvProgramType);
		_progressBarCoach = (ProgressBar) findViewById(R.id.cicularProgressBarCoach);
		_progressBarAthlete = (ProgressBar) findViewById(R.id.cicularProgressBarAthlete);
		tvAttCoach = (TextView) findViewById(R.id.tvAttCoach);
		tvAttAthlete = (TextView) findViewById(R.id.tvAttAthlete);
		image = (ImageView) findViewById(R.id.ivSessionBackgroundPic);
		//		tvAttCoaches = (TextView) findViewById(R.id.tvAttCoaches);
		//		tvAttAthletes = (TextView) findViewById(R.id.tvAttAthletes);
	}

	public void athleteCheckIn(View v) {
		openAthleteCheckIn(session, program);
	}

	public void coachCheckIn(View v) {
		openCoachCheckIn(session, program);
	}

	private void openCoachCheckIn(KeenSession session2, KeenProgram program2) {
		Intent i = new Intent(this, CoachesCheckInActivity.class);
		i.putExtra("session", session2);
		i.putExtra("program", program2);
		startActivity(i);
	}

	private void openAthleteCheckIn(KeenSession session2, KeenProgram program2) {
		Intent i = new Intent(this, AthleteCheckInActivity.class);
		i.putExtra("session", session2);
		i.putExtra("program", program2);
		startActivity(i);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.session_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.miShowList) {
			// Start the check-in activity
			CheckinEditMode.editMode = false;
			Intent checkinIntent = new Intent(getBaseContext(), AthleteCoachCheckinActivity.class);
			checkinIntent.putExtra("session", session);
			startActivity(checkinIntent);
			overridePendingTransition(R.anim.bottom_in, R.anim.top_out);

		} else if (item.getItemId() == R.id.miSendMessage) {
			showMassMessageDialog();
		} else if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showMassMessageDialog() {
		DialogFragment newFragment = new MassMessageFragment(program.getEnrolledAthletes(), session.getCoachAttendance());
		newFragment.show(getSupportFragmentManager(), "Mass Message Dialog");
	}

}
