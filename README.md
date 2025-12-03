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

This aligns closely with project outline proposal.







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