package com.frsarker.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.frsarker.weatherapp.WeatherResponse;
import com.google.gson.Gson;

import java.util.Locale;

/**
 * WeatherCache
 * ------------
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
    private final Gson gson = new Gson();

    public WeatherCache(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * CachedWeather - small wrapper around WeatherResponse + timestamp.
     */
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

    private String buildDataKey(String cityName) {
        return KEY_PREFIX_DATA + cityName.toLowerCase(Locale.US);
    }

    private String buildTimeKey(String cityName) {
        return buildDataKey(cityName) + KEY_SUFFIX_TIME;
    }


    /**
     * Save weather data and timestamp for a city.
     */
    public void saveWeather(String cityName, WeatherResponse response, long timestampMillis) {
        String json = gson.toJson(response);
        prefs.edit()
                .putString(buildDataKey(cityName), json)
                .putLong(buildTimeKey(cityName), timestampMillis)
                .apply();
    }

    /**
     * Load cached weather for a city. Returns null if nothing is cached.
     */
    public CachedWeather loadWeather(String cityName) {
        String json = prefs.getString(buildDataKey(cityName), null);
        if (json == null) {
            return null;
        }
        long ts = prefs.getLong(buildTimeKey(cityName), 0L);
        WeatherResponse response = gson.fromJson(json, WeatherResponse.class);
        return new CachedWeather(response, ts);
    }
}
