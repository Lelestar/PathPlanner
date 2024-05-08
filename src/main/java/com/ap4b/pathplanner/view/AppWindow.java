package com.ap4b.pathplanner.view;

import com.ap4b.pathplanner.controller.MenuController;
import com.ap4b.pathplanner.model.Application;
import javafx.scene.layout.BorderPane;

/**
 * The Class AppWindow for JavaFX.
 */
public class AppWindow {
    private BorderPane content;
    private Application app;
    private MenuController menuController;

    public AppWindow(Application app) {
        this.app = app;
        initializeUI();
    }

    private void initializeUI() {
        content = new BorderPane();

        // Setup MenuFX
        MenuFX menuBar = new MenuFX();
        content.setTop(menuBar);
        menuController = new MenuController(menuBar);

        // Create map
        Map map = new Map(app.getMapLink());

        // Additional setup can be performed here, such as adding status bars, other panels, etc.
    }

    public BorderPane getContent() {
        return content;
    }
}