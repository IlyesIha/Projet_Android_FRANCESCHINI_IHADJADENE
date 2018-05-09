package com.example.joffrey.weatherapp;

import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

/**
 * Created by joffrey on 04/05/2018.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private JSONArray weatherArray;
    private com.example.joffrey.weatherapp.Location myLocation = new com.example.joffrey.weatherapp.Location();

    public WeatherAdapter(JSONArray array){
        this.weatherArray = array;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_weather_element, parent, false);
        WeatherHolder holder = new WeatherHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        try{
            String text = weatherArray.getJSONObject(position).getJSONArray("weather").getJSONObject(0).getString("description");
            String img = weatherArray.getJSONObject(position).getJSONArray("weather").getJSONObject(0).getString("icon");
            String date = weatherArray.getJSONObject(position).getString("dt_txt");
            String temps = weatherArray.getJSONObject(position).getJSONObject("main").getString("temp").toString() + " °C";
            String cityname = myLocation.getCity();
            holder.changeText(text,date,temps,img,cityname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherArray.length();
    }

    public void setNewWeather(JSONArray array){
        this.weatherArray = array;
        notifyDataSetChanged();
    }


    class WeatherHolder extends RecyclerView.ViewHolder{
        private TextView tv_temps_txt, tv_temps_units, tv_date, tv_city;
        private ImageView image;

        public WeatherHolder(View itemView) {
            super(itemView);
            this.tv_temps_txt = itemView.findViewById(R.id.rv_weather_element_temps_txt);
            this.tv_date = itemView.findViewById(R.id.rv_weather_element_date);
            this.tv_temps_units = itemView.findViewById(R.id.rv_weather_element_temps_units);
            this.image = itemView.findViewById(R.id.rv_weather_image);
            this.tv_city = itemView.findViewById(R.id.rv_weather_element_city);

        }

        private void changeText(String text, String date,String temp, String img, String city){
            tv_temps_txt.setText(text);
            tv_date.setText(date);
            tv_temps_units.setText(temp);
            tv_city.setText(city);
            setImage(img);
        }

        private void setImage(String img){
            switch (img){
                case "01d": image.setImageResource(R.drawable.clear_day); break;
                case "02d": image.setImageResource(R.drawable.few_clouds_day);break;
                case "03d": image.setImageResource(R.drawable.normal_clouds);break;
                case "04d": image.setImageResource(R.drawable.broken_cloud);break;
                case "09d": image.setImageResource(R.drawable.hard_rain);break;
                case "10d": image.setImageResource(R.drawable.rain_day);break;
                case "11d": image.setImageResource(R.drawable.thunderstorm);break;
                case "13d": image.setImageResource(R.drawable.snow);break;
                case "50d": image.setImageResource(R.drawable.haze);break;
                case "01n": image.setImageResource(R.drawable.clear_night);break;
                case "02n": image.setImageResource(R.drawable.few_clouds_night);break;
                case "03n": image.setImageResource(R.drawable.normal_clouds);break;
                case "04n": image.setImageResource(R.drawable.broken_cloud);break;
                case "09n": image.setImageResource(R.drawable.hard_rain);break;
                case "10n": image.setImageResource(R.drawable.rain_day);break;
                case "11n": image.setImageResource(R.drawable.thunderstorm);break;
                case "13n": image.setImageResource(R.drawable.snow);break;
                case "50n": image.setImageResource(R.drawable.haze);break;
            }
        }
    }



}
