package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.view.DepartureArrivalPanel;

public class DepartureArrivalPanelController {

    private Application app;
    private DepartureArrivalPanel departureArrivalPanel;

    public DepartureArrivalPanelController(Application app, DepartureArrivalPanel departureArrivalPanel) {
        this.app = app;
        this.departureArrivalPanel = departureArrivalPanel;

        addEventHandlers();
    }

    private void addEventHandlers() {
        departureArrivalPanel.getCbCityDeparture().setOnAction(
                event -> {
                    if (departureArrivalPanel.getCbCityDeparture().getValue() != null) {
                        app.fillRoadsLists(
                                departureArrivalPanel.getCbCityDeparture().getValue(),
                                DepartureArrivalPanel.Direction.DEPARTURE
                        );
                    }
                }
        );

        departureArrivalPanel.getCbCityArrival().setOnAction(
                event -> {
                    if (departureArrivalPanel.getCbCityArrival().getValue() != null) {
                        app.fillRoadsLists(
                                departureArrivalPanel.getCbCityArrival().getValue(),
                                DepartureArrivalPanel.Direction.ARRIVAL
                        );
                    }
                }
        );

        departureArrivalPanel.getCbStreetDeparture().setOnAction(
                event -> app.fillPointsLists(DepartureArrivalPanel.Direction.DEPARTURE)
        );

        departureArrivalPanel.getCbStreetArrival().setOnAction(
                event -> app.fillPointsLists(DepartureArrivalPanel.Direction.ARRIVAL)
        );

        departureArrivalPanel.getGoButton().setOnAction(
                event -> app.readDepartureArrivalPanelForItinerary()
        );
    }
}
