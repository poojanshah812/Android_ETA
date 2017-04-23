package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.Helpers.UpdateLocationHelper;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	Trip trip = null;
	JSONObject obj;
	TextView ongoingTrip;
	Button stopTrip, markArrived;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		boolean t = getIntent().hasExtra("currentTrip");
		if (((PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.getString(ViewTripActivity.PREF_TRIP_ID, null)) != null) && !(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.getString(ViewTripActivity.PREF_TRIP_ID, null)).equals("-1")) {

			Log.d(TAG, "The Shared Preference is:  " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.getString(ViewTripActivity.PREF_TRIP_ID, null));
			testConnect();
			trip = getIntent().getParcelableExtra("currentTrip");


		}

		ongoingTrip = (TextView) findViewById(R.id.ongoing_trip);
		stopTrip = (Button) findViewById(R.id.stop_trip);
		markArrived = (Button) findViewById(R.id.mark_arrived);

		stopTrip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setPrefs();
				ongoingTrip.setVisibility(View.INVISIBLE);
				stopTrip.setVisibility(View.INVISIBLE);
				markArrived.setVisibility(View.INVISIBLE);
				UpdateLocationHelper updateLocationHelper = new UpdateLocationHelper(MainActivity.this);
				updateLocationHelper.stopUsingGPS();
			}
		});

		markArrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				UpdateLocationHelper updateLocationHelper = new UpdateLocationHelper(MainActivity.this);
				double latitude = updateLocationHelper.getLatitude();
				double longitude = updateLocationHelper.getLongitude();
				Log.d(TAG,"The lat long is:   "+ latitude +"," + longitude);
				updateLocationHelper.stopUsingGPS();
			}
		});

	}

	public void testConnect() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadTripService(this).execute("http://cs9033-homework.appspot.com/");//http://cs9033-homework.appspot.com
		}
	}

	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startCreateTripActivity(View view) {

		Intent intent = new Intent(this, CreateTripActivity.class);
		startActivityForResult(intent, 1);

		// TODO - fill in here
	}

	public void setPrefs(){
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit()
				.putString(ViewTripActivity.PREF_TRIP_ID, "-1")
				.commit();
	}
	
	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startViewTripActivity(View view) {

		Intent intent = new Intent(this, ViewTripActivity.class);
		if(trip == null)
		{
			Toast.makeText(getBaseContext(), "No Trip Created", Toast.LENGTH_LONG).show();
		}
		else
		{
			intent.putExtra("TripObject",trip);
			startActivity(intent);
		}

	}

	public void startViewTripListActivity(View view) {
		Intent intent = new Intent(this, TripHistoryActivity.class);
		//if(trip == null)
		//{
			//Toast.makeText(getBaseContext(), "No Trip Created", Toast.LENGTH_LONG).show();
		//} else {
			startActivity(intent);
		//}
	}

	public void displayCurrentTrip(){

		JSONArray people;
		JSONArray time;
		JSONArray location;
		try {
			people = obj.getJSONArray("people");
			time = obj.getJSONArray("time_left");
			location = obj.getJSONArray("distance_left");

			//String temp = people.;
			ArrayList<String> tempPeople = new ArrayList<String> ();
			ArrayList<String> tempTime = new ArrayList<String>();
			ArrayList<String> tempLocation = new ArrayList<String>();
			String tripName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.getString(ViewTripActivity.PREF_TRIP_NAME, null);

			ongoingTrip.setVisibility(View.VISIBLE);
			stopTrip.setVisibility(View.VISIBLE);
			markArrived.setVisibility(View.VISIBLE);

			ongoingTrip.append("Ongoing Trip: "+ tripName + "\n");
			ongoingTrip.append("\n");

			for(int i =0; i<people.length();i++){
				tempPeople.add(people.get(i).toString());
				tempLocation.add(location.get(i).toString());
				int newTime = Integer.parseInt(time.get(i).toString());
				float readTime = newTime/60;
				tempTime.add(readTime+"");
				ongoingTrip.append(people.get(i).toString()+" is " + location.get(i).toString()+" miles away and will arrive in " + readTime + " minutes. \n");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receive result from CreateTripActivity here.
	 * Can be used to save instance of Trip object
	 * which can be viewed in the ViewTripActivity.
	 * 
	 * Note: This method will be called when a Trip
	 * object is returned to the main activity. 
	 * Remember that the Trip will not be returned as
	 * a Trip object; it will be in the persisted
	 * Parcelable form. The actual Trip object should
	 * be created and saved in a variable for future
	 * use, i.e. to view the trip.
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO - fill in here
		super.onActivityResult(requestCode,resultCode,data);

		if(requestCode == 1)
		{
			if (resultCode == RESULT_OK)
			{
				trip = data.getParcelableExtra("TripData");
				Log.d("onResult", "Trip name: " 	+ trip.getName());
				Toast.makeText(getBaseContext(),"Trip created successfully",Toast.LENGTH_LONG).show();
			}
			else if(resultCode == RESULT_CANCELED)
			{
				Toast.makeText(getBaseContext(),"Error creating trip",Toast.LENGTH_LONG).show();
			}
		}
	}

	private class DownloadTripService extends AsyncTask<String, Void, String> {

		public MainActivity activity;

		public DownloadTripService(MainActivity act){
			this.activity = act;
		}

		@Override
		protected String doInBackground(String... urls) {

			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				return "Unable to open URL";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				obj = new JSONObject(result);
				Toast.makeText(getApplicationContext(), "Results Fetched", Toast.LENGTH_SHORT);
				activity.displayCurrentTrip();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public JSONObject toJSON() throws JSONException {

			String tripID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.getString(ViewTripActivity.PREF_TRIP_ID, null);
			JSONObject json = new JSONObject();
			json.put("command", "TRIP_STATUS");
			json.put("trip_id", tripID);


			return json;
		}

		public String downloadUrl(String inURL) throws IOException {
			URL url = new URL(inURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				OutputStream outputStream = connection.getOutputStream();
				JSONObject temp = toJSON();
				String test = temp.toString();
				byte[] array;
				array = temp.toString().getBytes();
				outputStream.write(array);
				InputStream in = connection.getInputStream();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return null;
				}

				int bytesRead = 0;
				byte[] buffer = new byte[1024];


				while ((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}

				out.close();
				String str = new String(out.toByteArray());
				return new String(out.toByteArray());

			} catch (JSONException e) {
				e.printStackTrace();
				return "JSON exception caught";
			} finally {
				connection.disconnect();
			}

		}

	}
}
