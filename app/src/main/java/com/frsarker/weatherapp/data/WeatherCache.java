package com.frsarker.weatherapp.data;


import android.content.Context;
import android.content.SharedPreferences;

public class WeatherCache {

    private static final String PREF_NAME = "weather_cache_prefs";
    private static final String KEY_PREFIX_DATA = "weather_data_";
    private static final String KEY_SUFFIX_TIME = "_time";

    private final SharedPreferences prefs;



    public WeatherCache(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }



}
