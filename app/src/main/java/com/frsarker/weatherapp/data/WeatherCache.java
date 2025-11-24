package com.frsarker.weatherapp.data;


import android.content.Context;
import android.content.SharedPreferences;
import com.frsarker.weatherapp.WeatherResponse;


/**
 * WeatherCache
 *
 * Simple cache that stores the last known weather for a city using SharedPreferences.
 *    - Saves WeatherResponse as JSON.
 *    - Also stores a timestamp of when it was saved.
 *    - Allows loading cached weather if network is unavailable.
 */

public class WeatherCache {

    private static final String PREF_NAME = "weather_cache_prefs";
    private static final String KEY_PREFIX_DATA = "weather_data_";
    private static final String KEY_SUFFIX_TIME = "_time";

    private final SharedPreferences prefs;



    public WeatherCache(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static class CachedWeather {
        private final WeatherResponse response;
        private final long timestampMillis;

        public CachedWeather(WeatherResponse response, long timestampMillis) {
            this.response = response;
            this.timestampMillis = timestampMillis;
        }

        public WeatherResponse getResponse() {
            return response;
        }

        public long getTimestampMillis() {
            return timestampMillis;
        }
    }


}
