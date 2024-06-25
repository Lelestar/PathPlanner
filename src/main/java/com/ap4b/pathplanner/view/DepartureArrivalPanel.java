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

    public enum Direction {
        DEPARTURE,
        ARRIVAL,
        BOTH
    }

    public DepartureArrivalPanel() {
        setPadding(new Insets(10, 10, 10, 0));
        setSpacing(10);
        setMaxWidth(300);

        Label lblDeparture = new Label("Departure");
        lblDeparture.getStyleClass().add("app-label");

        Label lblArrival = new Label("Arrival");
        lblArrival.getStyleClass().add("app-label");

        cbCityDeparture = new ComboBox<>();
        cbStreetDeparture = new ComboBox<>();
        cbPointDeparture = new ComboBox<>();
        cbCityArrival = new ComboBox<>();
        cbStreetArrival = new ComboBox<>();
        cbPointArrival = new ComboBox<>();
        btnGo = new Button("Go");
        btnGo.getStyleClass().add("app-button");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Define column constraints: one column for labels, one for comboboxes
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMaxWidth(100);

        ColumnConstraints comboBoxColumn = new ColumnConstraints();
        comboBoxColumn.setMaxWidth(210);

        grid.getColumnConstraints().addAll(labelColumn, comboBoxColumn);

        // Add departure section
        grid.add(lblDeparture, 0, 0, 2, 1);  // Span across both columns

        Label lblCityDeparture = new Label("City");
        lblCityDeparture.getStyleClass().add("app-text");

        Label lblStreetDeparture = new Label("Street");
        lblStreetDeparture.getStyleClass().add("app-text");

        Label lblPointDeparture = new Label("Point");
        lblPointDeparture.getStyleClass().add("app-text");

        grid.addRow(1, lblCityDeparture, cbCityDeparture);
        grid.addRow(2, lblStreetDeparture, cbStreetDeparture);
        grid.addRow(3, lblPointDeparture, cbPointDeparture);

        // Add arrival section
        grid.add(lblArrival, 0, 4, 2, 1);  // Span across both columns

        Label lblCityArrival = new Label("City");
        lblCityArrival.getStyleClass().add("app-text");

        Label lblStreetArrival = new Label("Street");
        lblStreetArrival.getStyleClass().add("app-text");

        Label lblPointArrival = new Label("Point");
        lblPointArrival.getStyleClass().add("app-text");

        grid.addRow(5, lblCityArrival, cbCityArrival);
        grid.addRow(6, lblStreetArrival, cbStreetArrival);
        grid.addRow(7, lblPointArrival, cbPointArrival);

        // Add go button
        grid.add(btnGo, 0, 8, 2, 1);
        btnGo.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().add(grid);
    }

    public void addCity(String city) {
        if (cbCityDeparture.getItems().contains(city)) {
            return;
        }
        cbCityDeparture.getItems().add(city);
        cbCityArrival.getItems().add(city);
    }

    public void addStreet(String street, Direction direction) {
        if (direction == Direction.DEPARTURE) {
            if (cbStreetDeparture.getItems().contains(street)) {
                return;
            }
            cbStreetDeparture.getItems().add(street);
        } else if (direction == Direction.ARRIVAL) {
            if (cbStreetArrival.getItems().contains(street)) {
                return;
            }
            cbStreetArrival.getItems().add(street);
        } else {
            if (cbStreetDeparture.getItems().contains(street) || cbStreetArrival.getItems().contains(street)) {
                return;
            }
            cbStreetDeparture.getItems().add(street);
            cbStreetArrival.getItems().add(street);
        }
    }

    public void addPoint(Integer point, Direction direction) {
        if (direction == Direction.DEPARTURE) {
            if (cbPointDeparture.getItems().contains(point.toString())) {
                return;
            }
            cbPointDeparture.getItems().add(point.toString());
        } else if (direction == Direction.ARRIVAL) {
            if (cbPointArrival.getItems().contains(point.toString())) {
                return;
            }
            cbPointArrival.getItems().add(point.toString());
        } else {
            if (cbPointDeparture.getItems().contains(point.toString()) || cbPointArrival.getItems().contains(point.toString())) {
                return;
            }
            cbPointDeparture.getItems().add(point.toString());
            cbPointArrival.getItems().add(point.toString());
        }
    }

    public String getSelectedStreet(Direction direction) {
        if (direction == Direction.DEPARTURE) {
            return cbStreetDeparture.getSelectionModel().getSelectedItem();
        } else if (direction == Direction.ARRIVAL) {
            return cbStreetArrival.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }
    }

    public void resetStreets(Direction direction) {
        if (direction == Direction.DEPARTURE) {
            cbStreetDeparture.getItems().clear();
        } else if (direction == Direction.ARRIVAL) {
            cbStreetArrival.getItems().clear();
        } else {
            cbStreetDeparture.getItems().clear();
            cbStreetArrival.getItems().clear();
        }
    }

    public void clearCbDeparture(){
        cbCityDeparture.setValue(null);
        cbStreetDeparture.setValue(null);
        cbPointDeparture.setValue(null);
    }

    public void clearCbArrival(){
        cbCityArrival.setValue(null);
        cbStreetArrival.setValue(null);
        cbPointArrival.setValue(null);
    }

    public void setCbDeparture(int point, String road){
        cbPointDeparture.setValue(Integer.toString(point));
        cbStreetDeparture.setValue(road);
    }

    public void setCbArrival(int point, String road){
        cbPointArrival.setValue(Integer.toString(point));
        cbStreetArrival.setValue(road);
    }

    public void resetPoints(Direction direction) {
        if (direction == Direction.DEPARTURE) {
            cbPointDeparture.getItems().clear();
        } else if (direction == Direction.ARRIVAL) {
            cbPointArrival.getItems().clear();
        }
    }

    public void resetCities() {
        cbCityDeparture.getItems().clear();
        cbCityArrival.getItems().clear();
    }

    public int getPointNumber(Direction direction) {
        if (cbPointDeparture.getSelectionModel().getSelectedItem() == null || cbPointArrival.getSelectionModel().getSelectedItem() == null) {
            return -1;
        } else if (direction == Direction.DEPARTURE) {
            return Integer.parseInt(cbPointDeparture.getSelectionModel().getSelectedItem());
        } else if (direction == Direction.ARRIVAL) {
            return Integer.parseInt(cbPointArrival.getSelectionModel().getSelectedItem());
        } else {
            return -1;
        }
    }

    public ComboBox<String> getCbCityDeparture() {
        return cbCityDeparture;
    }

    public ComboBox<String> getCbCityArrival() {
        return cbCityArrival;
    }

    public ComboBox<String> getCbStreetDeparture() {
        return cbStreetDeparture;
    }

    public ComboBox<String> getCbStreetArrival() {
        return cbStreetArrival;
    }

    public ComboBox<String> getCbPointDeparture() {
        return cbPointDeparture;
    }

    public ComboBox<String> getCbPointArrival() {
        return cbPointArrival;
    }

    public Button getGoButton() {
        return btnGo;
    }
}
