package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Trip implements Parcelable {
	
	// Member fields should exist here, what else do you need for a trip?
	// Please add additional fields
	private String name;
	private long tripID;
	private int numOfParticipants;
	private String location;
	private String travelTime;
	private String travelDate;
	private ArrayList<Person> persons;
	private String meetSpot;
	private double latitude;
	private double longitude;

	public String getMeetSpot() {
		return meetSpot;
	}

	public void setMeetSpot(String meetSpot) {
		this.meetSpot = meetSpot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTripID() {
		return tripID;
	}

	public void setTripID(long tripID) {
		this.tripID = tripID;
	}

	public int getNumOfParticipants() {
		return numOfParticipants;
	}

	public void setNumOfParticipants(int numOfParticipants) {
		this.numOfParticipants = numOfParticipants;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	public ArrayList<Person> getPersons() {
		return persons;
	}

	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Trip() {
	}

	/**
	 * Parcelable creator. Do not modify this function.
	 */
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
		public Trip createFromParcel(Parcel p) {
			return new Trip(p);
		}

		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};
	
	/**
	 * Create a Trip model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Trip(Parcel p) {
		Log.d("Parcel object: ", p.toString());
		this.name = p.readString();
		this.tripID = p.readLong();
		this.numOfParticipants = p.readInt();
		this.location = p.readString();
		this.travelTime = p.readString();
		this.travelDate = p.readString();
		this.persons = p.createTypedArrayList(Person.CREATOR);
		this.meetSpot = p.readString();
		this.latitude = p.readDouble();
		this.longitude = p.readDouble();

		// TODO - fill in here
	}
	
	/**
	 * Create a Trip model object from arguments
	 * 
	 * @param name  Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 */
	public Trip(String name, long tripID, int numOfParticipants, String location, String travelTime,
				String travelDate, ArrayList<Person> persons, String meetSpot, Double latitude, Double longitude) {

		Log.d("Trip name: ", name);
		this.name = name;
		this.tripID = tripID;
		this.numOfParticipants = numOfParticipants;
		this.location = location;
		this.travelTime = travelTime;
		this.travelDate = travelDate;
		this.persons = persons;
		this.meetSpot = meetSpot;
		this.latitude = latitude;
		this.longitude = longitude;
		// TODO - fill in here, please note you must have more arguments here
	}

	/**
	 * Serialize Trip object by using writeToParcel. 
	 * This function is automatically called by the
	 * system when the object is serialized.
	 * 
	 * @param dest Parcel object that gets written on 
	 * serialization. Use functions to write out the
	 * object stored via your member variables. 
	 * 
	 * @param flags Additional flags about how the object 
	 * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
	 * In our case, you should be just passing 0.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(name);
		dest.writeLong(tripID);
		dest.writeInt(numOfParticipants);
		dest.writeString(location);
		dest.writeString(travelTime);
		dest.writeString(travelDate);
		dest.writeTypedList(persons);
		dest.writeString(meetSpot);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		// TODO - fill in here 
	}
	
	/**
	 * Feel free to add additional functions as necessary below.
	 */
	
	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}
}
