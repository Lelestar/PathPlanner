package com.ap4b.pathplanner.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;
import java.util.Objects;
import java.util.Vector;
import javafx.geometry.Point2D;

/**
 * The Class Map in JavaFX.
 */
public class Map extends Canvas {

    private Image mapImage;
    private Vector<Point2D> itinerary = new Vector<>();

    private final Color START_COLOR = Color.GREEN;
    private final Color END_COLOR = Color.RED;
    private final Color ITINERARY_COLOR = Color.BLUE;
    private final Color POINT_COLOR = Color.BLACK;

    private final int ITINERARY_WIDTH = 5;
    private final int POINT_SIZE = 20;

    /**
     * Instantiates a new map.
     *
     * @param imgPath the image path
     */
    public Map(String imgPath) {
        super();
        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(imgPath), "Resource not found: " + imgPath);
            mapImage = new Image(is.toString(), true);  // true = background loading
            mapImage.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() == 1.0) {
                    setWidth(mapImage.getWidth());
                    setHeight(mapImage.getHeight());
                    draw();  // Only draw when image is fully loaded
                }
            });
            mapImage.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    throw new RuntimeException("Failed to load map image: " + newValue.getMessage(), newValue);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize map.", e);
        }
    }


    /**
     * Update zoom.
     *
     * @param zoom the zoom level
     */
    public void updateZoom(double zoom) {
        setScaleX(zoom);
        setScaleY(zoom);
    }

    /**
     * Draw the map and the itinerary.
     */
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        clear(gc);

        // Draw the map image
        gc.drawImage(mapImage, 0, 0);

        // Draw itinerary
        if (itinerary.size() > 1) {
            gc.setStroke(ITINERARY_COLOR);
            gc.setLineWidth(ITINERARY_WIDTH);

            Point2D prev = null;
            for (Point2D pt : itinerary) {
                if (prev != null) {
                    gc.strokeLine(prev.getX(), prev.getY(), pt.getX(), pt.getY());
                }
                prev = pt;
            }

            // Draw start and end points
            drawPoint(gc, itinerary.firstElement(), START_COLOR, "S");
            drawPoint(gc, itinerary.lastElement(), END_COLOR, "E");
        }
    }

    /**
     * Draw a point with a label.
     *
     * @param gc the GraphicsContext
     * @param point the point location
     * @param color the color of the point
     * @param label the label of the point
     */
    private void drawPoint(GraphicsContext gc, Point2D point, Color color, String label) {
        gc.setFill(color);
        gc.fillOval(point.getX() - POINT_SIZE / 2, point.getY() - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.fillText(label, point.getX(), point.getY());
    }

    /**
     * Clear the canvas.
     *
     * @param gc the GraphicsContext
     */
    private void clear(GraphicsContext gc) {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}

