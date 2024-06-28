package com.ap4b.pathplanner.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileSelectionWindow {

    private static final String DATA_PATH = "/com/ap4b/pathplanner/data/";

    public static void display(OnFileSelectedListener listener, String cssPath) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select XML File");

        ListView<String> listView = new ListView<>();
        listView.getStyleClass().add("list-view-2");

        try {
            URL resource = FileSelectionWindow.class.getResource(DATA_PATH);
            if (resource != null) {
                if (resource.getProtocol().equals("jar")) {
                    // Running from a JAR file
                    JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
                    try (JarFile jarFile = jarConnection.getJarFile()) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith("com/ap4b/pathplanner/data/") && entryName.endsWith(".xml")) {
                                listView.getItems().add(entryName.substring(entryName.lastIndexOf("/") + 1));
                            }
                        }
                    }
                } else {
                    // Running from a directory
                    File folder = new File(resource.toURI());
                    File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
                    if (listOfFiles != null) {
                        for (File file : listOfFiles) {
                            listView.getItems().add(file.getName());
                        }
                    }
                }
            } else {
                System.out.println("Resource URL not found for: " + DATA_PATH);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        Button selectButton = new Button("Select");
        selectButton.getStyleClass().add("app-button");
        selectButton.setMaxWidth(Float.MAX_VALUE);
        selectButton.setOnAction(e -> {
            String selectedFile = listView.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                String fullPath = DATA_PATH + selectedFile;
                listener.onFileSelected(fullPath);
                window.close();
            }
        });

        VBox layout = new VBox(8);
        layout.getStyleClass().add("root-background");
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(8));
        layout.getChildren().addAll(listView, selectButton);

        Scene scene = new Scene(layout, 360, 400);
        scene.getStylesheets().add(Objects.requireNonNull(FileSelectionWindow.class.getResource(cssPath)).toExternalForm());
        window.setScene(scene);
        window.getIcons().add(new Image(Objects.requireNonNull(FileSelectionWindow.class.getResourceAsStream("/com/ap4b/pathplanner/icon.png"))));
        window.setResizable(false);
        window.showAndWait();
    }

    public interface OnFileSelectedListener {
        void onFileSelected(String filePath);
    }
}