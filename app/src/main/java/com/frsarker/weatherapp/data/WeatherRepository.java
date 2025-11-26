package com.frsarker.weatherapp.data;

import android.os.Bundle;
import com.frsarker.weatherapp.BuildConfig;
import com.frsarker.weatherapp.WeatherApiService;
import com.frsarker.weatherapp.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * WeatherRepository Class
 *
 * The WeatherRepository class is part of the "data layer" in the application's architecture.
 *
 * What is the purpose? Why change the architecture from COMP 312 WeatherApp?
 *    Without a Repository, MainActivity must:
 *       - build Retrofit
 *       - construct HTTP calls
 *       - decide what to show on failure
 *
 *    This becomes messy.  The Repository pattern fixes that.
 *
 ****
 */

public class WeatherRepository {

    private final WeatherApiService apiService;
    private final WeatherCache cache;

    public WeatherRepository(WeatherApiService apiService, WeatherCache cache) {
        this.apiService = apiService;
        this.cache = cache;
    }

    /**
     * Callback interface so the repository can tell the UI:
     *    - Success (fresh or cached)
     *    - Error (no network + no cache)
     */
    public interface WeatherCallback {
        void onSuccess(WeatherResponse response, boolean fromCache, long lastUpdatedMillis);
        void onError(String message);
    }




    public void getCurrentWeatherWithCache(final String cityName, final WeatherCallback callback) {
        Call<WeatherResponse> call = apiService.getCurrentWeather(
                cityName,
                BuildConfig.WEATHER_API_KEY,
                "metric"
        );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse body = response.body();
                    long now = System.currentTimeMillis();

                    // Save to cache...
                    cache.saveWeather(cityName, body, now);
                    // Notify UI (fresh data)...
                    callback.onSuccess(body, false, now);

                } else {

                    WeatherCache.CachedWeather cached = cache.loadWeather(cityName);
                    if (cached != null) {
                        callback.onSuccess(cached.getResponse(), true, cached.getTimestampMillis());

                    } else {

                        callback.onError("City not found and no cached data.");
                    }
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                WeatherCache.CachedWeather cached = cache.loadWeather(cityName);
                if (cached != null) {
                    callback.onSuccess(cached.getResponse(), true, cached.getTimestampMillis());

                } else {

                    callback.onError("Network error and no cached data available.");
                }
            }
        });
    }
}
