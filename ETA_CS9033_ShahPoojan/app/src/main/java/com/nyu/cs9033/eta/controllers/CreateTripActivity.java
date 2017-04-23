package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.Helpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class CreateTripActivity extends Activity {
	
	private static final String TAG = "CreateTripActivity";
	static final int ADD_REQUEST = 1;
	static final int LOCATION_REQUEST = 2;
	HashSet<String> numbers = new HashSet<String>();
	private ArrayList<Person> persons = new ArrayList<Person>();
	public int indicator = 0;
	private Trip trip = new Trip();
	public Button createTrip;

	EditText tripName, tripLocation, tripTime, tripDate, tripParticipants, tripMeetSpot, tripInterest;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);

		createTrip = (Button) findViewById(R.id.trip_submit);

		tripName = (EditText) findViewById(R.id.trip_name);
		tripLocation = (EditText) findViewById(R.id.location);
		tripInterest = (EditText) findViewById(R.id.interest);
		tripDate = (EditText) findViewById(R.id.date);
		tripTime = (EditText) findViewById(R.id.time);
		tripParticipants = (EditText) findViewById(R.id.participants);
		tripMeetSpot = (EditText) findViewById(R.id.meeting_spot);

		createTrip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				trip = createTrip();
				try {
					printJSON();
				} catch (JSONException exception) {
					exception.printStackTrace();
				}
				saveTrip(trip);
			}
		});

		final Button cancelTrip = (Button) findViewById(R.id.trip_cancel);
		cancelTrip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cancelTrip();
			}
		});


		final Button addParticipant = (Button) findViewById(R.id.add_participant);
		addParticipant.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addParticipant();
			}
		});

		final Button searchLocation = (Button) findViewById(R.id.search_locn);
		searchLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final EditText tripLocation = (EditText) findViewById(R.id.location);
				String location = tripLocation.getText().toString();

				final EditText tripInterest = (EditText) findViewById(R.id.interest);
				String tripDetails = tripInterest.getText().toString();

				if (location.equals("") || tripDetails.equals("")) {
					Toast.makeText(getBaseContext(), "Enter Location and Interest", Toast.LENGTH_LONG).show();
				} else if (indicator == 0) {
					searchLocation();
					indicator = 1;
				} else if (indicator == 1) {
					tripLocation.setText("");
					tripLocation.setEnabled(true);

					tripInterest.setText("");
					tripInterest.setEnabled(true);

					indicator = 0;
				}
			}
		});

		final Button getID = (Button) findViewById(R.id.get_id);
		getID.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(tripName.getText().toString().matches("") || tripLocation.getText().toString().matches("") || tripInterest.getText().toString().matches("")
						|| tripDate.getText().toString().matches("") || tripTime.getText().toString().matches("")
						|| tripParticipants.getText().toString().matches("") || tripMeetSpot.getText().toString().matches("")) {
					Toast.makeText(getApplicationContext(),"Enter all fields to generate trip ID", Toast.LENGTH_SHORT).show();

				}
				else {
					testConnect();
					getID.setEnabled(false);
				}
			}
		});
	}
	
	/**
	 * This method should be used to
	 * instantiate a Trip model object.
	 * 
	 * @return The Trip as represented
	 * by the View.
	 */
	public Trip createTrip() {

		//Trip trip = new Trip();

		EditText name = (EditText) findViewById(R.id.trip_name);
		String trip_name = name.getText().toString();
		trip.setName(trip_name);

		EditText location = (EditText) findViewById(R.id.location);
		String trip_location = location.getText().toString();
		trip.setLocation(trip_location);

		EditText date = (EditText) findViewById(R.id.date);
		String trip_date = date.getText().toString();
		trip.setTravelDate(trip_date);

		EditText time = (EditText) findViewById(R.id.time);
		String trip_time = time.getText().toString();
		trip.setTravelTime(trip_time);

		EditText meeting_spot = (EditText) findViewById(R.id.meeting_spot);
		String meet_spot = meeting_spot.getText().toString();
		trip.setMeetSpot(meet_spot);

		/*EditText participants = (EditText) findViewById(R.id.participants);
		String trip_participants = participants.getText().toString();
		String[] friendsArray;
		int numOfParticipants = 0;
		ArrayList<Person> friends = new ArrayList<Person>();
		if(trip_participants.equals("")) {
			friends = null;
		} else {
			friendsArray = trip_participants.split(",");
			int arrayLength = friendsArray.length;
			for(int i=0; i<arrayLength; i++) {
				Person person = new Person(friendsArray[i].trim());
				friends.add(person);
				numOfParticipants++;
			}
		}*/

		/*Trip trip = new Trip(trip_name, 1001, numOfParticipants, trip_location, trip_time, trip_date,
				friends, meet_spot);*/

		trip.setPersons(persons);
		return trip;
	}

	/**
	 * For HW2 you should treat this method as a 
	 * way of sending the Trip data back to the
	 * main Activity.
	 * 
	 * Note: If you call finish() here the Activity 
	 * will end and pass an Intent back to the
	 * previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully 
	 * saved.
	 */
	public boolean saveTrip(Trip trip) {

		if(trip.getName().equals("") || trip.getLocation().equals("") || trip.getTravelDate().equals("")
				|| trip.getTravelTime().equals("") || trip.getMeetSpot().equals("") ||
				trip.getPersons() == null) {
			Toast.makeText(getBaseContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();
		} else {
			Log.d("Saving Trip", trip.toString());
			Intent intent = new Intent();
			intent.putExtra("TripData", trip);
			setResult(RESULT_OK, intent);

			TripDatabaseHelper databaseHelper = new TripDatabaseHelper(getBaseContext());
			databaseHelper.insertTrip(trip);
			databaseHelper.insertParticipants(trip, trip.getTripID());
			//Log.d("Trip created with id: ", Long.toString(tripID));
			finish();
		}

		return true;
	}

	/**
	 * This method should be used when a
	 * user wants to cancel the creation of
	 * a Trip.
	 * 
	 * Note: You most likely want to call this
	 * if your activity dies during the process
	 * of a trip creation or if a cancel/back
	 * button event occurs. Should return to
	 * the previous activity without a result
	 * using finish() and setResult().
	 */
	public void cancelTrip() {
	
		// TODO - fill in here
		Intent intent = new Intent();
		setResult(RESULT_CANCELED,intent);
		finish();
	}

	public void addParticipant() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(pickContactIntent, ADD_REQUEST);
	}

	private void searchLocation(){

		Uri locationURI =  Uri.parse("location://com.example.nyu.hw3api");
		Intent searchLocation = new Intent(Intent.ACTION_VIEW, locationURI);

		final EditText tripLocation = (EditText) findViewById(R.id.location);
		String location = tripLocation.getText().toString();
		tripLocation.setEnabled(false);

		final EditText tripInterest = (EditText) findViewById(R.id.interest);
		String tripDetails = tripInterest.getText().toString();
		tripInterest.setEnabled(false);

		String searchString = location + "::" + tripDetails;
		Log.d(TAG, "Search string: " + searchString);
		searchLocation.putExtra("searchVal", searchString);
		Log.d(TAG, "Search string: " + searchString);
		startActivityForResult(searchLocation, LOCATION_REQUEST);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			cancelTrip();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if((resultCode != Activity.RESULT_OK) && (resultCode != Activity.RESULT_FIRST_USER)) {
			return;
		}

		Log.d(TAG, "Request code: " + requestCode);
		Log.d(TAG, "Result code: " + resultCode);

		if(requestCode == ADD_REQUEST) {

			Uri contactUri = data.getData();
			String[] queryFields = new String[]{
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.DATA1};

			Cursor cursor = this.getContentResolver().query(contactUri, queryFields, null, null, null);

			// null check
			if (cursor.getCount() == 0) {
				cursor.close();
				return;
			}

			Person person = new Person();

			final TextView participants = (TextView) findViewById(R.id.participants);
			cursor.moveToFirst();
			String[] temp = cursor.getColumnNames();
			Log.d(TAG, "Last entry: " + cursor.getString(temp.length - 1));
			Log.d(TAG, "Person name: " +cursor.getString(0));
			Log.d(TAG, "Contact number: " +cursor.getString(1));

			String personName = cursor.getString(0);
			String number = cursor.getString(1);

			//add it to the list
			//attach person name to participants display
			if(!numbers.contains(number))
			{
				if(numbers.isEmpty()) {
					participants.setText(participants.getText() + personName);
				} else {
					participants.setText(participants.getText() + ", " + personName);
				}
				numbers.add(number);
				person.setName(personName);
				person.setPhoneNum(number);
				persons.add(person);
				Toast.makeText(getBaseContext(), "Contact Added", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(getBaseContext(), personName +" is already present in the participants list.", Toast.LENGTH_LONG).show();
			}
			cursor.close();
		}
		if(requestCode == LOCATION_REQUEST) {
			ArrayList locationList = data.getStringArrayListExtra("retVal");
			Log.d(TAG, locationList.toString());
			Log.d(TAG, "Location: " + locationList.get(0));

			String location = locationList.get(0).toString();
			trip.setLocation(location);

			/*String locationAddress = locationList.get(1).toString();
			trip.setLocationAddress(locationAddress);
			Log.d(TAG, "Location Address is: "+ locationAddress);*/

			double latitude = Double.parseDouble(locationList.get(2).toString());
			trip.setLatitude(latitude);

			double longitude = Double.parseDouble(locationList.get(3).toString());
			trip.setLongitude(longitude);

			final TextView getLocation = (TextView)findViewById(R.id.location);
			getLocation.setText(location);

			Toast.makeText(getBaseContext(), "You have selected "+ locationList.get(0)+ " for your trip", Toast.LENGTH_LONG).show();

		}
	}

	public void printJSON() throws JSONException {
		JSONObject jsonObject = toJSON();
		Log.d(TAG,"JSON object: " + jsonObject);
	}

	public JSONObject toJSON() throws JSONException {

		trip.setPersons(persons);

		JSONObject jsonObject = new JSONObject();
		ArrayList<String> locations = new ArrayList<String>();
		locations.add(0,trip.getLocation());
		locations.add(1,trip.getLatitude() + "");
		locations.add(2,trip.getLongitude() + "");

		ArrayList<String> friends = new ArrayList<String>();
		for(int i=0; i<trip.getPersons().size(); i++){
			friends.add(i, trip.getPersons().get(i).getName());
		}

		String time = trip.getTravelDate();
		int year=0, month=0, day=0;
		int count = 0, lastIndex=0;
		for(int i=0; i<time.length(); i++) {
			if(time.charAt(i) == '/' && count == 0) {
				Log.d("Month", time.substring(0,i-1));
				month = Integer.valueOf(time.substring(0,i));
				count++;
				lastIndex = i;
			} else if(time.charAt(i) == '/' && count == 1) {
				Log.d("Day",time.substring(lastIndex+1, i-1));
				day = Integer.valueOf(time.substring(lastIndex+1, i));
				Log.d("Year",time.substring(i+1, time.length()-1));
				year = Integer.valueOf(time.substring(i+1, time.length()));
				break;
			}
		}
		Calendar tripCalendarTime = Calendar.getInstance();
		tripCalendarTime.set(year, month, day);
		long datetime = tripCalendarTime.getTimeInMillis();
		Log.d(TAG,"Time in millis: "+datetime);

		jsonObject.put("command","CREATE_TRIP");
		jsonObject.put("location",locations);
		jsonObject.put("datetime", datetime);
		jsonObject.put("people",friends);

		return jsonObject;
	}

	public void testConnect(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if(networkInfo != null && networkInfo.isConnected()){
			Log.d(TAG,"Connected");
			new DownloadTripService(this).execute("http://cs9033-homework.appspot.com/");
		}
	}

	public void enableCreation(){
		createTrip.setEnabled(true);
		createTrip.setText("Create Trip");
	}

	private class DownloadTripService extends AsyncTask<String,Void,String> {

		public CreateTripActivity activity;
		public DownloadTripService(CreateTripActivity act) {
			activity = act;
		}

		@Override
		protected String doInBackground(String... urls) {

			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				String a = e.toString();
				return "Unable to open URL";
			}
		}

		@Override

		protected void onPostExecute(String result) {

			Log.d("In post execute","The result : "+ result);
			try {
				JSONObject obj = new JSONObject(result);
				Log.d("JSON obj: ",obj.toString());
				String str = obj.getString("trip_id");
				long id = Long.parseLong(str);
				Log.d("Response from service", "The trip id is: " + id);
				trip.setTripID(id);
				activity.enableCreation();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		public String downloadUrl(String inURL) throws IOException {
			URL url = new URL(inURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				OutputStream outputStream = connection.getOutputStream();
				trip = createTrip();
				JSONObject jsonObject = toJSON();
				byte[] array;
				array = jsonObject.toString().getBytes();
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
