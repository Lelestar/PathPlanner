package com.ap4b.pathplanner.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class ControlsWindow {

    public ControlsWindow(String cssPath) {
        Stage window = new Stage();
        window.setTitle("Controls");

        // Setup the content of the window
        VBox root = new VBox(10);  // Vertical spacing between nodes
        root.getStyleClass().add("root-background");
        root.setAlignment(Pos.CENTER);

        // Create labels and add them to the root pane
        Label lblUsageTitle = new Label("In Usage Mode:");
        lblUsageTitle.getStyleClass().add("app-label");
        Label lblUsageControls = new Label(
                "- Left click to place a starting point.\n" +
                        "- Right click to place an ending point.\n" +
                        "- Middle mouse button + move to drag the map."
        );
        lblUsageControls.getStyleClass().add("app-text");

        Label lblEditTitle = new Label("In Edit Mode:");
        lblEditTitle.getStyleClass().add("app-label");
        Label lblEditControls = new Label(
                "- Once in edit mode, you can freely add new points or remove them with CTRL + click.\n" +
                        "- Similarly, to add new streets, use ALT + click in two places to create two points of a street."
        );
        lblEditControls.getStyleClass().add("app-text");

        root.getChildren().addAll(lblUsageTitle, lblUsageControls, lblEditTitle, lblEditControls);

        // Setup the Scene
        Scene scene = new Scene(root, 600, 250);
        scene.getStylesheets().add(Objects.requireNonNull(ControlsWindow.class.getResource(cssPath)).toExternalForm());
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
