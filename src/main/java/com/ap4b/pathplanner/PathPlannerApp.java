package com.ap4b.pathplanner;

import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.view.AppWindow;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PathPlannerApp extends javafx.application.Application {

    private final static String DATA_PATH = "/com/ap4b/pathplanner/data/";
    private Application app;

    @Override
    public void init() {
        // Initialize the model
        app = new Application(DATA_PATH + "region_belfort_streets.xml");
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the main window (View)
        AppWindow appWindow = new AppWindow(app, primaryStage);
        Scene scene = new Scene(appWindow.getContent(), 1200, 700);
        primaryStage.setTitle("Path Planner");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
            launch(args);
    }
}