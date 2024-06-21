package com.ap4b.pathplanner.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.JarFile;

public class FileSelectionWindow {

    private static final String DATA_PATH = "/com/ap4b/pathplanner/data/";

    public static void display(OnFileSelectedListener listener) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select XML File");

        ListView<String> listView = new ListView<>();

        try {
            URL resource = FileSelectionWindow.class.getResource(DATA_PATH);
            if (resource != null) {
                if (resource.getProtocol().equals("jar")) {
                    // Running from a JAR file
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    try (JarFile jarFile = new JarFile(jarPath)) {
                        jarFile.stream()
                                .filter(e -> e.getName().startsWith("com/ap4b/pathplanner/data/") && e.getName().endsWith(".xml"))
                                .forEach(e -> listView.getItems().add(new File(e.getName()).getName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Button selectButton = new Button("Select");
        selectButton.setOnAction(e -> {
            String selectedFile = listView.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                String fullPath = DATA_PATH + selectedFile;
                listener.onFileSelected(fullPath);
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(listView, selectButton);

        Scene scene = new Scene(layout, 300, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    public interface OnFileSelectedListener {
        void onFileSelected(String filePath);
    }
}