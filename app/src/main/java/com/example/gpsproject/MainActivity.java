package com.example.gpsproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView latitudetext;
    TextView longitudetext;
    TextView startlatitudetext;
    TextView startlongitudetext;
    TextView addressText;
    Geocoder geocoder;
    List<Address> addressList;
    TextView distanceText;
    Location LocationOne;
    double distance;
    int initial;
    String finalDistance;
    Button button;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitudetext=findViewById(R.id.longitude);
        latitudetext=findViewById(R.id.latitude);
        addressText=findViewById(R.id.addressText);
        distanceText=findViewById(R.id.distanceText);
        button=findViewById(R.id.button);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onLocationChanged(Location location) {
                geocoder = new Geocoder(MainActivity.this,Locale.US);
                if(initial == 0) {
                    LocationOne= location;
                    initial+=1;
                }
                distance +=LocationOne.distanceTo(location);
                finalDistance = new Double(distance).toString();
                distanceText.setText("Total Distance: "+finalDistance+ " m");


                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                longitudetext.setText("Longitude: " + longitude);
                latitudetext.setText("Latitude: " + latitude);

                try {
                    addressList = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addressList != null && addressList.size() >= 1) {
                        String address = addressList.get(0).getAddressLine(0);
                        String findcity = addressList.get(0).getLocality();
                        String findState = addressList.get(0).getAdminArea();
                        String findTheCountry = addressList.get(0).getCountryName();

                        addressText.setText("Address: " +address );

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        distanceText.setText("Distance: 0.0");
                        longitudetext.setText("Longitude: 0.0");
                        latitudetext.setText("Latitude: 0.0");

                    }
                });

        }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        }



        else

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            locationManager.removeUpdates(locationListener);
        }


    }
}

