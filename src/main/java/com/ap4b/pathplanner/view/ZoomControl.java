package com.ap4b.pathplanner.view;

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Zoom controls positioned at the bottom-left of the map.
 */
public class ZoomControl extends HBox {

    private Button btnZoomIn, btnZoomOut;
    private Slider zoomSlider;

    public ZoomControl() {
        setPadding(new Insets(10, 0, 40, 10));
        setAlignment(Pos.CENTER); // Ensures that controls are aligned to the center
        setSpacing(10);
        setMaxWidth(300);

        btnZoomIn = new Button("+");
        btnZoomOut = new Button("-");
        btnZoomIn.setMinSize(20, 20);
        btnZoomOut.setMinSize(20, 20);

        zoomSlider = new Slider(0, 100, 50);

        getChildren().addAll(btnZoomIn, zoomSlider, btnZoomOut);
    }
}
