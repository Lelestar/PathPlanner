package com.ap4b.pathplanner.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the name of a road, identifying whether it's a highway, national road,
 * departmental road, European road, or a city name.
 */
public class RoadName {
    /** Road name */
    private String name;

    // Patterns to define non-city road types (e.g., A36, E54, etc.)
    private static final Pattern HIGHWAY_PATTERN = Pattern.compile(".*A(\\d+).*");
    private static final Pattern EUROPEAN_PATTERN = Pattern.compile(".*E(\\d+).*");
    private static final Pattern NATIONAL_PATTERN = Pattern.compile(".*N(\\d+).*");
    private static final Pattern DEPARTMENTAL_PATTERN = Pattern.compile(".*D(\\d+).*");

    /**
     * Constructs a new RoadName.
     *
     * @param name the road name
     */
    public RoadName(String name) {
        this.name = name;
    }

    /**
     * Checks if the road is a highway.
     *
     * @return true if the road is a highway, otherwise false
     */
    public boolean isHighway() {
        Matcher m = HIGHWAY_PATTERN.matcher(name);
        return m.matches();
    }

    /**
     * Checks if the road is a European road.
     *
     * @return true if the road is a European road, otherwise false
     */
    public boolean isEuropean() {
        Matcher m = EUROPEAN_PATTERN.matcher(name);
        return m.matches();
    }

    /**
     * Checks if the road is a national road.
     *
     * @return true if the road is a national road, otherwise false
     */
    public boolean isNational() {
        Matcher m = NATIONAL_PATTERN.matcher(name);
        return m.matches();
    }

    /**
     * Checks if the road is a departmental road.
     *
     * @return true if the road is a departmental road, otherwise false
     */
    public boolean isDepartmental() {
        Matcher m = DEPARTMENTAL_PATTERN.matcher(name);
        return m.matches();
    }

    /**
     * Checks if the road name suggests a city name (i.e., not any specific road type).
     *
     * @return true if the name suggests a city, otherwise false
     */
    public boolean isCity() {
        return (!isHighway() && !isEuropean() && !isNational() && !isDepartmental());
    }

    /**
     * Extracts the city name from a road name formatted as "city - roadName".
     *
     * @return the city name, or the entire road name if no specific city is indicated
     */
    public String extractCityName() {
        int dashIndex = name.indexOf('-');

        if (dashIndex > -1) {
            String cityName = name.substring(0, dashIndex).trim();
            return cityName;
        }
        return name;
    }
}