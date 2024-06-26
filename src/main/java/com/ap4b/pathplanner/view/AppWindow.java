package com.ap4b.pathplanner.view;

import com.ap4b.pathplanner.controller.*;
import com.ap4b.pathplanner.model.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Class AppWindow for JavaFX.
 */
public class AppWindow {
    private BorderPane content;
    private Application app;
    private Stage primaryStage;
    private DepartureArrivalPanel departureArrivalPanel;
    private MenuController menuController;
    private DepartureArrivalPanelController departureArrivalPanelController;
    private Map map;
    private ZoomControl zoomControl;
    private MapController mapController;
    private ZoomController zoomController;
    private PanelInformations panelInformations;

    private final int INFO_PANEL_WIDTH = 250;
    private final int SPACE_BETWEEN_MAP_AND_PANEL = 41;

    public AppWindow(Application app, Stage primaryStage) {
        this.app = app;
        this.primaryStage = primaryStage;
        initializeUI();
    }

    private void initializeUI() {
        content = new BorderPane();

        // Create Menu
        MenuFX menuBar = new MenuFX();
        content.setTop(menuBar);
        menuController = new MenuController(menuBar, app, this);

        // Create Map
        map = new Map(app.getMapLink());
        zoomControl = new ZoomControl();
        BorderPane.setAlignment(zoomControl, Pos.BOTTOM_RIGHT);
        mapController = new MapController(app, map, this);
        zoomController = new ZoomController(zoomControl, app);

        // Panel for Map and ZoomControl
        BorderPane mapContainer = new BorderPane();
        mapContainer.setCenter(map);
        mapContainer.setBottom(zoomControl);
        content.setCenter(mapContainer);

        // Create DepartureArrivalPanel and PanelInformations
        VBox rightPanel = new VBox();
        rightPanel.getStyleClass().add("right-panel");
        panelInformations = new PanelInformations(INFO_PANEL_WIDTH, primaryStage.getHeight(), "Lambert-II");
        departureArrivalPanel = new DepartureArrivalPanel();
        VBox.setMargin(departureArrivalPanel, new javafx.geometry.Insets(0, 0, 0, 10));
        departureArrivalPanelController = new DepartureArrivalPanelController(app, departureArrivalPanel);

        // Add both panels to the VBox
        rightPanel.getChildren().addAll(departureArrivalPanel, panelInformations);
        VBox.setVgrow(departureArrivalPanel, Priority.NEVER);
        VBox.setVgrow(panelInformations, Priority.ALWAYS);
        content.setRight(rightPanel);

        // Bind the size of the map and panelInformations to the size of the primaryStage
        map.getScrollPane().prefWidthProperty().bind(primaryStage.widthProperty().subtract(INFO_PANEL_WIDTH + SPACE_BETWEEN_MAP_AND_PANEL));
        map.getScrollPane().prefHeightProperty().bind(primaryStage.heightProperty().subtract(64));
    }

    /**
     * Get the main content pane of this window.
     *
     * @return the main content pane of this application window.
     */
    public BorderPane getContent() {
        return content;
    }

    public DepartureArrivalPanel getDepartureArrivalPanel() {
        return departureArrivalPanel;
    }

    public Map getMap() {
        return map;
    }

    public ZoomControl getZoomControl() { return zoomControl; }

    public Bounds getZoomControlBoundsInScene() {
        return zoomControl.localToScene(zoomControl.getBoundsInLocal());
    }

    public PanelInformations getPanelInformations() { return panelInformations; }

    public void resetMessage(){

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}