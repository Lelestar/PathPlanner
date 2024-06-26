package com.ap4b.pathplanner.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class AboutWindow {

    public AboutWindow(String cssPath) {
        Stage window = new Stage();
        window.setTitle("About");

        // Setup the content of the window
        VBox root = new VBox(10);  // Vertical spacing between nodes
        root.getStyleClass().add("root-background");
        root.setAlignment(Pos.CENTER);

        // Create a label and add it to the root pane
        Label lblInfo = new Label("This project was created by:\n");
        lblInfo.getStyleClass().add("app-label");
        Label lblInfo2 = new Label("- Leonard ZIPPER\n- Noemie L'EVEQUE\n- Thibault FOSSE\n\nStudents at the University of Technology of Belfort-Montbeliard.\n(Spring 2024)");
        lblInfo2.getStyleClass().add("app-text");
        root.getChildren().addAll(lblInfo, lblInfo2);

        // Setup the Scene
        Scene scene = new Scene(root, 400, 200);
        scene.getStylesheets().add(Objects.requireNonNull(AboutWindow.class.getResource(cssPath)).toExternalForm());
        window.getIcons().add(new Image(Objects.requireNonNull(FileSelectionWindow.class.getResourceAsStream("/com/ap4b/pathplanner/icon.png"))));
        window.setScene(scene);

        // Position the window in the center of the screen
        window.centerOnScreen();

        // Make this window block input to other windows
        window.initModality(Modality.APPLICATION_MODAL);

        // Set default close operation
        window.setOnCloseRequest(event -> window.close());
        // Show the window
        window.show();
    }
}

