package com.frsarker.weatherapp.data;

import android.os.Bundle;
import com.frsarker.weatherapp.BuildConfig;
import com.frsarker.weatherapp.WeatherApiService;
import com.frsarker.weatherapp.WeatherResponse;

import retrofit2.Call;


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
 */

public class WeatherRepository {

    private final WeatherApiService apiService;

    public WeatherRepository(WeatherApiService apiService) {
        this.apiService = apiService;
    }

    public Call<WeatherResponse> getCurrentWeather(String cityName) {
        return apiService.getCurrentWeather(
                cityName;
                BuildConfig.WEATHER_API_KEY,
                "metric"
        );
    }
}
