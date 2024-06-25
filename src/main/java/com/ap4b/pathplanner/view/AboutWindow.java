package com.ap4b.pathplanner.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutWindow extends Stage {

    public AboutWindow() {
        setTitle("About");

        // Setup the content of the window
        VBox root = new VBox(10);  // Vertical spacing between nodes
        root.setAlignment(Pos.CENTER);

        // Create a label and add it to the root pane
        Label lblInfo = new Label("Coucou, nous sommes étudiants en Informatique et nous avons réalisé cette application !");
        root.getChildren().add(lblInfo);

        // Setup the Scene
        Scene scene = new Scene(root, 500, 400);
        setScene(scene);

        // Position the window in the center of the screen
        centerOnScreen();

        // Make this window block input to other windows
        initModality(Modality.APPLICATION_MODAL);

        // Set default close operation
        setOnCloseRequest(event -> this.close());

        // Show the window
        show();
    }
}

