package com.ap4b.pathplanner.model;

import java.util.Vector;

public class Application {
    // Constants
    private final String ImagesPath = "/com/ap4b/pathplanner/img/";

    private final String mapLink;

    // Road Network
    private RoadNetwork roadNetwork;
    private Vector<ItineraryState> path;

    public Application(String XmlPath) {
        roadNetwork = new RoadNetwork();
        roadNetwork.parseXml(XmlPath);

        mapLink = ImagesPath + roadNetwork.getImageFileName();
    }

    public String getMapLink() {
        return mapLink;
    }
}
