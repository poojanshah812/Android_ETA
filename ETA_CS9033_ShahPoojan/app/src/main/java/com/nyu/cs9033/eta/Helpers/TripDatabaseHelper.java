package com.nyu.cs9033.eta.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;


public class TripDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "ETA_DATABASE_NEW";

    //Trips table
    public static final String TRIPS_DATABASE_NAME = "TRIPS";
    public static final String COLUMN_TRIP_ID = "_id";
    public static final String COLUMN_TRIP_NAME = "trip_name";
    public static final String COLUMN_TRIP_LOCATION = "trip_locn";
    public static final String COLUMN_TRIP_DATE = "trip_date";
    public static final String COLUMN_TRIP_TIME = "trip_time";
    public static final String COLUMN_TRIP_MEET_SPOT = "meet_spot";
    public static final String COLUMN_TRIP_PARTIC_NUM = "partic_num";
    private static final String TRIPS_DATABASE_CREATE =
            "CREATE TABLE " + TRIPS_DATABASE_NAME + " ( " +
                    COLUMN_TRIP_ID + " int, " +
                    COLUMN_TRIP_NAME + " varchar(100), " +
                    COLUMN_TRIP_LOCATION + " text, " +
                    COLUMN_TRIP_DATE + " date, " +
                    COLUMN_TRIP_TIME + " time, " +
                    COLUMN_TRIP_MEET_SPOT + " text, " +
                    COLUMN_TRIP_PARTIC_NUM + " int" + ")";


    //Participants table
    public static final String PARTICIPANTS_DATABASE_NAME = "PARTICIPANTS";
    //public static final String COLUMN_PART_ID = "part_id";
    public static final String COLUMN_PART_TRIP_ID = "trip_id";
    public static final String COLUMN_PART_NAME = "name";
    public static final String COLUMN_PART_NUM = "phone_num";
    public static final String COLUMN_PART_LAT = "latitude";
    public static final String COLUMN_PART_LONG = "longitude";
    public static final String COLUMN_PART_DEST = "destination";
    private static final String PARTICIPANTS_DATABASE_CREATE =
            "CREATE TABLE " + PARTICIPANTS_DATABASE_NAME + "( " +
                    COLUMN_PART_TRIP_ID + " integer references " + TRIPS_DATABASE_NAME + "(" +COLUMN_TRIP_ID + "), " +
                    COLUMN_PART_NAME + " varchar(20), " +
                    COLUMN_PART_NUM + " varchar(12), " +
                    COLUMN_PART_LAT + " real, " +
                    COLUMN_PART_LONG + " real, " +
                    COLUMN_PART_DEST + " text" + ")";

    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ";
    private static final String FETCH_TRIPS_QUERY = "SELECT * FROM " + TRIPS_DATABASE_NAME;
    private static final String FETCH_PARTICIPANTS_QUERY = "SELECT * FROM " + PARTICIPANTS_DATABASE_NAME +
            " WHERE " + COLUMN_PART_TRIP_ID + " = ";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TRIPS_DATABASE_CREATE);
        sqLiteDatabase.execSQL(PARTICIPANTS_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY+TRIPS_DATABASE_NAME);
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY+PARTICIPANTS_DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public TripDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long insertTrip(Trip trip) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRIP_NAME, trip.getName());
        values.put(COLUMN_TRIP_LOCATION, trip.getLocation());
        values.put(COLUMN_TRIP_DATE, trip.getTravelDate());
        values.put(COLUMN_TRIP_TIME, trip.getTravelTime());
        values.put(COLUMN_TRIP_MEET_SPOT, trip.getMeetSpot());
        values.put(COLUMN_TRIP_PARTIC_NUM, trip.getPersons().size());
        values.put(COLUMN_TRIP_ID, trip.getTripID());
        return getWritableDatabase().insert(TRIPS_DATABASE_NAME, null, values);
    }

    public void insertParticipants(Trip trip, long trip_id) {
        ContentValues values = new ContentValues();

        ArrayList<Person> participants = trip.getPersons();
        for(int i=0; i<participants.size(); i++) {
            values.put(COLUMN_PART_TRIP_ID, trip_id);
            values.put(COLUMN_PART_NAME, participants.get(i).getName());
            values.put(COLUMN_PART_NUM, participants.get(i).getPhoneNum());
            values.put(COLUMN_PART_DEST, trip.getLocation());
            getWritableDatabase().insert(PARTICIPANTS_DATABASE_NAME, null, values);
        }
    }

    public Cursor fetchTrips() {
        //ArrayList<Trip> trips = new ArrayList<Trip>();
        Cursor cursor = getReadableDatabase().rawQuery(FETCH_TRIPS_QUERY, null);
        /*if(cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.setTripID(Long.parseLong(cursor.getString(0)));
                trip.setName(cursor.getString(1));
                trip.setLocation(cursor.getString(2));
                trip.setTravelDate(cursor.getString(3));
                trip.setTravelTime(cursor.getString(4));
                trip.setMeetSpot(cursor.getString(5));
                trip.setNumOfParticipants(Integer.parseInt(cursor.getString(6)));
                trips.add(trip);
            } while(cursor.moveToNext());
        }*/
        return cursor;
    }

    public Cursor fetchParticipants(long tripId) {
        return getReadableDatabase().rawQuery(FETCH_PARTICIPANTS_QUERY+tripId,null);
    }
}
