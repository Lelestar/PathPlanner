package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.model.Application;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class MapController {
    private Application app;
    private Pane map;

    public MapController(Application app, Pane map) {
        this.app = app;
        this.map = map;
        initialize();
    }

    private void initialize() {

        map.setOnMouseMoved(this::onMouseMoved);
        map.setOnMouseClicked(this::onMouseClicked);
    }

    private void onMouseMoved(MouseEvent event) {
        app.updateNearestPoint((int)event.getX(), (int)event.getY());
    }

    public void onMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { // Bouton droit
            app.setNearestPointAsArrival();
        }

        if (event.getButton() == MouseButton.PRIMARY) { // Bouton gauche
            app.setNearestPointAsStart();
        }
    }
}
