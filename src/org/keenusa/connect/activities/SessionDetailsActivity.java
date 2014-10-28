package org.keenusa.connect.activities;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.data.daos.AthleteAttendanceDAO;
import org.keenusa.connect.data.daos.CoachAttendanceDAO;
import org.keenusa.connect.data.daos.ProgramEnrollmentDAO;
import org.keenusa.connect.data.daos.SessionDAO;
import org.keenusa.connect.fragments.MassMessageFragment;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.utilities.CheckinMenuActions;
import org.keenusa.connect.utilities.FastBlur;
import org.keenusa.connect.utilities.SetSessionImage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SessionDetailsActivity extends FragmentActivity {

	private TextView tvProgramName, tvLocation, tvDate, tvAttCoach, tvAttAthlete, tvProgramType, tvProgramTimes, tvAttCoaches, tvAttAthletes;
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	private ImageView image;
	private ProgressBar _progressBarCoach, _progressBarAthlete;

	private KeenSession session;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.activity_session_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setView();
		long sessionId = getIntent().getLongExtra("SESSION_ID", 0);
		new LoadSessionDataTask().execute(sessionId);
	}

	private void setData() {
		if (session != null) {
			String address = "";
			KeenProgram program = session.getProgram();

			tvDate.setText(formamtDate(session.getDate()));
			tvProgramName.setText(program.getName());
			//		changeBackgroundImage(program.getName());
			SetSessionImage.changeBackgroundImage(program.getName(), image);
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

			if (session.getRegisteredCoachCount() != 0) {
				_progressBarCoach.setProgress(100 * session.getCheckedInCoachCount() / session.getRegisteredCoachCount());
			} else {
				_progressBarCoach.setProgress(100);
			}
			_progressBarCoach.setMax(100);
			tvAttCoach.setText(session.getCheckedInCoachCount() + " / " + session.getRegisteredCoachCount());

			if (session.getRegisteredAthleteCount() != 0) {
				_progressBarAthlete.setProgress(100 * session.getCheckedInAthleteCount() / session.getRegisteredAthleteCount());
			} else {
				_progressBarAthlete.setProgress(100);
			}
			_progressBarAthlete.setMax(100);
			tvAttAthlete.setText(session.getCheckedInAthleteCount() + " / " + session.getRegisteredAthleteCount());
		}
	}

	private void changeBackgroundImage(String name) {
		if (name.equals("Sports 1")) {
			image.setImageResource(R.drawable.sports1);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Sports 2")) {
			image.setImageResource(R.drawable.sports2);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Sports 1 & 2")) {
			image.setImageResource(R.drawable.sports1tennis);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Basketball") || name.equals("Basketball Clinic - Hoops")
				|| name.equals("Summer Family Pool Party or Basketball & Picnic")) {
			image.setImageResource(R.drawable.basketballclinic);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("KEENquatics") || name.equals("KEENquatics - SWIM")) {
			image.setImageResource(R.drawable.quatics);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Summer Family Picnic & Games")) {
			image.setImageResource(R.drawable.picnic);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Kids Sports & Tennis - SFUHS")) {
			image.setImageResource(R.drawable.sports1tennis);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Kids and Young Adult Sports at YMCA") || name.equals("Kids Sports and Young Adult Sports - East Bay")) {
			image.setImageResource(R.drawable.ymca);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("Holiday Party 2012")) {
			image.setImageResource(R.drawable.holidayparty);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		} else if (name.equals("KEENGala: Pre-Event") || name.equals("KEENGala - Event Volunteer Shift 1")
				|| name.equals("KEENGala - Event Volunteer Shift 2")) {
			image.setImageResource(R.drawable.holidayparty);
			//			blurImage(tvDate, tvProgramName, tvLocation, tvProgramType, tvProgramTimes);
		}
	}

	//	private void blurImage(final TextView tvDate, final TextView tvProgramName, final TextView tvLocation, final TextView tvProgramType,
	//			final TextView tvProgramTimes) {
	//		image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
	//
	//			@Override
	//			public boolean onPreDraw() {
	//				image.getViewTreeObserver().removeOnPreDrawListener(this);
	//				image.buildDrawingCache();
	//
	//				Bitmap bmp = image.getDrawingCache();
	//				blur(bmp, tvDate);
	//				blur(bmp, tvProgramName);
	//				blur(bmp, tvLocation);
	//				blur(bmp, tvProgramType);
	//				blur(bmp, tvProgramTimes);
	//				return true;
	//			}
	//		});
	//	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void blur(Bitmap bkg, TextView textView) {
		float scaleFactor = 1;
		float radius = 20;

		Bitmap overlay = Bitmap.createBitmap((int) (textView.getMeasuredWidth() / scaleFactor), (int) (textView.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-textView.getLeft() / scaleFactor, -textView.getTop() / scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = FastBlur.doBlur(overlay, (int) radius, true);
		textView.setBackground(new BitmapDrawable(getResources(), overlay));
	}

	private CharSequence formamtDate(DateTime date) {
		DateTime dateTime = session.getDate();
		String result = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(dateTime.toDate());
		return result;
	}

	private void setView() {
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
	}

	//	private TextView addStatusText(ViewGroup container) {
	//		TextView result = new TextView(this);
	//		result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	//		result.setTextColor(0xFFFFFFFF);
	//		container.addView(result);
	//		return result;
	//	}

	public void athleteCheckIn(View v) {
		openAthleteCheckIn(session, session.getProgram());
	}

	public void coachCheckIn(View v) {
		openCoachCheckIn(session, session.getProgram());
	}

	private void openCoachCheckIn(KeenSession session2, KeenProgram program2) {
		Intent i = new Intent(this, CoachesCheckInActivity.class);
		i.putExtra("session", session2);
		i.putExtra("program", program2);
		startActivity(i);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}

	private void openAthleteCheckIn(KeenSession session2, KeenProgram program2) {
		Intent i = new Intent(this, AthleteCheckInActivity.class);
		i.putExtra("session", session2);
		i.putExtra("program", program2);
		startActivity(i);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
			CheckinMenuActions.editMode = false;
			CheckinMenuActions.sendMassMessages = true;
			Intent checkinIntent = new Intent(getBaseContext(), AthleteCoachCheckinActivity.class);
			checkinIntent.putExtra("SESSION_ID", session.getId());
			startActivity(checkinIntent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		} else if (item.getItemId() == R.id.miSendMessage) {
			showMassMessageDialog();
		} else if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showMassMessageDialog() {
		DialogFragment newFragment = new MassMessageFragment(session.getProgram().getProgramEnrolments(), session.getCoachAttendance());
		newFragment.show(getSupportFragmentManager(), "Mass Message Dialog");
	}

	private class LoadSessionDataTask extends AsyncTask<Long, Void, KeenSession> {

		@Override
		protected void onPreExecute() {
			//			if (llProgressBar != null) {
			//				llProgressBar.setVisibility(View.VISIBLE);
			//			}
		}

		@Override
		protected KeenSession doInBackground(Long... params) {
			SessionDAO sessionDAO = new SessionDAO(SessionDetailsActivity.this);
			KeenSession session = sessionDAO.getSessionById(params[0]);

			ProgramEnrollmentDAO peDAO = new ProgramEnrollmentDAO(SessionDetailsActivity.this);
			List<KeenProgramEnrolment> enrolments = peDAO.getKeenProgramEnrollments(session.getProgram().getId());
			session.getProgram().setProgramEnrolments(enrolments);

			AthleteAttendanceDAO aaDAO = new AthleteAttendanceDAO(SessionDetailsActivity.this);
			List<AthleteAttendance> aAttehndances = aaDAO.getAthleteAttendancesBySessionId(session.getId());
			session.setAthleteAttendance(aAttehndances);

			CoachAttendanceDAO caDAO = new CoachAttendanceDAO(SessionDetailsActivity.this);
			List<CoachAttendance> cAttehndances = caDAO.getCoachAttendancesBySessionId(session.getId());
			session.setCoachAttendance(cAttehndances);

			return session;
		}

		@Override
		protected void onPostExecute(KeenSession dbSession) {
			//			if (llProgressBar != null) {
			//				llProgressBar.setVisibility(View.GONE);
			//			}
			session = dbSession;
			setData();

		}

	}

}
