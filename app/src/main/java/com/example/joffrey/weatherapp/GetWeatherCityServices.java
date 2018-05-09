package com.example.joffrey.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetWeatherCityServices extends IntentService {

    private static final String get_city_weather = "com.example.joffrey.weatherapp.action.GetWeatherCityServices";
    private static final String TAG = "GetWeatherCurrentServices";

    public GetWeatherCityServices() {
        super("GetWeatherCityServices");
    }


    // TODO: Customize helper method
    public static void startActionWeather(Context context) {
        Intent intent = new Intent(context, GetWeatherCityServices.class);
        intent.setAction(get_city_weather);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(get_city_weather.equals(action));
            handleActionWeather();
        }
    }

    private void handleActionWeather() {
        Log.i(TAG, "Thread service name : " + Thread.currentThread().getName());
        URL url = null;

        try {
            url = new URL("http://api.openweathermap.org/data/2.5/forecast?lang=fr&units=metric&APPID=b5fe018318b91102e114c2e5db8138d8&lat=" + Location.getCity());
            Log.i(TAG, "URL : " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "weather.json"));
                Log.d(TAG, "Current Weather JSON downloaded !");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(WeatherCityActivity.WEATHER_UPDATE));
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
