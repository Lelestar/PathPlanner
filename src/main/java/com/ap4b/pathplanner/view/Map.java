package com.ap4b.pathplanner.view;

import com.ap4b.pathplanner.model.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.net.URI;
import java.net.URL;
import java.util.Vector;
import javafx.geometry.Point2D;
import javafx.scene.transform.Scale;

/**
 * The Class Map in JavaFX.
 */
public class Map extends Pane {

    private Image mapImage;
    private Canvas canvas;
    private ScrollPane scrollPane;
    private Vector<Point> itinerary = new Vector<>();

    private final Color START_COLOR = Color.GREEN;
    private final Color END_COLOR = Color.RED;
    private final Color ITINERARY_COLOR = Color.BLUE;
    private final Color POINT_COLOR = Color.BLACK;

    private final int ITINERARY_WIDTH = 5;
    private final int POINT_SIZE = 15;

    private Point nearestPoint = null;

    /**
     * Constructs a Map instance with the specified image path.
     *
     * @param imgPath the path to the map image resource
     */
    public Map(String imgPath) {
        scrollPane = new ScrollPane();
        canvas = new Canvas();

        try {
            URL resource = getClass().getResource(imgPath);
            if (resource == null) {
                throw new RuntimeException("Resource URL not found for: " + imgPath);
            }
            URI ressourceURI = resource.toURI();
            mapImage = new Image(ressourceURI.toString(), true);

            mapImage.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() == 1.0 && !mapImage.isError()) {
                    canvas.setWidth(mapImage.getWidth());
                    canvas.setHeight(mapImage.getHeight());
                    scrollPane.setContent(canvas);
                    scrollPane.setPannable(true);
                    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    draw();
                    this.getChildren().add(scrollPane);
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
     * @param zoomFactor the zoom factor
     */
    public void updateZoom(double zoomFactor) {
        Scale newScale = new Scale(zoomFactor, zoomFactor);
        canvas.getTransforms().clear();
        canvas.getTransforms().add(newScale);
    }

    /**
     * Draw the map and the itinerary.
     */
    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clear(gc);
        gc.drawImage(mapImage, 0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw nearest point
        if (nearestPoint != null) {
            drawPoint(gc, nearestPoint, POINT_COLOR);
            if (nearestPoint.getInfos() != null) {
                drawPointInfos(gc, nearestPoint);
            }
        }

        // Draw itinerary
        if (itinerary.size() > 1) {
            gc.setStroke(ITINERARY_COLOR);
            gc.setLineWidth(ITINERARY_WIDTH);

            Point prev = null;
            for (Point pt : itinerary) {
                if (prev != null) {
                    gc.strokeLine(prev.getX(), prev.getY(), pt.getX(), pt.getY());
                }
                prev = pt;
            }

            // Draw start and end points
            drawPoint(gc, itinerary.firstElement(), START_COLOR);
            drawPoint(gc, itinerary.lastElement(), END_COLOR);
        }
    }

    /**
     * Draw a point with a label.
     *
     * @param gc the GraphicsContext
     * @param point the point location
     * @param color the color of the point
     */
    private void drawPoint(GraphicsContext gc, Point point, Color color) {
        gc.setFill(color);
        gc.fillOval(point.getX() - POINT_SIZE / 2, point.getY() - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
    }

    private void drawPointInfos(GraphicsContext gc, Point point) {
        int lineHeight = 15;
        int padding = 10;
        int infoCount = point.getInfos().size();
        int rectHeight = padding * 2 + lineHeight * infoCount;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.setFill(Color.WHITE);
        gc.fillRect(point.getX(), point.getY() - rectHeight, 200, rectHeight);
        gc.strokeRect(point.getX(), point.getY() - rectHeight, 200, rectHeight);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        for (int i = 0; i < infoCount; i++) {
            gc.fillText(point.getInfos().elementAt(i), point.getX() + padding, point.getY() - rectHeight + padding + lineHeight * (i + 1));
        }
    }

    /**
     * Clear the canvas.
     *
     * @param gc the GraphicsContext
     */
    private void clear(GraphicsContext gc) {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setNearestPoint(Point nearestPoint) {
        this.nearestPoint = nearestPoint;
    }

    public Point getNearestPoint() {
        return nearestPoint;
    }

    public void addPoint(Point p) {itinerary.add(p);}
}

