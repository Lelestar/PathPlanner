package com.ap4b.pathplanner.model;

import com.ap4b.pathplanner.view.AppWindow;

import java.util.Arrays;
import java.util.Vector;

public class Application {
    // Constants
    private final String ImagesPath = "/com/ap4b/pathplanner/img/";

    private final String mapLink;

    // View
    private AppWindow appWindow;

    // Road Network
    private RoadNetwork roadNetwork;
    private Vector<ItineraryState> path;

    public Application(String XmlPath) {
        roadNetwork = new RoadNetwork();
        roadNetwork.parseXml(XmlPath);

        mapLink = ImagesPath + roadNetwork.getImageFileName();
    }

    public void setAppWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
        initializeAfterAppWindowSet(); // Initialize after setting the window
    }

    private void initializeAfterAppWindowSet() {
        fillCitiesLists();
    }

    public String getMapLink() {
        return mapLink;
    }

    public void fillCitiesLists() {
        // Fill the cities lists
        Object[] roadsList = roadNetwork.getRoadNames().toArray();
        Arrays.sort(roadsList);
        String city;
        RoadName road;
        for (int l = 0; l < roadNetwork.getRoadCount(); l++) {
            road = new RoadName((String) roadsList[l]);
            if(road.isCity()){
                city = road.extractCityName();
                if(!appWindow.getDepartureArrivalPanel().isCityAlreadyAdded(city))
                    appWindow.getDepartureArrivalPanel().addCity(city);
            }
        }
    }
}
