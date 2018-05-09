package com.example.joffrey.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Menu extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private com.example.joffrey.weatherapp.Location myLocation = new com.example.joffrey.weatherapp.Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //TextView tv = findViewById(R.id.tv_autor);
        Button btn_current  = findViewById(R.id.button_current);
        Button btn_city = findViewById(R.id.button_select_city);

        btn_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLocationPermission()){
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(Menu.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                myLocation.setLatitude(location.getLatitude());
                                myLocation.setLongitude(location.getLongitude());
                                Toast.makeText(getApplicationContext(), R.string.toast_ok + myLocation.getLatitude() + " " +  myLocation.getLongitude(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Menu.this, WeatherCurrentActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.toast_error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        btn_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                builder.setMessage(R.string.alert_message);
                builder.setTitle(R.string.alert_title);
                final EditText input = new EditText(Menu.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton(R.string.alert_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //city = input.getText().toString();
                        myLocation.setCity(input.getText().toString());
                        Toast.makeText(getApplicationContext(), myLocation.getCity(), Toast.LENGTH_LONG).show();
                        Intent j = new Intent(Menu.this, WeatherCityActivity.class);
                        startActivity(j);
                    }
                });

                builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), R.string.alert_cancel, Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_current : if(checkLocationPermission()){
                mFusedLocationClient.getLastLocation().addOnSuccessListener(Menu.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            myLocation.setLatitude(location.getLatitude());
                            myLocation.setLongitude(location.getLongitude());
                            Toast.makeText(getApplicationContext(), R.string.toast_ok + myLocation.getLatitude() + " " +  myLocation.getLongitude(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Menu.this, WeatherCurrentActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.toast_error,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } break;
            case R.id.item_search : AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                builder.setMessage(R.string.alert_message);
                builder.setTitle(R.string.alert_title);
                final EditText input = new EditText(Menu.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton(R.string.alert_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //city = input.getText().toString();
                        myLocation.setCity(input.getText().toString());
                        Toast.makeText(getApplicationContext(), myLocation.getCity(), Toast.LENGTH_LONG).show();
                        Intent j = new Intent(Menu.this, WeatherCityActivity.class);
                        startActivity(j);
                    }
                });

                builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), R.string.alert_cancel, Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
