package com.ap4b.pathplanner.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * The Class PanelControls for JavaFX.
 */
public class PanelControls extends HBox {

    private ComboBox<String> cbCityDeparture, cbCityArrival;
    private ComboBox<String> cbStreetDeparture, cbStreetArrival;
    private ComboBox<String> cbPointDeparture, cbPointArrival;
    private Button btnGo, btnZoomIn, btnZoomOut;
    private Slider zoomSlider;

    public PanelControls() {
        // Setting up the main layout
        GridPane leftPanel = createSection();
        GridPane rightPanel = createSection();

        // Configure Left Panel for Departure and Arrival Inputs
        setupDepartureArrivalSection(leftPanel);

        // Configure Right Panel for Zoom Controls
        setupZoomSection(rightPanel);

        this.getChildren().addAll(leftPanel, rightPanel);
        this.setSpacing(10);
    }

    private GridPane createSection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private void setupDepartureArrivalSection(GridPane panel) {
        Label lblCity = new Label("City:");
        Label lblStreet = new Label("Street:");
        Label lblPoint = new Label("Point:");

        cbCityDeparture = new ComboBox<>();
        cbStreetDeparture = new ComboBox<>();
        cbPointDeparture = new ComboBox<>();
        cbCityArrival = new ComboBox<>();
        cbStreetArrival = new ComboBox<>();
        cbPointArrival = new ComboBox<>();

        btnGo = new Button("Go");

        panel.addRow(0, new Label("Departure"), new Label("Arrival"));
        panel.addRow(1, lblCity, cbCityDeparture, cbCityArrival);
        panel.addRow(2, lblStreet, cbStreetDeparture, cbStreetArrival);
        panel.addRow(3, lblPoint, cbPointDeparture, cbPointArrival);
        panel.add(btnGo, 1, 4, 2, 1);
    }

    private void setupZoomSection(GridPane panel) {
        Label lblZoom = new Label("Zoom:");
        btnZoomIn = new Button("+");
        btnZoomOut = new Button("-");
        zoomSlider = new Slider(0, 100, 50);  // Example values

        panel.addRow(0, lblZoom);
        panel.addRow(1, btnZoomOut, zoomSlider, btnZoomIn);
    }

    // Additional methods for setting up event handlers, updating comboboxes, etc.
    // For example:
    public void addCity(String city, boolean isDeparture, boolean isArrival) {
        if (isDeparture) {
            cbCityDeparture.getItems().add(city);
        }
        if (isArrival) {
            cbCityArrival.getItems().add(city);
        }
    }
}
