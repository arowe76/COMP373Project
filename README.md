# Weather App
A modernized Android weather application built in Java, redesigned for COMP 373 to incorporate architectural thinking,
modular design, offline-awareness and expanded weather features such as a 5-day forecast.

This project evolves the original COMP 312 Weather App into a more robust, maintainable and architecture-driven mobile 
system aligned with the goals of software architecture and advanced object-oriented design.


## Mission Statement
Our mission is to collaboratively design and develop an Android weather application that applies real-world software
architecture principles, design patterns, and modular object-oriented programming techniques.


## Project Overview
This project extends a previously built Java weather service into a full Android application that:

    - Fetches real-time weather conditions using the OpenWeatherMap API
    - Displays current weather data (temperature, humidity, wind speed, etc.)
    - Dynamically updates UI background base on weather conditions
    - Supports caching and offline behavior
    - Provides a full 5-Day forecast with daily high/low temerpatures and cartoon icons
    - Allows users to search for weather by city
    - Utilizes architectural patterns such as MVA (Modern-View-Adapter), Observer and Facade
    - Implements WorkManager for auto-sync functionality (planned and partially scaffolded)


## Key Features Implemented
Current Weather Retrieval:

    - Real-time API calls using Retrofit and Gson
    - Displays temperature, humidity, wind speed, conditions, country, sunsrise/sunset, etc.
    - API key stored privately in local.properties

5-Day Forecast:

    - Parses multi-entry JSON forecast
    - Extracts daily min/max temperatures
    - Displays weather icons and day names
    - Integrates into the MainActivity UI layout

UI/UX Improvements:

    - Redesigned layout to support forecast weather
    - Added dynamic background colors depending on weather
    - Updated text colors to support visibility accross different backgrounds
    - Debugged UI issues using Toasts and Logcat


App Architecture Enhancements:

    - Repository + Cache layer (WeatherCache, WeatherRepository)
    - Refactor toward MVA architecture
    - Placeholder WeatherSyncWorker prepared for auto-sync through WorkManager

Build & Tooling Improvements:

    - Updated Gradle dependencies
    - Configured SDK compatibility
    - Cleaned up project structure for Android Studio/IntelliJ 
    - Improved documentation throughout Java classes


## Team Contribution Summary
Our team contributed to expanding and architecturally restructuring a pre-existing weather app.  Key contributions
include:

    - Integrated 5-Day Forecast Functionality
        * JSON parsing, UI elements, temperature formatting

    - Architectural Refactoring
        * Applied Model-View Adapter sturcture
        * Moved API logic into WeatherRepository
        * Added caching logic to support offline fallback
        * Improved object models (WeatherResponse, Forecast Response, nested classes)

    - Improving Code Readability & Maintainability
        * Added detailed comments explaining API calls, caching, background logic, & UI updates
        * Simplified MainActivity areas and methods

    - Sync & Automation Preparation
        * Implemented placeholder WeatherSyncWorker and prepared WorkManager integration
        * Designed flow for periodic background refresh

    - Technical Debugging
        * Fixed UI crashes, XML issues, broken click listeners
        * Used Logcat/Toast messages for debuggind Android-specified problems
        * Ensured that resources loaded correctly

    - Documentation & Collaboration
        * Updated README
        * Coordinated IntelliJ <--> GitHub syncing issues
        * Ensured secure API key storage
        * Created slide content for final presentation


## In Progress / Planned Changes
Short-Term Goals:

    - Better error handling for API failures
    - UI polish and accessibility improvements

Long-term Goals:

    - Sever weather alerts
    - User settings with a toggle button (units: °F/°C, theme switching)
    - Geolocation-based default city instead of always "Chicago"
    - Animations and improved icons


## Tools, Frameworks & Technologies
    - Language: Java
    - IDE: Android Studio/IntelliJ IDEA
    - Android Components:
        * Activities
        * WorkManager
        * View System (TextView, ImageView, Layouts)
    - Networking: Retrofit + OkHttp
    - JSON Parsing: Gson
    - Architecture: 
        * Model-View-Adapter (MVA)
        * Observer pattern
        * Facade pattern
        * Single Responsibility Principle (SRP)
        * Dependency Inversion Princiiple (DIP)


## System Architecture Summary
Core Components:

    * MainActivity - UI controller, handles search & main display
    * WeatherRepository - mediates between API + cache
    * WeatherCache - stores last successful responses
    * ApiClient/WeatherApiService - handles Retrofit API calls
    * WeatherResponse/ForecastResponse - data models for JSON mapping
    * WeatherSyncWorker - planned periodic sync worker