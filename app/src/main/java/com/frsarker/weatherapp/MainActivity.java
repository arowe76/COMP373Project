package com.frsarker.weatherapp;

import com.frsarker.weatherapp.ForecastResponse;
import com.frsarker.weatherapp.WeatherApiService;
import com.frsarker.weatherapp.ApiClient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.frsarker.weatherapp.data.WeatherCache;
import com.frsarker.weatherapp.data.WeatherRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * MainActivity is the main screen of the Weather App
 *
 * It allows users to:
 *    - Search for a city
 *    - Fetch real-time weather data from OpenWeatherMap API
 *    - Display detailed weather information (temperature, humidity, etc.)
 *    - Dynamically change the background based on current weather conditions
 */
public class MainActivity extends AppCompatActivity {
    // API endpoint base URL for weather data
    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    // API key, stored securely in BuildConfig
    private final String API_KEY = BuildConfig.WEATHER_API_KEY;

    // Repository + Cache
    private WeatherRepository weatherRepository;
    private WeatherCache weatherCache;


    // UI elements for user input
    private EditText searchCityEditText;
    private Button searchButton;

    // UI elements for displaying weather data
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;
    ImageView currWeatherIcon;
    // Offline banner at top/bottom of the screen
    private TextView offlineBannerTxt;

    // 5-Day Weather Forecast
    private TextView forecastDay1;
    private TextView forecastDay2;
    private TextView forecastDay3;
    private TextView forecastDay4;
    private TextView forecastDay5;

    // Right-side Min/Max for each day
    private TextView forecastMin1, forecastMin2, forecastMin3,  forecastMin4, forecastMin5;
    private TextView forecastMax1, forecastMax2, forecastMax3,  forecastMax4, forecastMax5;

    // Middle icons for each day
    private ImageView forecastIcon1, forecastIcon2, forecastIcon3, forecastIcon4, forecastIcon5;



    /**
     * Initializes the app when activity is created.
     * Sets up UI components, binds views and fetches default city weather ("Chicago").
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("DEBUG", "onCreate: Started");      // Shows in Logcat
        setContentView(R.layout.activity_main);             // This loads UI...

        // 5-Day Forecast
        forecastDay1 = findViewById(R.id.textForecastDay1);
        forecastDay2 = findViewById(R.id.textForecastDay2);
        forecastDay3 = findViewById(R.id.textForecastDay3);
        forecastDay4 = findViewById(R.id.textForecastDay4);
        forecastDay5 = findViewById(R.id.textForecastDay5);

        // 5-Day Forecast: right side Min/Max
        forecastMin1 = findViewById(R.id.textForecastMin1);
        forecastMin2 = findViewById(R.id.textForecastMin2);
        forecastMin3 = findViewById(R.id.textForecastMin3);
        forecastMin4 = findViewById(R.id.textForecastMin4);
        forecastMin5 = findViewById(R.id.textForecastMin5);

        forecastMax1 = findViewById(R.id.textForecastMax1);
        forecastMax2 = findViewById(R.id.textForecastMax2);
        forecastMax3 = findViewById(R.id.textForecastMax3);
        forecastMax4 = findViewById(R.id.textForecastMax4);
        forecastMax5 = findViewById(R.id.textForecastMax5);

        // 5-Day Forecast: middle icons
        forecastIcon1 = findViewById(R.id.iconForecast1);
        forecastIcon2 = findViewById(R.id.iconForecast2);
        forecastIcon3 = findViewById(R.id.iconForecast3);
        forecastIcon4 = findViewById(R.id.iconForecast4);
        forecastIcon5 = findViewById(R.id.iconForecast5);

        Log.d("DEBUG", "setContentView: End");    // Shows in Logcat


        // Setup Retrofit + Repository + Cache...
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
        weatherCache = new WeatherCache(getApplicationContext());
        weatherRepository = new WeatherRepository(apiService, weatherCache, this);

        // Initialize your EditText and Button views...
        searchCityEditText = findViewById(R.id.searchCity);
        searchButton = findViewById(R.id.searchButton);
        Log.d("DEBUG", "searchButton: End");      // Shows in Logcat

        // Bind views from the layout...
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        currWeatherIcon = findViewById(R.id.currWeatherIcon);


        offlineBannerTxt = findViewById(R.id.offlineBanner);

        Log.d("DEBUG", "findViewById: End");     // Shows in Logcat

        // Set default city weather when app launches...
        fetchWeatherData("Chicago");

        // Set up search...
        // searchButton = findViewById(R.id.searchButton);

        // Set up search button click listener...
        searchButton.setOnClickListener(v -> {
            Log.d("BUTTON", "CLICKED");      // Shows in Logcat
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();  // Shows in GUI

            String city = searchCityEditText.getText().toString().trim();
            if (!city.isEmpty()) {
                //Debugging Toast - Confirm if the click is being registered & city is being passed...
                Toast.makeText(this, "Search for: " + city, Toast.LENGTH_SHORT).show();   // Shows in GUI
                fetchWeatherData(city);
            }
        });
    }


    /**
     * Makes an asynchronous API call to fetch current weather data for the specified city using WeatherRepository
     * (which handles remote + cache + auto-sync).
     *
     * @param cityName Name of the city to retrieve weather information for...
     */
    private void fetchWeatherData(String cityName) {

        // Logs for Debugging...
        Log.d("API_CHECK", "Using API key: " + API_KEY);    // Shows in Logcat
        Log.d("API_CHECK", "City: " + cityName);            // Shows in Logcat

        weatherRepository.getCurrentWeatherWithCache(cityName, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse weather, boolean fromCache, long lastUpdatedMillis) {
                // Logs for Debugging...
                Log.d("WEATHER_RESPONSE", "City: " + weather.getCityName());          // Shows in Logcat
                Log.d("WEATHER_RESPONSE", "Temp: " + weather.getMain().getTemp());    // Shows in Logcat
                Log.d("WEATHER_RESPONSE", "Min: " + weather.getMain().getTempMin());  // Shows in Logcat
                Log.d("WEATHER_RESPONSE", "Max: " + weather.getMain().getTempMax());  // Shows in Logcat

                // Build address...
                String address = weather.getCityName() + "," + weather.getSys().getCountry();
                // Build updatedAt message...
                String updatedAt;
                if (fromCache) {
                    // Show the time only in the orange banner, so the main "updated_at" label can stay empty while
                    // offline...
                    updatedAt = "";
                } else {
                    // When fresh network data, show a normal "last updated" message...
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String time = sdf.format(new Date(lastUpdatedMillis));
                    updatedAt = "Updated just now " + time;
                }

                // Convert temperatures from Celsius to Fahrenheit...
                float tempCelsius = weather.getMain().getTemp();
                float tempMinCelsius = weather.getMain().getTempMin();
                float tempMaxCelsius = weather.getMain().getTempMax();

                float tempFahrenheit = (tempCelsius * 9 / 5) + 32;
                float tempMinFahrenheit = (tempMinCelsius * 9 / 5) + 32;
                float tempMaxFahrenheit = (tempMaxCelsius * 9 / 5) + 32;

                // Format and extract weather data for UI update...
                String temp = String.format(Locale.getDefault(), "%.1f°F", tempFahrenheit);
                String tempMin = "Min Temperature: " + String.format(Locale.getDefault(), "%.1f°F",
                        tempMinFahrenheit);
                String tempMax = "Max Temperature: " + String.format(Locale.getDefault(), "%.1f°F",
                        tempMaxFahrenheit);
                String wind = String.format(Locale.getDefault(), "%.1f m/s", weather.getWind().getSpeed());
                String pressure = weather.getMain().getPressure() + " hPa";
                String humidity = weather.getMain().getHumidity() + "%";

                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.
                        getSys().getSunrise() * 1000));
                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.
                        getSys().getSunset() * 1000));

                String weatherDescription = weather.getWeather().get(0).getDescription();

                Log.d("WEATHER_RESPONSE", "Calling updateWeatherUI...");        // Shows in Logcat
                updateWeatherUI(address, updatedAt, weatherDescription, temp, tempMin, tempMax, sunrise,
                        sunset, wind, pressure, humidity);

                // Only fetch 5-Day forecast when we have fresh network data
                if (!fromCache) {
                    loadFiveDayForecast(cityName);
                }

                // Offline banner will now show when using cache...
                setOfflineBanner(fromCache, lastUpdatedMillis);

                if (fromCache) {
                    Toast.makeText(MainActivity.this,
                            "Showing cached data (offline or error).",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                Log.e("WEATHER_ERROR", message);
                Log.e("BANNER_ERROR", "Network AND no cache → triggering no-cache banner");

                // Tell setOfflineBanner: "Error/no cache" case...
                setOfflineBanner(false, 0L);
            }
        });
    }

    /**
     * Update the UI elements with fetched and formatted weather data.
     * Also triggers dynamic background color updates based on the weather condition.
     *
     * @param address            address City and country
     * @param updatedAt          Update timestamp
     * @param weatherDescription Weather description (clear, rain, cloudy, etc.)
     * @param temp               Current temperature
     * @param tempMin            Minimum temperature
     * @param tempMax            Maximum temperature
     * @param sunrise            Sunrise time
     * @param sunset             Sunset time
     * @param wind               Wind speed
     * @param pressure           Atmospheric pressure
     * @param humidity           Humidity percentage
     */
    public void updateWeatherUI(String address, String updatedAt, String weatherDescription, String temp, String tempMin,
                                String tempMax, String sunrise, String sunset, String wind, String pressure, String
                                        humidity) {
        // Debug - Make sure it is visible...
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
        // Debug log - Toast...
        Toast.makeText(this, "UI Updated!", Toast.LENGTH_SHORT).show();                  // Shows in GUI
        Log.d("WEATHER_UI", "Updating UI with fetched weather data");                       // Shows in Logcat
        Toast.makeText(this, "UI updated for: " + address, Toast.LENGTH_LONG).show();    // Shows in GUI
        Log.d("WEATHER_UI", "UI updated for: " + address);                                  // Shows in Logcat

        addressTxt.setText(address);
        updated_atTxt.setText(updatedAt);
        statusTxt.setText(weatherDescription.toUpperCase());
        tempTxt.setText(temp);
        temp_minTxt.setText(tempMin);
        temp_maxTxt.setText(tempMax);
        sunriseTxt.setText(sunrise);
        sunsetTxt.setText(sunset);
        windTxt.setText(wind);
        pressureTxt.setText(pressure);
        humidityTxt.setText(humidity);
        currWeatherIcon.setImageResource(getWeatherIcon(weatherDescription));


        // This function will call the setDynamicBackground to update the color (GUI Background) of the current weather...
        setDynamicBackground(weatherDescription);
    }

    /**
     * Shows or hides the offline banner depending on whether data is from cache.
     *
     * @param fromCache         // true if we are using cached weather, false if fresh from API
     * @param lastUpdatedMillis // timestamp (System.currentTimeMillis) when the cached data was saved
     */
    private void setOfflineBanner(boolean fromCache, long lastUpdatedMillis) {

        Log.d("BANNER_DEBUG",
                "setOfflineBanner called: fromCache=" + fromCache + ", lastUpdatedMillis=" + lastUpdatedMillis);
        Log.d("BANNER_DEBUG",
                "offlineBannerTxt is " + (offlineBannerTxt == null ? "NULL" : "NOT NULL"));

        if (offlineBannerTxt == null) {
            return;
        }
            if (fromCache) {
                // CASE #1: Offline, but we DO have cached data
                // Show banner with "cached from <time>"...
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time = sdf.format(new Date(lastUpdatedMillis));
                String bannerText = "Offline mode: showing cached data from " + time;

                offlineBannerTxt.setText(bannerText);
                offlineBannerTxt.setVisibility(View.VISIBLE);

                // Hide the "Last Updated..." label while offline...
                if (updated_atTxt != null) {
                    updated_atTxt.setVisibility(View.GONE);
                }

            } else {

                if (lastUpdatedMillis == 0L) {
                    // CASE 2: Offline AND no cached data
                    // Show a different banner message...
                    String bannerText = "Offline mode: No cached data available";

                    offlineBannerTxt.setText(bannerText);
                    offlineBannerTxt.setVisibility(View.VISIBLE);

                    // Hide the "Last updated..." label
                    if (updated_atTxt != null) {
                        updated_atTxt.setVisibility(View.GONE);

                    }

                } else {
                    // CASE 3: Online, fresh data
                    // Hide banner and show normal "Last updated..." label
                    offlineBannerTxt.setVisibility(View.GONE);

                    if (updated_atTxt != null) {
                        updated_atTxt.setVisibility(View.VISIBLE);
                    }
                }
            }
        }


    /**
     * Dynamically changes the screen background color based on the current weather description.
     *
     * @param weatherDescription Current weather condition (e.g., clear, clouds, rain)
     */
    public void setDynamicBackground(String weatherDescription) {
        RelativeLayout mainContainer = findViewById(R.id.mainContainer);

        // Default text color...
        int textColor = getResources().getColor(android.R.color.black);                                  // Default to black text

        if (weatherDescription.contains("clear")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));  // Light Blue...
            Log.d("WEATHER_UI", "Changing background color to: Light Blue");
            textColor = getResources().getColor(android.R.color.white);

        } else if (weatherDescription.contains("clouds")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));      // Gray for Clouds...
            textColor = getResources().getColor(android.R.color.white);
        } else if (weatherDescription.contains("rain")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));   // Dark Blue for Rain...
            textColor = getResources().getColor(android.R.color.white);
        } else if (weatherDescription.contains("snow")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.white));            // White for Snow...
            textColor = getResources().getColor(android.R.color.black);
        } else if (weatherDescription.contains("thunderstorm")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.black));            // Dark for Storm...
            textColor = getResources().getColor(android.R.color.white);
        } else {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.background_light)); // Default light...
            textColor = getResources().getColor(android.R.color.black);
        }

        // Applies text color to ALL important textViews...
        applyTextColor(textColor);

        // Debug log - Toast...
        Toast.makeText(this, "Changing background color...", Toast.LENGTH_SHORT).show();    // Shows in GUI
        Log.d("WEATHER_UI", "Changing background color...");                                   // Shows in Logcat
    }


        /**
         * Applies a specified text color to all weather-related Textviews.
         *
         * @param Color The color to apply to text elements.
         */
        private void applyTextColor ( int Color) {
            addressTxt.setTextColor(Color);
            updated_atTxt.setTextColor(Color);
            statusTxt.setTextColor(Color);
            tempTxt.setTextColor(Color);
            temp_minTxt.setTextColor(Color);
            temp_maxTxt.setTextColor(Color);
            sunriseTxt.setTextColor(Color);
            sunsetTxt.setTextColor(Color);
            windTxt.setTextColor(Color);
            pressureTxt.setTextColor(Color);
            humidityTxt.setTextColor(Color);
        }
    private int getWeatherIcon(String description) {
        description = description.toLowerCase();

        if (description.contains("blizzard")) {
            return R.drawable.blizzard;
        } else if (description.contains("snow") || description.contains("flurries") || description.contains("blowing_snow")) {
            return R.drawable.flurries;
        } else if (description.contains("clear") && description.contains("night")) {
            return R.drawable.clear_night;
        } else if (description.contains("clear") || description.contains("sunny") || description.contains("mostly_sunny")) {
            return R.drawable.sunny;
        } else if (description.contains("cloud")) {
            return R.drawable.cloudy;
        } else if (description.contains("drizzle")) {
            return R.drawable.drizzle;
        } else if (description.contains("haze") || description.contains("fog") || description.contains("dust") || description.contains("smoke")) {
            return R.drawable.haze_fog_dust_smoke;
        } else if (description.contains("heavy rain") || description.contains("showers")) {
            return R.drawable.heavy_rain;
        } else if (description.contains("rain")) {
            return R.drawable.showers_rain;
        } else {
            return R.drawable.sunny; // default icon
        }
    }

    /**
     * Fetches and populates 5-day forecast.
     *
     * @param cityName
     */
    private void loadFiveDayForecast(String cityName) {
            WeatherApiService api = ApiClient.getClient().create(WeatherApiService.class);

            Call<ForecastResponse> call = api.getFiveDayForecast(
                    cityName,
                    BuildConfig.WEATHER_API_KEY,
                    "imperial"
            );

            call.enqueue(new Callback<ForecastResponse>() {
                @Override
                public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Log.e("Forecast", "Error response: "  + response.code());
                        return;
                    }

                    ForecastResponse forecast = response.body();
                    List<ForecastResponse.ForecastItem> items = forecast.list;
                    if (items == null || items.isEmpty()) {
                        Log.e("Forecast", "No forecast data");
                        return;
                    }

                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEE M/d", Locale.getDefault());

                    setForecastRow(forecastDay1, forecastMin1, forecastMax1, forecastIcon1, items, 0, dayFormat);
                    setForecastRow(forecastDay2, forecastMin2, forecastMax2, forecastIcon2, items, 8, dayFormat);
                    setForecastRow(forecastDay3, forecastMin3, forecastMax3, forecastIcon3, items, 16, dayFormat);
                    setForecastRow(forecastDay4, forecastMin4, forecastMax4, forecastIcon4, items, 24, dayFormat);
                    setForecastRow(forecastDay5, forecastMin5, forecastMax5, forecastIcon5, items, 32, dayFormat);
                }

                @Override
                public void onFailure(Call<ForecastResponse> call, Throwable t) {
                    Log.e("Forecast", "Failed to load 5-Day forecast", t);
                }
            });
    }

    private void setForecastRow(TextView dayTv,
                                TextView minTv,
                                TextView maxTv,
                                ImageView iconTv,
                                List<ForecastResponse.ForecastItem> items,
                                int index,
                                SimpleDateFormat dayFormat) {

        if (dayTv == null || minTv == null || maxTv == null || iconTv == null) return;

        // If the API didn't give enough entries, just clear this line...
        if (index >= items.size()) {
            dayTv.setText("");
            minTv.setText("");
            maxTv.setText("");
            iconTv.setVisibility(View.INVISIBLE);
            return;
        }

        ForecastResponse.ForecastItem item = items.get(index);

        // LEFT SIDE: date + description - Convert Unix time (seconds) -> Date (ms)...
        Date date = new Date(item.dt * 1000L);
        String dayStr = dayFormat.format(date);    // e.g., "Wed 12/3"

        String desc = "";
        String main = "";

        if (item.weather != null && !item.weather.isEmpty()) {
            if (item.weather.get(0).description != null) {
                desc = item.weather.get(0).description;
            }

            main = desc;


            if (item.weather.get(0).description != null) {
                main = item.weather.get(0).description;
            }
        }

        if (desc.isEmpty()) {
            dayTv.setText(dayStr);
        } else {
            dayTv.setText(dayStr + "; " + desc);
        }

        // RIGHT SIDE: Min / Max temps
        double min = 0.0;
        double max = 0.0;

        if (item.main != null) {
            min = item.main.tempMin;
            max = item.main.tempMax;
        }

        minTv.setText(String.format(Locale.getDefault(), "Min %.1f°F", min));
        maxTv.setText(String.format(Locale.getDefault(), "Max %.1f°F", max));

        // Icon in the middle...
        String iconKey = !desc.isEmpty() ? desc : main;
        iconTv.setImageResource(getWeatherIcon(iconKey));
        iconTv.setVisibility(View.VISIBLE);
    }
}