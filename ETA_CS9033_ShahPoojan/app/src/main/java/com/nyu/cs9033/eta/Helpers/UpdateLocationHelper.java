package com.nyu.cs9033.eta.Helpers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateLocationHelper extends Service implements LocationListener {
    private final Context mContext;
    private static final long DISTANCE_FOR_UPDATE= 0;
    private static final long TIME_FOR_UPDATE = 1000* 5 * 1;
    Location location;
    double latitude;
    double longitude;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    protected LocationManager locationManager;

    double updateLatitude;
    double updaLongitude;

    public final String TAG = "UpdateLocationHelper";

    public UpdateLocationHelper(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!checkGPS && ! checkNetwork){
                Log.d(TAG, "No network access.");
            }
            else{
                this.canGetLocation = true;
                if(checkNetwork){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,TIME_FOR_UPDATE,DISTANCE_FOR_UPDATE,this);
                    if(locationManager!=null){
                        location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location!=null){
                            latitude = location.getLatitude();
                            longitude= location.getLongitude();
                        }
                    }
                }
                else if(checkGPS){
                    if(location!=null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,TIME_FOR_UPDATE,DISTANCE_FOR_UPDATE,this);
                        if(locationManager!=null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location != null) {
            updateLatitude = location.getLatitude();
            updaLongitude = location.getLongitude();


            new DownloadTripHelper1(this).execute("http://cs9033-homework.appspot.com/");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(UpdateLocationHelper.this);
        }
    }

    private class DownloadTripHelper1 extends AsyncTask<String, Void, String> {

        public UpdateLocationHelper activity;

        public DownloadTripHelper1(UpdateLocationHelper a){
            this.activity = a;
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

            Log.d(TAG, "The result in main is : " + result);


        }

        public JSONObject toJSON() throws JSONException {

            JSONObject json = new JSONObject();

            json.put("command","UPDATE_LOCATION");
            json.put("latitude", updateLatitude);
            json.put("longitude", updaLongitude);
            json.put("datetime", System.currentTimeMillis());
            return json;
        }

        public String downloadUrl(String inurl) throws IOException {
            URL url = new URL(inurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                OutputStream outputStream = connection.getOutputStream();

                Log.d(TAG, "Im in upload");
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

                //return new String(sss);*/
            } catch (JSONException e) {
                e.printStackTrace();
                return "Caught json exception";
            } finally {
                connection.disconnect();
            }

        }

    }

}
