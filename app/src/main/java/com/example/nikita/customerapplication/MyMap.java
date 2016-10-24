package com.example.nikita.customerapplication;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nikita on 23-09-2016.
 */
public class MyMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker, mCurrLocationMarker1;
    LocationRequest mLocationRequest;

    RelativeLayout rl;
    TextView time,distance,status;
    String vehicle;
    private ProgressDialog loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();



        setContentView(R.layout.tracking_route);
        rl = (RelativeLayout)findViewById(R.id.belowlayout);

        time = (TextView)findViewById(R.id.mytime);
        distance = (TextView)rl.findViewById(R.id.mydistance);
        status = (TextView)findViewById(R.id.mystatus);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        // Initializing
        MarkerPoints = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       Bundle bundle = getIntent().getExtras();
        vehicle = bundle.getString("vehicle");
        Toast.makeText(MyMap.this,vehicle,Toast.LENGTH_LONG).show();
            getData();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        if (MarkerPoints.size() > 1) {
            MarkerPoints.clear();
            mMap.clear();
        }

    }

    private void getData(){
        Toast.makeText(MyMap.this,"new distance",Toast.LENGTH_LONG).show();
        String myvehicle = vehicle;
        loading = ProgressDialog.show(MyMap.this, "Please wait...", "Fetching...", false, false);
        final String url = Config.FETCH_URL+myvehicle;
        final Handler mHandler = new Handler();
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // Write your code here to update the UI.
                                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        loading.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray vehicles = jsonObject
                                                    .getJSONArray(Config.JSON_ARRAY); // JSON Array
                                            JSONObject v = vehicles.getJSONObject(0);
                                            //String distance = v.getString("distance");
                                            String time1 = v.getString("time");
                                            String distance1 = v.getString("distance");
                                            String str_latitude1 = v.getString("str_latitude");
                                            String str_longitude1 = v.getString("str_longitude");
                                            String end_latitude1 = v.getString("end_latitude");
                                            String end_longitude1 = v.getString("end_longitude");
                                            String status1 = v.getString("status");
                                            if (mCurrLocationMarker != null) {
                                                mCurrLocationMarker.remove();
                                            }
                                            if (mCurrLocationMarker1 != null) {
                                                mCurrLocationMarker1.remove();
                                            }
                                            Double str_latitude = Double.parseDouble(str_latitude1);
                                            Double str_longitude = Double.parseDouble(str_longitude1);
                                            Double end_latitude = Double.parseDouble(end_latitude1);
                                            Double end_longitude = Double.parseDouble(end_longitude1);
                                            LatLng str_latlng = new LatLng(str_latitude,str_longitude);
                                            MarkerPoints.add(str_latlng);
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(str_latlng);
                                            markerOptions.title("Start Position");


                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                            //animateMarker(mCurrLocationMarker,latLng,false);
                                            mCurrLocationMarker = mMap.addMarker(markerOptions);

                                            //move map camera
                                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(str_latlng));
                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                            LatLng end_latlng = new LatLng(end_latitude,end_longitude);
                                            MarkerPoints.add(end_latlng);
                                            MarkerOptions markerOptions1 = new MarkerOptions();
                                            markerOptions1.position(end_latlng);
                                            markerOptions1.title("Running Position");


                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                            //animateMarker(mCurrLocationMarker,latLng,false);
                                            mCurrLocationMarker1 = mMap.addMarker(markerOptions);

                                            //move map camera
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(end_latlng));
                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                            time.setText(time1);
                                            distance.setText(distance1);
                                            status.setText(status1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(MyMap.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                RequestQueue requestQueue = Volley.newRequestQueue(MyMap.this);
                                requestQueue.add(stringRequest);

                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        };
        Thread th = new Thread(myRunnable);
        th.start();

      /*  StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray vehicles = jsonObject
                            .getJSONArray(Config.JSON_ARRAY); // JSON Array
                    JSONObject v = vehicles.getJSONObject(0);
                    //String distance = v.getString("distance");
                    String time1 = v.getString("time");
                    String distance1 = v.getString("distance");
                    //String str_latitude = v.getString("str_latitude");
                    //String str_longitude = v.getString("str_longitude");
                    //String end_latitude = v.getString("end_latitude");
                    //String end_longitude = v.getString("end_longitude");
                    time.setText(time1);
                    distance.setText(distance1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyMap.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MyMap.this);
        requestQueue.add(stringRequest);*/


        }



    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }



    @Override
    public void onConnected(Bundle bundle) {
       /* mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        //startLocationUpdates();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10*1000);
        mLocationRequest.setFastestInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // mLastLocation = location;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }











    // Fetches data from url passed





    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
}