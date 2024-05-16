package com.ap4b.pathplanner.model;

import com.ap4b.pathplanner.view.AppWindow;
import com.ap4b.pathplanner.view.DepartureArrivalPanel;
import javafx.scene.control.Alert;

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

    // Departure and Arrival points numbers
    private int departurePoint = -1;
    private int arrivalPoint = -1;

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
        fillRoadsLists("All", DepartureArrivalPanel.Direction.BOTH);
        fillPointsLists(DepartureArrivalPanel.Direction.BOTH);
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
                appWindow.getDepartureArrivalPanel().addCity(city);
            }
        }
    }

    public void fillRoadsLists(String city, DepartureArrivalPanel.Direction direction) {
        // Fill the roads lists
        Object[] roadsList = roadNetwork.getRoadNames().toArray();
        Arrays.sort(roadsList);

        // Reset the combo boxes
        appWindow.getDepartureArrivalPanel().resetStreets(direction);

        // Add the roads to the combo box
        for (int l = 0; l < roadNetwork.getRoadCount(); l++) {
            RoadName road = new RoadName((String) roadsList[l]);
            if (road.extractCityName().equals(city) || city.equals("All")) {
                appWindow.getDepartureArrivalPanel().addStreet(roadsList[l].toString(), direction);
            }
        }
    }

    public void fillPointsLists(DepartureArrivalPanel.Direction direction) {
        // Fill the points lists
        String roadName = appWindow.getDepartureArrivalPanel().getSelectedStreet(direction);
        if (roadName != null) {
            Vector<Integer> points = roadNetwork.getRoad(roadName).getPoints();
            for (int i = 0; i < points.size(); i++) {
                appWindow.getDepartureArrivalPanel().addPoint(points.elementAt(i), direction);
            }
        }
    }

    public void readDepartureArrivalPanelForItinerary() {
        // Read the departure and arrival points
        int departurePointNumber = appWindow.getDepartureArrivalPanel().getPointNumber(DepartureArrivalPanel.Direction.DEPARTURE);
        int arrivalPointNUmber = appWindow.getDepartureArrivalPanel().getPointNumber(DepartureArrivalPanel.Direction.ARRIVAL);
        setArrivalPoint(arrivalPointNUmber);
        setDeparturePoint(departurePointNumber);
        // Find the shortest path
        searchItinerary();

    }

    public void setDeparturePoint(int departurePoint) {
        this.departurePoint = departurePoint;
    }

    public void setArrivalPoint(int arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
    }

    public void searchItinerary() {
        // Check if the departure and arrival points are set
        if (departurePoint == -1 || arrivalPoint == -1) {
            // Create an alert
            createErrorAlert("Error", "Missing departure or arrival point", "Please select a departure and an arrival point.");
            return;
        }
        // Check if the departure and arrival points are the same
        if (departurePoint == arrivalPoint) {
            // Create an alert
            createErrorAlert("Error", "Same departure and arrival point", "Please select different departure and arrival points.");
            return;
        }
    }

    public void createErrorAlert(String title, String header, String content) {
        // Create an alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
