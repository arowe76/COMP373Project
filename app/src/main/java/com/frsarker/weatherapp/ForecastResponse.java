package com.frsarker.weatherapp;
import com.google.gson.annotations.SerializedName;
import java.util.List;


/**
 * Model for OpenWeather 5-Day / 3-Hour forecast API (/forecast)
 */
public class ForecastResponse {

    // The main list of forecast entries (3-hour steps)
    @SerializedName("list")
    public List<ForecastItem> list;

    public static class ForecastItem {

        // Timestamp in seconds since epoch...
        @SerializedName("dt")
        public long dt;

        // Main temp info (min/max)
        @SerializedName("main")
        public WeatherResponse.Main main;

        // Weather description(s) (e.g., "broken clouds")
        @SerializedName("weather")
        public List<WeatherResponse.Weather> weather;
    }

    public static class Main {

        @SerializedName("temp_min")
        public double tempMin;
        @SerializedName("temp_max")
        public double tempMax;
    }

    public static class Weather {

        @SerializedName("description")
        public String description;
    }
}
