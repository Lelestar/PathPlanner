package com.ap4b.pathplanner.model;

import com.ap4b.pathplanner.view.AppWindow;
import com.ap4b.pathplanner.view.DepartureArrivalPanel;
import javafx.scene.control.Alert;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Application {
    // Constants
    private final String ImagesPath = "/com/ap4b/pathplanner/img/";
    private final String mapLink;
    private final double MAP_SCALE = 18.388125;
    private final float INITIAL_ZOOM = (float) 0.5;
    private final Point LAMBERT_TOP_LEFT = new Point(897990, 2324046);
    private final Point LAMBERT_BOTTOM_RIGHT = new Point(971518, 2272510);
    private final Point PIXELS_BOTTOM_RIGHT = new Point(4000, 2801);

    // View
    private AppWindow appWindow;

    // Road Network
    private RoadNetwork roadNetwork;
    private ShortestPath shortestPath;
    private Vector<ItineraryState> path;

    // Departure and Arrival points numbers
    private int departurePoint = -1;
    private int arrivalPoint = -1;

    public Application(String XmlPath) {
        roadNetwork = new RoadNetwork();
        roadNetwork.parseXml(XmlPath);

        mapLink = ImagesPath + roadNetwork.getImageFileName();

        shortestPath = new ShortestPath();
        shortestPath.init(roadNetwork, INITIAL_ZOOM);
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
        if ((departurePoint >= 0) && (arrivalPoint >= 0)) {
            path = shortestPath.solve(departurePoint, arrivalPoint);
            ItineraryState pos;
            for (Iterator it = path.iterator(); it.hasNext();)
            {
                pos = (ItineraryState) it.next();
                appWindow.getMap().addPoint(shortestPath.getNodeCoords(pos.node));
            }
            //afficherListeRoutes();
        }
        /**else {
            if (departurePoint >= 0) {
                fenetre.getPanneauVue().getCarte().ajouterPoint(plus_court_chemin.getNodeCoords(depart));
            }
            else if (arrivee >= 0) {
                fenetre.getPanneauVue().getCarte().ajouterPoint(plus_court_chemin.getNodeCoords(arrivee));
            }
        }*/
    }

    public void createErrorAlert(String title, String header, String content) {
        // Create an alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void updateNearestPoint(int x, int y) {
        Point mousePoint = new Point(x, y);

        Point previousPoint = appWindow.getMap().getNearestPoint();
        // Find the nearest point
        int nearestPointId = shortestPath.findNearestNode(mousePoint);
        Point nearestPoint = shortestPath.getNodeCoords(nearestPointId);

        if (previousPoint == null || !previousPoint.equals(nearestPoint)) {
            // Add data to the nearest point
            Vector<String> infos = new Vector<>();
            infos.add("Point " + nearestPointId);
            // Add the Lambert coordinates
            Point newCoords = getLambertCoords(nearestPoint);
            infos.add("Coordinates: (" + newCoords.getX() + ", " + newCoords.getY() + ")");
            // Search for the roads
            Set roads = roadNetwork.getRoadsList();
            Iterator itRoads;
            String roadName;
            for (itRoads = roads.iterator(); itRoads.hasNext(); ) {
                roadName = (String) itRoads.next();
                Vector<Integer> points = roadNetwork.getRoad(roadName).getPoints();
                for (int j = 0; j < points.size(); j++) {
                    if (points.elementAt(j) == nearestPointId) {
                        infos.add("Road: " + roadName);
                    }
                }
            }

            // Set the nearest point
            nearestPoint.setInfos(infos);
            appWindow.getMap().setNearestPoint(nearestPoint);
            appWindow.getMap().draw();
        }
    }

    /**
     * Gets the Lambert coordinates.
     *
     * @param pt_pixels the point in pixel coordinates
     * @return the Lambert coordinates
     */
    public Point getLambertCoords(Point pt_pixels) {
        // Calculate the extent of the Lambert coordinates in x and y directions
        int extentX = (int) (LAMBERT_BOTTOM_RIGHT.getX() - LAMBERT_TOP_LEFT.getX());
        int extentY = (int) (LAMBERT_BOTTOM_RIGHT.getY() - LAMBERT_TOP_LEFT.getY());

        // Calculate the proportional Lambert coordinates based on the pixel coordinates
        double lambertZeroX = (double) (pt_pixels.getX() * extentX) / PIXELS_BOTTOM_RIGHT.getX();
        double lambertZeroY = (double) (pt_pixels.getY() * extentY) / PIXELS_BOTTOM_RIGHT.getY();

        // Convert to final Lambert coordinates by adding the top-left Lambert coordinates
        int x = (int) (LAMBERT_TOP_LEFT.getX() + lambertZeroX);
        int y = (int) (LAMBERT_TOP_LEFT.getY() + lambertZeroY);

        // Return the calculated Lambert coordinates as a new point
        return new Point(x, y);
    }
}
