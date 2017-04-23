package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Person implements Parcelable {
	
	// Member fields should exist here, what else do you need for a person?
	// Please add additional fields
	private String name;
	private int personID;
	private String phoneNum;
	//private String currentLocation;
	private String latitude;
	private String longitude;
	private String destination;
	private String eta;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Parcelable creator. Do not modify this function.
	 */
	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		public Person createFromParcel(Parcel p) {
			return new Person(p);
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	public Person() {
	}

	/**
	 * Create a Person model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Person(Parcel p) {

		Log.d("Parcel object: ", p.toString());
		this.name = p.readString();
		this.personID = p.readInt();
		this.phoneNum = p.readString();
		this.latitude = p.readString();
		this.longitude = p.readString();
		this.destination = p.readString();
		this.eta = p.readString();

		// TODO - fill in here
		
	}
	
	/**
	 * Create a Person model object from arguments
	 * 
	 * @param name Add arbitrary number of arguments to
	 * instantiate Person class based on member variables.
	 */
	public Person(String name, int personID, String phoneNum, String latitude, String longitude, String destination, String eta) {

		Log.d("Person object: ", name);
		this.name = name;
		this.personID = personID;
		this.phoneNum = phoneNum;
		this.latitude = latitude;
		this.longitude = longitude;
		this.destination = destination;
		this.eta = eta;
		// TODO - fill in here, please note you must have more arguments here
	}

	/**
	 * Serialize Person object by using writeToParcel.  
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
		dest.writeInt(personID);
		dest.writeString(phoneNum);
		dest.writeString(latitude);
		dest.writeString(longitude);
		dest.writeString(destination);
		dest.writeString(eta);

		// TODO - fill in here 	
	}

	public Person(String name)
	{

		// TODO - fill in here, please note you must have more arguments here
		this.name = name;
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
