package com.example.joffrey.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by joffrey on 04/05/2018.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private JSONArray weatherArray;

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
            String text = weatherArray.getJSONObject(position).getJSONObject("weather").getString("description");
            holder.changeText(text);
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
        private TextView tv;

        public WeatherHolder(View itemView) {
            super(itemView);
            this.tv = itemView.findViewById(R.id.rv_weather_element);
        }

        private void changeText(String text){
            tv.setText(text);
        }
    }



}
