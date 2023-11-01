package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitudeTextView, longitudeTextView, accuracyTextView, altitudeTextView, addressTextView;
    Geocoder geocoder;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                String address = "";

                geocoder = new Geocoder(getApplicationContext(), Locale.GERMAN);
                try {
                    List<Address> listAdresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (listAdresses != null && listAdresses.size() > 0) {

                        if (listAdresses.get(0).getThoroughfare() != null) {
                            address += listAdresses.get(0).getThoroughfare() + " ";
                        }

                        if (listAdresses.get(0).getSubThoroughfare() != null) {
                            address += listAdresses.get(0).getSubThoroughfare() + ", ";
                        }

                        if (listAdresses.get(0).getPostalCode() != null) {
                            address += listAdresses.get(0).getPostalCode() + " ";
                        }

                        if (listAdresses.get(0).getLocality() != null) {
                            address += listAdresses.get(0).getLocality() + " ";
                        }

                        Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshDisplayValues(location.getLatitude(), location.getLongitude(), location.getAccuracy(), location.getAltitude(), address);
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
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }
    }

    public void refreshDisplayValues(double latitude, double longitude, double accuray, double altitude, String address) {
        latitudeTextView.setText("Latitude " + Double.toString(latitude));
        longitudeTextView.setText("Longitude " + Double.toString(longitude));
        accuracyTextView.setText("Accuracy " + Double.toString(accuray));
        altitudeTextView.setText("Altitude " + Double.toString(altitude));
        addressTextView.setText(address);
    }

}
