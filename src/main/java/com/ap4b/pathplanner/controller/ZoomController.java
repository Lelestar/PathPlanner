package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.view.MenuFX;
import com.ap4b.pathplanner.view.ZoomControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class ZoomController implements EventHandler<ActionEvent> {
    private ZoomControl zoomControl;
    private Application app;

    public ZoomController(ZoomControl zoomControl, Application app) {
        this.zoomControl = zoomControl;
        this.app = app;
        attachHandlers();
    }
    @Override
    public void handle(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == zoomControl.getBtnZoomIn()){
            handleZoomIn();
        }
        if (source == zoomControl.getBtnZoomOut()){
            handleZoomOut();
        }
    }

    private void attachHandlers() {
        // Attach event handlers to each menu item
        zoomControl.getZoomSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                app.setZoomByValue(newValue.floatValue());
            }
        });
        zoomControl.getBtnZoomIn().setOnAction(this);
        zoomControl.getBtnZoomOut().setOnAction(this);
    }

    private void handleZoomIn(){
        app.modifyZoom(Application.ZOOM_NOTCH);
    }

    private void handleZoomOut(){
        app.modifyZoom(-Application.ZOOM_NOTCH);
    }
}
