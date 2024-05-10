package com.ap4b.pathplanner.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The Class DepartureArrivalPanel for JavaFX.
 */
public class DepartureArrivalPanel extends HBox {

    private ComboBox<String> cbCityDeparture, cbCityArrival;
    private ComboBox<String> cbStreetDeparture, cbStreetArrival;
    private ComboBox<String> cbPointDeparture, cbPointArrival;
    private Button btnGo;

    public DepartureArrivalPanel() {
        setPadding(new Insets(10, 10, 10, 0));
        setSpacing(10);
        setMaxWidth(300);

        Label lblDeparture = new Label("Departure");
        lblDeparture.setFont(Font.font("Arial",FontWeight.BOLD, 14));

        Label lblArrival = new Label("Arrival");
        lblArrival.setFont(Font.font("Arial",FontWeight.BOLD, 14));

        cbCityDeparture = new ComboBox<>();
        cbStreetDeparture = new ComboBox<>();
        cbPointDeparture = new ComboBox<>();
        cbCityArrival = new ComboBox<>();
        cbStreetArrival = new ComboBox<>();
        cbPointArrival = new ComboBox<>();
        btnGo = new Button("Go");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Define column constraints: one column for labels, one for comboboxes
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPercentWidth(40);  // 40% width for labels

        ColumnConstraints comboBoxColumn = new ColumnConstraints();
        comboBoxColumn.setPercentWidth(70);  // 70% width for ComboBoxes

        grid.getColumnConstraints().addAll(labelColumn, comboBoxColumn);

        // Add departure section
        grid.add(lblDeparture, 0, 0, 2, 1);  // Span across both columns
        grid.addRow(1, new Label("City"), cbCityDeparture);
        grid.addRow(2, new Label("Street"), cbStreetDeparture);
        grid.addRow(3, new Label("Point"), cbPointDeparture);

        // Add arrival section
        grid.add(lblArrival, 0, 4, 2, 1);  // Span across both columns
        grid.addRow(5, new Label("City"), cbCityArrival);
        grid.addRow(6, new Label("Street"), cbStreetArrival);
        grid.addRow(7, new Label("Point"), cbPointArrival);

        // Add go button
        grid.add(btnGo, 1, 8, 2, 1);

        this.getChildren().add(grid);
    }

    public void addCity(String city) {
        cbCityDeparture.getItems().add(city);
        cbCityArrival.getItems().add(city);
    }

    public boolean isCityAlreadyAdded(String city) {
        return cbCityDeparture.getItems().contains(city);
    }
}
