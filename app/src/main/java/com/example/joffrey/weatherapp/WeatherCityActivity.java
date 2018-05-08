package com.example.joffrey.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class WeatherCityActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    private final String TAG = "WeatherCurrentActivity";
    private RecyclerView recyclerView;
    private com.example.joffrey.weatherapp.Location myLocation = new com.example.joffrey.weatherapp.Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_city);

        recyclerView = findViewById(R.id.rv_current);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        WeatherAdapter wa = new WeatherAdapter(getCurrentWeatherFromFile());

        recyclerView.setAdapter(wa);

        GetWeatherCurrentServices.startActionWeather(this);
        IntentFilter intentFilter = new IntentFilter(WEATHER_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new WeatherCityActivity.WeatherUpdate(), intentFilter);


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
                mFusedLocationClient.getLastLocation().addOnSuccessListener(WeatherCityActivity.this, new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if(location != null){
                            myLocation.setLatitude(location.getLatitude());
                            myLocation.setLongitude(location.getLongitude());
                            Toast.makeText(getApplicationContext(), R.string.toast_ok + myLocation.getLatitude() + " " +  myLocation.getLongitude(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(WeatherCityActivity.this, WeatherCurrentActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.toast_error,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } break;
            case R.id.item_search : AlertDialog.Builder builder = new AlertDialog.Builder(WeatherCityActivity.this);
                builder.setMessage(R.string.alert_message);
                builder.setTitle(R.string.alert_title);
                final EditText input = new EditText(WeatherCityActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton(R.string.alert_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //city = input.getText().toString();
                        myLocation.setCity(input.getText().toString());
                        Toast.makeText(getApplicationContext(), myLocation.getCity(), Toast.LENGTH_LONG).show();
                        Intent j = new Intent(WeatherCityActivity.this, WeatherCityActivity.class);
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


    public static final String WEATHER_UPDATE = "com.example.joffrey.WeatherApp.WEATHER_CITY_UPDATE";

    public class WeatherUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Intent : " + getIntent().getAction());
            JSONArray array = getCurrentWeatherFromFile();
            WeatherAdapter wa = (WeatherAdapter) recyclerView.getAdapter();
            wa.setNewWeather(array);
            Log.d("cont ", Integer.toString(array.length()));
        }
    }

    public JSONArray getCurrentWeatherFromFile(){
        try {

            InputStream is = new FileInputStream(getCacheDir() + "/" + "city_weather.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer,"UTF-8")).getJSONArray("list");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
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
