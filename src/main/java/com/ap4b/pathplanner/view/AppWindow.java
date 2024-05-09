package com.ap4b.pathplanner.view;

import com.ap4b.pathplanner.controller.MenuController;
import com.ap4b.pathplanner.model.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * The Class AppWindow for JavaFX.
 */
public class AppWindow {
    private BorderPane content;
    private Application app;
    private Stage primaryStage;
    private MenuController menuController;

    private final int INFO_PANEL_WIDTH = 250;
    private final int SPACE_BETWEEN_MAP_AND_PANEL = 35;

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
        menuController = new MenuController(menuBar);

        // Create Map
        Map map = new Map(app.getMapLink());
        ZoomControl zoomControl = new ZoomControl();
        BorderPane.setAlignment(zoomControl, Pos.BOTTOM_RIGHT);

        // Panel for Map and ZoomControl
        BorderPane mapContainer = new BorderPane();
        mapContainer.setCenter(map);
        mapContainer.setBottom(zoomControl);
        content.setCenter(mapContainer);

        // Create PanelInformations
        PanelInformations panelInformations = new PanelInformations(INFO_PANEL_WIDTH, primaryStage.getHeight(), "metric");
        BorderPane.setMargin(panelInformations, new javafx.geometry.Insets(0, 0, 0, 10));
        content.setRight(panelInformations);

        // Bind the size of the map and panelInformations to the size of the primaryStage
        map.getScrollPane().prefWidthProperty().bind(primaryStage.widthProperty().subtract(INFO_PANEL_WIDTH + SPACE_BETWEEN_MAP_AND_PANEL));
        map.getScrollPane().prefHeightProperty().bind(primaryStage.heightProperty().subtract(63));
        panelInformations.prefHeightProperty().bind(primaryStage.heightProperty());
    }

    /**
     * Get the main content pane of this window.
     *
     * @return the main content pane of this application window.
     */
    public BorderPane getContent() {
        return content;
    }
}