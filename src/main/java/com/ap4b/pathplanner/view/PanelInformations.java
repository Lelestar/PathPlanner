package com.ap4b.pathplanner.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class PanelInformations for JavaFX.
 */
public class PanelInformations extends VBox {

    private Label lblInfos;
    private ListView<String> listInfos;
    private Label lblRouteSheet;
    private ListView<String> listRouteSheet;
    private List<String> routeDetails = new ArrayList<>();

    private String message1, message2, su, pathLength;

    public String setPathLength(String pathLength){
        return this.pathLength = pathLength;
    }

    public PanelInformations(int width, double height, String unitsSystem) {
        super(10); // spacing between elements
        setPadding(new Insets(10));

        // Information Label
        lblInfos = new Label("Informations:");
        lblInfos.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(lblInfos);

        // Information List
        listInfos = new ListView<>();
        listInfos.setPrefSize(width, 100);
        listInfos.setMinHeight(100);
        getChildren().add(listInfos);

        // Route Sheet Label
        lblRouteSheet = new Label("Route Sheet:");
        lblRouteSheet.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(lblRouteSheet);

        // Route Sheet List
        listRouteSheet = new ListView<>();
        listRouteSheet.setPrefWidth(width);
        getChildren().add(listRouteSheet);
        VBox.setVgrow(listRouteSheet, Priority.ALWAYS);

        initializeDefaultMessages(unitsSystem);
    }

    private void initializeDefaultMessages(String unitsSystem) {
        listInfos.getItems().addAll(
                "Welcome!",
                "Choose an itinerary.",
                "System of units: " + unitsSystem
        );

        listRouteSheet.getItems().add("No route selected.");
    }

    public void addRoute(String route) {
        routeDetails.add(route);
        listRouteSheet.getItems().add(route);
    }

    public void updateInfos(List<String> infos) {
        listInfos.getItems().clear();
        listInfos.getItems().addAll(infos);
    }

    public void updateRoads(List<String> roads) {
        listRouteSheet.getItems().clear();
        listRouteSheet.getItems().addAll(roads);
    }

    public void clearInfos() {
        listInfos.getItems().clear();
    }

    public void clearRoads() {
        listRouteSheet.getItems().clear();
        routeDetails.clear();
    }

    // Example method to set messages
    public void setMessage(String message1, String message2) {
        clearInfos();
        updateInfos(List.of(message1, message2));
    }

    public void setLengthPath(String lengthPath){};
}

