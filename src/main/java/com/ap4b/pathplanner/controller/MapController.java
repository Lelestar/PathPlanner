package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.view.AppWindow;
import com.ap4b.pathplanner.view.Map;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class MapController {
    private Application app;
    private Map map;
    private AppWindow appWindow;


    public MapController(Application app, Map map, AppWindow appWindow) {
        this.app = app;
        this.map = map;
        this.appWindow = appWindow;
        initialize();
    }

    private void initialize() {
        map.setOnMouseMoved(this::onMouseMoved);
        map.setOnMouseClicked(this::onMouseClicked);
    }

    private void onMouseMoved(MouseEvent event) {
        double scrollX = map.getScrollPane().getHvalue() * (map.getCanvas().getWidth() - map.getScrollPane().getViewportBounds().getWidth());
        double scrollY = map.getScrollPane().getVvalue() * (map.getCanvas().getHeight() - map.getScrollPane().getViewportBounds().getHeight());
        double zoomFactor = map.getZoomFactor();

        double adjustedX = (event.getX() + scrollX) / zoomFactor;
        double adjustedY = (event.getY() + scrollY) / zoomFactor;


        app.updateNearestPoint((int)adjustedX, (int)adjustedY);
    }

    public void onMouseClicked(MouseEvent event) {
        Bounds zoomControlBounds = appWindow.getZoomControlBoundsInScene();
        if (zoomControlBounds.contains(event.getX(), event.getY())) {
            // Ignore clicks on the zoom control
            return;
        }

        if (event.getButton() == MouseButton.SECONDARY) { // Right button
            app.setNearestPointAsArrival();
        }

        if (event.getButton() == MouseButton.PRIMARY) { // Left button
            app.setNearestPointAsStart();
        }
    }
}
