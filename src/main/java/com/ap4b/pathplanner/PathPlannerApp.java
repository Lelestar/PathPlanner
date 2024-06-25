package com.ap4b.pathplanner;

import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.view.AppWindow;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class PathPlannerApp extends javafx.application.Application {

    private final static String DATA_PATH = "/com/ap4b/pathplanner/data/";
    private Application app;
    private AppWindow appWindow;

    @Override
    public void init() {
        // Initialize the model
        app = new Application(DATA_PATH + "region_belfort_streets.xml");
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the main window (View)
        appWindow = new AppWindow(app, primaryStage);
        app.setAppWindow(appWindow); // Set the AppWindow in the model
        Scene scene = new Scene(appWindow.getContent(), 1200, 700);

        // Load the CSS file and the font
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ap4b/pathplanner/style_light.css")).toExternalForm());
        Font.loadFont(getClass().getResourceAsStream("/com/ap4b/pathplanner/fonts/Inter-Regular.otf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/ap4b/pathplanner/fonts/Inter-SemiBold.otf"), 14);

        primaryStage.setTitle("Path Planner");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/ap4b/pathplanner/icon.png"))));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
            launch(args);
    }
}