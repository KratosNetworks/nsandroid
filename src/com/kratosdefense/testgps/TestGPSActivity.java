package com.kratosdefense.testgps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.location.*;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TestGPSActivity extends Activity {
	
	private Button btnStartGPS;
	private Button btnStopGPS;
	
	private LocationManager lm;
	private LocationListener ll;
	
	private void callSubmitWebService(double latitude, double longitude, double altitude) {
		
		EditText txtURL = (EditText)findViewById(R.id.txtURL);
		EditText txtDisplayName = (EditText)findViewById(R.id.txtDisplayName);
		
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(txtURL.getText().toString());

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("displayName", txtDisplayName.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
	        nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
	        nameValuePairs.add(new BasicNameValuePair("altitude", String.valueOf(altitude)));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	        
	        Log.d("TESTGPS", "response:" + responseBody);
	    } catch (ClientProtocolException e) {
	    	Log.d("TESTGPS", "A ClientProtocolException exception has occured: " + e.toString());
	    	Log.d("TESTGPS", "response:");
	    } catch (IOException e) {
	    	Log.d("TESTGPS", "An IOException has occured: " + e.toString());
	    }
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("TESTGPS", "Starting application...");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d("TESTGPS", "Assigning event handlers");
        this.btnStartGPS = (Button)findViewById(R.id.btnStartGPS);
        this.btnStopGPS = (Button)findViewById(R.id.btnStopGPS);
        btnStartGPS.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Log.d("TESTGPS", "btnStartGPS clicked");
        		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        		
        		// Get The current location
        		Criteria crit = new Criteria();
        		crit.setAccuracy(Criteria.ACCURACY_FINE);
        		String provider = lm.getBestProvider(crit, true);        		
        		Location lastKnownLocation = lm.getLastKnownLocation(provider);
        		
        		callSubmitWebService(
        				lastKnownLocation.getLatitude(), 
        				lastKnownLocation.getLongitude(), 
        				lastKnownLocation.getAltitude());
        		
        		Log.d("TESTGPS", "" + lastKnownLocation.getLatitude() + "");
            	Log.d("TESTGPS", "" + lastKnownLocation.getLongitude() + "");
            	Log.d("TESTGPS", "" + lastKnownLocation.getAltitude() + "");
        		
        		// Get future location updates
                ll = new LocationListener() {
                	public void onLocationChanged(Location location) {
                    	Log.d("TESTGPS", "Received onLocationChanged event");
                    	
                        if (location != null) {
                        	callSubmitWebService(
                        			location.getLatitude(), 
                        			location.getLongitude(), 
                        			location.getAltitude());
                        	
                        	Log.d("TESTGPS", "lat: " + location.getLatitude() + "");
                        	Log.d("TESTGPS", "long: " + location.getLongitude() + "");
                        	Log.d("TESTGPS", "alt: " + location.getAltitude() + "");
                        } else {
                        	Log.d("TESTGPS", "location received was empty");
                        }
                    }
                    
                    public void onProviderDisabled(String provider) {
                    	Log.d("TESTGPS", "Location provider disabled");
                    }
                    
                    public void onProviderEnabled(String provider) {
                    	Log.d("TESTGPS", "Location provider enabled");
                    }
                    
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    	Log.d("TESTGPS", "Location status changed");
                    }
                };
        		
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        	}
        });
        
        btnStopGPS.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Log.d("TESTGPS", "btnStopGPS clicked. Exiting application.");
        		//lm.removeUpdates(ll);
        		finish();
        	}
        });
    }
}

