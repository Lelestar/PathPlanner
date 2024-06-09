package com.ap4b.pathplanner.view;

import com.ap4b.pathplanner.model.Application;
import javafx.event.Event;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;

/**
 * Zoom controls positioned at the bottom-left of the map.
 */
public class ZoomControl extends HBox {

    private Button btnZoomIn, btnZoomOut;

    public Slider getZoomSlider() {
        return zoomSlider;
    }

    public Button getBtnZoomOut() {
        return btnZoomOut;
    }

    public Button getBtnZoomIn() {
        return btnZoomIn;
    }

    private Slider zoomSlider;

    public ZoomControl() {
        setPadding(new Insets(10, 0, 40, 10));
        setAlignment(Pos.CENTER); // Ensures that controls are aligned to the center
        setSpacing(8);
        setMaxWidth(300);

        btnZoomIn = new Button("+");
        btnZoomOut = new Button("-");
        btnZoomIn.getStyleClass().add("zoom-button");
        btnZoomOut.getStyleClass().add("zoom-button");
        btnZoomIn.setMinSize(20, 20);
        btnZoomOut.setMinSize(20, 20);

        zoomSlider = new Slider(0.1, 1, 0.5);
        zoomSlider.setMajorTickUnit(Application.ZOOM_NOTCH);
        zoomSlider.setMinorTickCount(0);
        zoomSlider.setSnapToTicks(true);

        getChildren().addAll(btnZoomOut, zoomSlider, btnZoomIn);
    }

    public void setZoomSlider(float value){
        zoomSlider.adjustValue(value);
    }
}
