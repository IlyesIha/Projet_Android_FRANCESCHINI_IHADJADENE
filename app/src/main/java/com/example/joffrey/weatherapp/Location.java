package com.example.joffrey.weatherapp;

/**
 * Created by joffrey on 04/05/2018.
 */

public class Location {

    private static String  latitude, longitude, city;

    public static String getCity(){
        return city;
    }

    public static String getLatitude(){
        return latitude;
    }

    public static String getLongitude(){
        return longitude;
    }

    public static void setLatitude(double lat){
        latitude = Double.toString(lat);
    }

    public static void setLongitude(double lon){
        longitude = Double.toString(lon);
    }

    public static void setCity (String name) {
        city = name;
    }
}
