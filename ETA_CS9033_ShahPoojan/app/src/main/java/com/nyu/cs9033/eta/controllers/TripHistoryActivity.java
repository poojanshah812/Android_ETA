package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.Helpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;


public class TripHistoryActivity extends Activity{

    Trip trip = new Trip();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_list);
        viewTrips();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void viewTrips() {

        final TripDatabaseHelper databaseHelper = new TripDatabaseHelper(getBaseContext());
        Cursor cursor = databaseHelper.fetchTrips();

        if(!cursor.moveToFirst()) {
            Toast.makeText(getBaseContext(), "No Trip Created", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        } else {
            //desired columns to bound
            String[] columns = new String[] {
                    TripDatabaseHelper.COLUMN_TRIP_ID,
                    TripDatabaseHelper.COLUMN_TRIP_NAME,
                    TripDatabaseHelper.COLUMN_TRIP_LOCATION
            };

            //XML views to be bound to the data
            int[] to = new int[] {
                    R.id.viewTripId,
                    R.id.viewTripName,
                    R.id.viewTripDest
            };

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                    this, R.layout.activity_view_trip_list,
                    cursor, columns, to, 0);

            final ListView listView = (ListView) findViewById(R.id.tripListView);
            listView.setAdapter(simpleCursorAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                    String tripName = cursor.getString(cursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_TRIP_NAME));
                    trip.setName(tripName);
                    trip.setTripID(cursor.getInt(cursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_TRIP_ID)));
                    trip.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_TRIP_LOCATION)));
                    trip.setTravelTime(cursor.getString(cursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_TRIP_TIME)));
                    trip.setTravelDate(cursor.getString(cursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_TRIP_DATE)));
                    trip.setMeetSpot(cursor.getString(cursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_TRIP_MEET_SPOT)));

                    ArrayList<Person> participants = new ArrayList<Person>();

                    Cursor partCursor = databaseHelper.fetchParticipants(trip.getTripID());
                    for(partCursor.moveToFirst(); !partCursor.isAfterLast(); partCursor.moveToNext()){
                        Person person = new Person();
                        person.setName(partCursor.getString(partCursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_PART_NAME)));
                        person.setPhoneNum(partCursor.getString(partCursor.getColumnIndexOrThrow(TripDatabaseHelper.COLUMN_PART_NUM)));
                        participants.add(person);
                    }

                    trip.setPersons(participants);
                    trip.setNumOfParticipants(participants.size());

                    Intent intent = new Intent(getBaseContext(), ViewTripActivity.class);
                    intent.putExtra("TripObject", trip);
                    //intent.putExtra("TripName", tripName);
                    intent.putExtra("tripID", tripName);
                    startActivity(intent);
                }
            });
        }


    }

}
