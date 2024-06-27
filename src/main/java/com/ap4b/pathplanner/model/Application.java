package com.ap4b.pathplanner.model;

import com.ap4b.pathplanner.view.AboutWindow;
import com.ap4b.pathplanner.view.AppWindow;
import com.ap4b.pathplanner.view.DepartureArrivalPanel;
import com.ap4b.pathplanner.view.FileSelectionWindow;
import javafx.scene.control.Alert;

import java.util.*;
import java.lang.Math;

public class Application {
    // Constants
    private final String ImagesPath = "/com/ap4b/pathplanner/img/";
    private String mapLink;
    private double scale; // 1 pixel = scale meters
    private float mapScale; // Scale of the map compared to the original image
    private Point lambertTopLeft;
    private Point lambertBottomRight;
    private Point pixelsBottomRight;
    private final float INITIAL_ZOOM = 0.5f;
    public final static float ZOOM_NOTCH = (float) 0.05;
    private float zoomPercentage = INITIAL_ZOOM;
    private float oldZoom = -1;
    private final float ZOOM_MIN = (float) 0.1;
    private final float ZOOM_MAX = (float) 1;
    private final int SCALE_SIZE_PX = 50;

    // View
    private AppWindow appWindow;

    private boolean themeIsLight = true;
    private boolean modeIsEdition = false;

    // Road Network
    private RoadNetwork roadNetwork;
    private ShortestPath shortestPath;
    private Vector<ItineraryState> path;

    // Departure and Arrival points numbers
    private int departurePoint = -1;
    private int arrivalPoint = -1;

    private int mouseNearestPoint = -1;

    public Application(String XmlPath) {
        roadNetwork = new RoadNetwork();
        roadNetwork.parseXml(XmlPath);

        mapLink = ImagesPath + roadNetwork.getImageFileName();
        scale = roadNetwork.getScale();
        mapScale = (float) roadNetwork.getMapScale();
        lambertTopLeft = roadNetwork.getLambertTopLeft();
        lambertBottomRight = roadNetwork.getLambertBottomRight();
        pixelsBottomRight = roadNetwork.getPixelsBottomRight();

        shortestPath = new ShortestPath();
        shortestPath.init(roadNetwork, mapScale);
    }

    public void setAppWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
        initializeAfterAppWindowSet(); // Initialize after setting the window
    }

    private void initializeAfterAppWindowSet() {
        fillCitiesLists();
        fillRoadsLists("All", DepartureArrivalPanel.Direction.BOTH);
        fillPointsLists(DepartureArrivalPanel.Direction.BOTH);
        appWindow.getMap().setScaleSize(SCALE_SIZE_PX);
        appWindow.getMap().setScaleLabel(convertUnitDistance(SCALE_SIZE_PX, zoomPercentage));
        setZoom();
    }

    public String getMapLink() {
        return mapLink;
    }

    public float getZoomPercentage() {
        return zoomPercentage;
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
        // Reset the combo boxes
        appWindow.getDepartureArrivalPanel().resetPoints(direction);
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
        searchItineraryFromPanel();

    }

    public void setDeparturePoint(int departurePoint) {
        this.departurePoint = departurePoint;
        appWindow.getMap().resetItinerary();

        Set roads = roadNetwork.getRoadsList();
        Iterator itRoads;
        String roadName="";
        Boolean found = false;
        for (itRoads = roads.iterator(); itRoads.hasNext(); ) {
             roadName = (String) itRoads.next();
             Vector<Integer> points = roadNetwork.getRoad(roadName).getPoints();
             for (int j = 0; j < points.size(); j++) {
                 if (points.elementAt(j) == departurePoint) {
                     found = true;
                 }
             }
            if(found) {
                break;
            }
        }

        appWindow.getDepartureArrivalPanel().clearCbDeparture();

        appWindow.getDepartureArrivalPanel().setCbDeparture(departurePoint, roadName);
    }


    public void setArrivalPoint(int arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
        appWindow.getMap().resetItinerary();

        Set roads = roadNetwork.getRoadsList();
        Iterator itRoads;
        String roadName="";
        Boolean found = false;
        for (itRoads = roads.iterator(); itRoads.hasNext(); ) {
            roadName = (String) itRoads.next();
            Vector<Integer> points = roadNetwork.getRoad(roadName).getPoints();
            for (int j = 0; j < points.size(); j++) {
                if (points.elementAt(j) == arrivalPoint) {
                    found = true;
                }
            }
            if(found) {
                break;
            }
        }

        appWindow.getDepartureArrivalPanel().clearCbArrival();

        appWindow.getDepartureArrivalPanel().setCbArrival(arrivalPoint, roadName);
    }

    public void searchItineraryFromPanel() {
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
            appWindow.getMap().recenterViewOnItinerary();
            displayRoadList();
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

    public void searchItineraryFromMap() {
        if (departurePoint >= 0) {
            appWindow.getMap().deleteFirst();
            appWindow.getMap().addPoint(shortestPath.getNodeCoords(departurePoint));
        }
        else if (arrivalPoint >= 0) {
            appWindow.getMap().deleteLast();
            appWindow.getMap().addPoint(shortestPath.getNodeCoords(arrivalPoint));
        }

        if ((departurePoint >= 0) && (arrivalPoint >= 0)) {
            path = shortestPath.solve(departurePoint, arrivalPoint);
            ItineraryState pos;
            for (Iterator it = path.iterator(); it.hasNext();)
            {
                pos = (ItineraryState) it.next();
                appWindow.getMap().addPoint(shortestPath.getNodeCoords(pos.node));
            }
            appWindow.getMap().recenterViewOnItinerary();
            displayRoadList();
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

    public void updateNearestPoint(int x, int y) {
        Point mousePoint = new Point(x, y);

        Point previousPoint = appWindow.getMap().getNearestPoint();
        // Find the nearest point
        int nearestPointId = shortestPath.findNearestNode(mousePoint);
        Point nearestPoint = shortestPath.getNodeCoords(nearestPointId);

        if (previousPoint == null || !previousPoint.equals(nearestPoint)) {
            mouseNearestPoint = nearestPointId;
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
        int extentX = (int) (lambertBottomRight.getX() - lambertTopLeft.getX());
        int extentY = (int) (lambertBottomRight.getY() - lambertTopLeft.getY());

        // Calculate the proportional Lambert coordinates based on the pixel coordinates
        double lambertZeroX = (double) (pt_pixels.getX() * extentX) / pixelsBottomRight.getX();
        double lambertZeroY = (double) (pt_pixels.getY() * extentY) / pixelsBottomRight.getY();

        // Convert to final Lambert coordinates by adding the top-left Lambert coordinates
        int x = (int) (lambertTopLeft.getX() + lambertZeroX);
        int y = (int) (lambertTopLeft.getY() + lambertZeroY);

        // Return the calculated Lambert coordinates as a new point
        return new Point(x, y);
    }

    public void modifyZoom(float modification){
        zoomPercentage+=modification;
        setZoom();
        appWindow.getMap().setScaleLabel(convertUnitDistance(SCALE_SIZE_PX, zoomPercentage));
    }

    public void setZoomByValue(float value){
        zoomPercentage = value;
        setZoom();
        appWindow.getMap().setScaleLabel(convertUnitDistance(SCALE_SIZE_PX, zoomPercentage));
    }

    public void setZoom(){
        if (zoomPercentage > ZOOM_MAX) zoomPercentage = ZOOM_MAX;
        if (zoomPercentage < ZOOM_MIN) zoomPercentage = ZOOM_MIN;
        if (oldZoom != zoomPercentage) {
            appWindow.getZoomControl().setZoomSlider(zoomPercentage);
            appWindow.getMap().updateZoom(zoomPercentage);
        }
    }

    /**
     * Sets the nearest point as start.
     */
    public void setNearestPointAsStart() {
        setDeparturePoint(mouseNearestPoint);
        appWindow.getMap().setTypePointUnique(true);
        searchItineraryFromMap();
    }

    /**
     * Sets the nearest point as arrival.
     */
    public void setNearestPointAsArrival() {
        setArrivalPoint(mouseNearestPoint);
        appWindow.getMap().setTypePointUnique(false);
        searchItineraryFromMap();
    }


    public void displayRoadList(){
        appWindow.getPanelInformations().clearRoads();
        //appWindow.getPanelInformations().setMessage(null, null);
        String roadName = "", formerRoadName = "";
        ItineraryState pos = null;
        int lenRoad = 0;
        int lenTotal = 0;
        int idEdge;
        int numPt, numFormerPt = -1, numFormerPt2 = -1;
        String leftRight;
        appWindow.getPanelInformations().clearRoads();

        for (Iterator it = path.iterator(); it.hasNext(); ) {
            pos = (ItineraryState) it.next();
            numPt = pos.node;
            if (numFormerPt >= 0) {
                idEdge = shortestPath.findEdge(numPt, numFormerPt);
                if (idEdge >=0) {
                    roadName = shortestPath.getEdgeName(idEdge);
                    lenRoad += shortestPath.getEdgeLength(idEdge);
                    if ((!formerRoadName.equals(roadName)) || (!it.hasNext()))
                        if (numFormerPt2 != -1) {
                            leftRight = determineLeftRight(numFormerPt2, numFormerPt, numPt);
                        }
                        else {
                            leftRight = "tout-droit";
                        }
                        appWindow.getPanelInformations().addRoute(roadName + " (" + convertUnitDistance(lenRoad, 1) + ")");
                        lenTotal += lenRoad;
                        lenRoad=0;
                    }
                    formerRoadName = roadName;
                }
                else if (numFormerPt != -1){
                    appWindow.getPanelInformations().addRoute("Erreur : Route non trouv\u00e9e ! (" + numFormerPt + "|" + numPt + ")");
                }
                numFormerPt2 = numFormerPt;
                numFormerPt = numPt;
            }
            appWindow.getPanelInformations().setPathLength(convertUnitDistance(lenTotal,1));
    }



    private String convertUnitDistance(double px, float zoom) {
        double m = (double)(px * (double) scale * (double)((double)1 / (double)zoom));
        String unit = (m > 1000) ? " km" : " m";
        if (m > 1000) {
            m /= 1000;
        }
        m = ((double) Math.round(m * 100)) / 100;
        return m + unit;
    }

    private String determineLeftRight(int id1, int id2, int id3) {
        Point p1 = roadNetwork.getPoint(id1);
        Point p2 = roadNetwork.getPoint(id2);
        Point p3 = roadNetwork.getPoint(id3);

        //determiner l'angle entre les deux droites

        //clacul de l'angle du precedent arc par rapport a l'origine
        double angle1 = (Math.atan2((p2.getY()-p1.getY()),(p2.getX()-p1.getX())));

        //calcul de l'angle de l'arc deux fois precedent par rapport a l'origine
        double angle2 = (Math.atan2((p3.getY()-p1.getY()),(p3.getX()-p1.getX())));

        //soustraction de l'un par rapport a l'autre pour avoir leur angle relatif
        double angle = angle2-angle1;

        if(Math.sin(angle)<-0.1)
            return "gauche";
        else if (Math.sin(angle)>0.1)
            return "droite";
        else
            return "tout_droit";
    }

    public void about(){
        String cssPath;
        if (themeIsLight){
            cssPath = "/com/ap4b/pathplanner/style_light.css";
        } else {
            cssPath = "/com/ap4b/pathplanner/style_dark.css";
        }
        new AboutWindow(cssPath);
    }

    public void handleMapChange() {
        String cssPath;
        if (themeIsLight) {
            cssPath = "/com/ap4b/pathplanner/style_light.css";
        } else {
            cssPath = "/com/ap4b/pathplanner/style_dark.css";
        }
        FileSelectionWindow.display(this::changeMap, cssPath);
    }

    public void changeMap(String filePath){
        roadNetwork = new RoadNetwork();
        roadNetwork.parseXml(filePath);

        scale = roadNetwork.getScale();
        mapScale = (float) roadNetwork.getMapScale();
        lambertTopLeft = roadNetwork.getLambertTopLeft();
        lambertBottomRight = roadNetwork.getLambertBottomRight();
        pixelsBottomRight = roadNetwork.getPixelsBottomRight();

        this.mapLink = ImagesPath + roadNetwork.getImageFileName();

        setZoomByValue(INITIAL_ZOOM);
        shortestPath.init(roadNetwork, mapScale);

        appWindow.getDepartureArrivalPanel().resetCities();
        appWindow.getDepartureArrivalPanel().resetStreets(DepartureArrivalPanel.Direction.BOTH);
        appWindow.getDepartureArrivalPanel().resetPoints(DepartureArrivalPanel.Direction.BOTH);

        fillCitiesLists();
        fillRoadsLists("All", DepartureArrivalPanel.Direction.BOTH);
        fillPointsLists(DepartureArrivalPanel.Direction.BOTH);

        appWindow.getMap().updateMap(mapLink);
        appWindow.getMap().setScaleSize(SCALE_SIZE_PX);
        appWindow.getMap().setScaleLabel(convertUnitDistance(SCALE_SIZE_PX, zoomPercentage));
    }

    public void switchTheme(){
        if (themeIsLight){
            appWindow.getPrimaryStage().getScene().getStylesheets().remove(Objects.requireNonNull(getClass().getResource("/com/ap4b/pathplanner/style_light.css")).toExternalForm());
            appWindow.getPrimaryStage().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ap4b/pathplanner/style_dark.css")).toExternalForm());
            themeIsLight = false;
        } else {
            appWindow.getPrimaryStage().getScene().getStylesheets().remove(Objects.requireNonNull(getClass().getResource("/com/ap4b/pathplanner/style_dark.css")).toExternalForm());
            appWindow.getPrimaryStage().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ap4b/pathplanner/style_light.css")).toExternalForm());
            themeIsLight = true;
        }
    }

    public void switchMode(){
        modeIsEdition = !modeIsEdition;
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public boolean isModeIsEdition() {
        return modeIsEdition;
    }

    public boolean getThemeIsLight() {
        return themeIsLight;
    }
}
