package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.Helpers.UpdateLocationHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewTripActivity extends Activity {

	private static final String TAG = "ViewTripActivity";
	public static final String PREF_TRIP_ID = "tripID";
	public static final String PREF_TRIP_NAME = "tripName";
	private Trip trip;
	private String tripID;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		// TODO - fill in here

		trip = getTrip(getIntent());
		tripID = Long.toString(trip.getTripID());
		viewTrip(trip);


		final Button startTrip = (Button) findViewById(R.id.start_trip);
		startTrip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String prefTripID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
						.getString(ViewTripActivity.PREF_TRIP_ID, null);

				String prefTripName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
						.getString(ViewTripActivity.PREF_TRIP_NAME, null);

				if (prefTripID == null || "-1".matches(prefTripID) || prefTripName != null) {

					setPrefs();

					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putExtra("currentTrip", trip);

					UpdateLocationHelper updatePhoneLocation = new UpdateLocationHelper(ViewTripActivity.this);
					double latitude = updatePhoneLocation.getLatitude();
					double longitude = updatePhoneLocation.getLongitude();
					startActivity(intent);
					Toast.makeText(getApplicationContext(), trip.getName() + " started", Toast.LENGTH_SHORT).show();
					finish();

				} else {
					Log.d(TAG, "The Shared Preference is: " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
							.getString(ViewTripActivity.PREF_TRIP_ID, null));
					Toast.makeText(getApplicationContext(), "A trip is already in progress", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	public void setPrefs(){
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit()
				.putString(PREF_TRIP_ID, tripID)
				.commit();
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit()
				.putString(PREF_TRIP_NAME, trip.getName())
				.commit();
	}

	/**
	 * Create a Trip object via the recent trip that
	 * was passed to TripViewer via an Intent.
	 *
	 * @param i The Intent that contains
	 * the most recent trip data.
	 *
	 * @return The Trip that was most recently
	 * passed to TripViewer, or null if there
	 * is none.
	 */
	public Trip getTrip(Intent i) {

		// TODO - fill in here
		Trip trip = i.getParcelableExtra("TripObject");
		return trip;
	}

	/**
	 * Populate the View using a Trip model.
	 *
	 * @param trip The Trip model used to
	 * populate the View.
	 */
	public void viewTrip(Trip trip) {

		// TODO - fill in here
		ArrayList<Person> friends = trip.getPersons();
		String participants = "";
		int length = friends.size();

		for(int i=0; i<length; i++) {
			if(i == length-1) {
				participants = participants + friends.get(i).getName() + "[" + friends.get(i).getPhoneNum() + "]";
			} else {
				participants = participants + friends.get(i).getName() + "[" + friends.get(i).getPhoneNum() + "]" + ", ";
			}
		}

		TextView name = (TextView) findViewById(R.id.viewName);
		name.setText(trip.getName());

		TextView location = (TextView) findViewById(R.id.viewLocation);
		location.setText(trip.getLocation());

		TextView date = (TextView) findViewById(R.id.viewDate);
		date.setText(trip.getTravelDate());

		TextView time = (TextView) findViewById(R.id.viewTime);
		time.setText(trip.getTravelTime());

		TextView meetSpot = (TextView) findViewById(R.id.viewMeetSpot);
		meetSpot.setText(trip.getMeetSpot());

		TextView listOfFriends = (TextView) findViewById(R.id.viewParticipants);
		listOfFriends.setText(participants);

	}
}

