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

import javafx.scene.transform.Affine;
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

    private final float ZOOM_MIN = (float) 0.1;
    private final float ZOOM_MAX = (float) 1;

    private Point nearestPoint = null;
    private boolean itineraryUniquePointIsDeparturePoint = true;

    private double zoomFactor = 1.0;

    private final Color SCALE_COLOR = Color.BLACK;
    private final double SCALE_STROKE_WIDTH = 2.0;
    private final int SCALE_START_X = 15;  // Distance from the left border
    private final int SCALE_START_Y = 25;  // Distance from the top border
    private String scaleLabel = "";
    private int scaleSizePx = 0;

    /**
     * Constructs a Map instance with the specified image path.
     *
     * @param imgPath the path to the map image resource
     */
    public Map(String imgPath) {
        scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("scroll-pane");
        canvas = new Canvas();

        this.getChildren().add(scrollPane);

        loadImage(imgPath);

        scrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> draw());
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> draw());
    }

    /**
     * Loads an image from the specified path and sets it as the map image.
     *
     * @param imgPath the path to the map image resource
     */
    private void loadImage(String imgPath) {
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
        this.zoomFactor = zoomFactor;
        Scale newScale = new Scale(zoomFactor, zoomFactor);
        canvas.getTransforms().clear();
        canvas.getTransforms().add(newScale);
        draw();
    }

    /**
     * Draw the map and the itinerary.
     */
    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clear(gc);
        gc.drawImage(mapImage, 0, 0, canvas.getWidth(), canvas.getHeight());

        double scrollX = scrollPane.getHvalue() * (canvas.getWidth() - scrollPane.getViewportBounds().getWidth());
        double scrollY = scrollPane.getVvalue() * (canvas.getHeight() - scrollPane.getViewportBounds().getHeight());

        // Adjust the point size and itinerary width for the current zoom level
        double adjustedPointSize = POINT_SIZE / zoomFactor;
        double adjustedItineraryWidth = ITINERARY_WIDTH / zoomFactor;

        // Draw nearest point
        if (nearestPoint != null) {
            drawPoint(gc, nearestPoint, POINT_COLOR, adjustedPointSize);
            if (nearestPoint.getInfos() != null) {
                drawPointInfos(gc, nearestPoint, scrollX, scrollY);
            }
        }

        // Draw itinerary
        if (itinerary.size() > 1) {
            gc.setStroke(ITINERARY_COLOR);
            gc.setLineWidth(adjustedItineraryWidth);

            Point prev = null;
            for (Point pt : itinerary) {
                if (prev != null) {
                    gc.strokeLine(prev.getX(), prev.getY(), pt.getX(), pt.getY());
                }
                prev = pt;
            }

            // Draw start and end points
            drawPoint(gc, itinerary.firstElement(), START_COLOR, adjustedPointSize);
            drawPoint(gc, itinerary.lastElement(), END_COLOR, adjustedPointSize);
        }
        else if (itinerary.size() == 1) {
            if (itineraryUniquePointIsDeparturePoint) {
                drawPoint(gc, itinerary.firstElement(), START_COLOR, adjustedPointSize);
            }
            else {
                drawPoint(gc, itinerary.firstElement(), END_COLOR, adjustedPointSize);
            }
        }

        // Draw scale with fixed size
        drawScale(gc, scrollX, scrollY, zoomFactor);
    }

    /**
     * Draw a point with a label.
     *
     * @param gc the GraphicsContext
     * @param point the point location
     * @param color the color of the point
     */
    private void drawPoint(GraphicsContext gc, Point point, Color color, double adjustedPointSize) {
        gc.setFill(color);
        gc.fillOval(point.getX() - adjustedPointSize / 2, point.getY() - adjustedPointSize / 2, adjustedPointSize, adjustedPointSize);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
    }

    private void drawPointInfos(GraphicsContext gc, Point point, double scrollX, double scrollY) {
        int lineHeight = 15;
        int padding = 10;
        int infoCount = point.getInfos().size();
        int rectHeight = padding * 2 + lineHeight * infoCount;

        double x = point.getX() * zoomFactor;
        double y = point.getY() * zoomFactor;

        // Save the current state of the graphics context
        gc.save();

        // Disable zoom effect for the rectangle
        gc.setTransform(1, 0, 0, 1, 0, 0);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.setFill(Color.WHITE);
        gc.fillRect(x / zoomFactor, (y - rectHeight) / zoomFactor, 200 / zoomFactor, rectHeight / zoomFactor);
        gc.strokeRect(x / zoomFactor, (y - rectHeight) / zoomFactor, 200 / zoomFactor, rectHeight / zoomFactor);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Inter Regular", FontWeight.NORMAL, 10 / zoomFactor));
        for (int i = 0; i < infoCount; i++) {
            gc.fillText(point.getInfos().elementAt(i), (x + padding) / zoomFactor, (y - rectHeight + padding + lineHeight * (i + 1)) / zoomFactor);
        }

        // Restore the previous state of the graphics context
        gc.restore();
    }

    /**
     * Draw the scale in the top left corner.
     *
     * @param gc the GraphicsContext
     */
    private void drawScale(GraphicsContext gc, double scrollX, double scrollY, double zoomFactor) {
        gc.save(); // Save the current state

        // Get the visible area of the canvas
        double visibleX = (scrollPane.getHvalue() * (canvas.getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor);
        double visibleY = (scrollPane.getVvalue() * (canvas.getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor);

        // Set the start position of the scale
        double startX = (int) visibleX + SCALE_START_X;
        double startY = (int) visibleY + SCALE_START_Y;

        // Draw main line
        gc.setStroke(SCALE_COLOR);
        gc.setLineWidth(SCALE_STROKE_WIDTH / zoomFactor);
        gc.strokeLine(startX, startY, startX + scaleSizePx / zoomFactor, startY);

        // Draw vertical lines
        gc.strokeLine(startX, startY - 2 / zoomFactor, startX, startY + 2 / zoomFactor);
        gc.strokeLine(startX + scaleSizePx / (2 * zoomFactor), startY - 2 / zoomFactor, startX + scaleSizePx / (2 * zoomFactor), startY + 2 / zoomFactor);
        gc.strokeLine(startX + scaleSizePx / zoomFactor, startY - 2 / zoomFactor, startX + scaleSizePx / zoomFactor, startY + 2 / zoomFactor);


        // Draw scale text
        gc.setFill(SCALE_COLOR);
        gc.setFont(Font.font("Inter Regular", FontWeight.BOLD, 10 / zoomFactor));
        gc.fillText(scaleLabel, startX, startY - 5 / zoomFactor);

        gc.restore(); // Restore the saved state
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

    public Canvas getCanvas() {
        return canvas;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setNearestPoint(Point nearestPoint) {
        this.nearestPoint = nearestPoint;
    }

    public Point getNearestPoint() {
        return nearestPoint;
    }

    public void addPoint(Point p) {itinerary.add(p);}

    public void setTypePointUnique(boolean isDeparture) {
        itineraryUniquePointIsDeparturePoint = isDeparture;
    }

    public void deleteFirst(){
        if (!itinerary.isEmpty()) {
            itinerary.remove(0);
        }

    }

    public void deleteLast(){
        if (!itinerary.isEmpty()) {
            itinerary.remove(itinerary.size() - 1);
        }
    }

    public void resetItinerary() {
        itinerary.clear();
        draw();
    }

    public void setScaleLabel(String scale) {
        scaleLabel = scale;
    }

    public void setScaleSize(int scaleSize) {
        scaleSizePx = scaleSize;
    }

    /**
     * Updates the map with a new image.
     *
     * @param imgPath the path to the new map image resource
     */
    public void updateMap(String imgPath) {
        loadImage(imgPath);
        resetItinerary();
        draw();
    }

    public void recenterViewOnItinerary() {
        if (itinerary.isEmpty()) {
            return;
        }

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        // Calculate the bounding box of the itinerary
        for (Point point : itinerary) {
            if (point.getX() < minX) {
                minX = point.getX();
            }
            if (point.getY() < minY) {
                minY = point.getY();
            }
            if (point.getX() > maxX) {
                maxX = point.getX();
            }
            if (point.getY() > maxY) {
                maxY = point.getY();
            }
        }

        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;

        double itineraryWidth = maxX - minX;
        double itineraryHeight = maxY - minY;

        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        // Add some margin around the itinerary
        double margin = 20; // 20 pixels margin
        itineraryWidth += margin * 2;
        itineraryHeight += margin * 2;

        // Calculate the optimal zoom factor to fit the entire itinerary in the viewport
        double zoomFactorWidth = viewportWidth / itineraryWidth;
        double zoomFactorHeight = viewportHeight / itineraryHeight;
        double optimalZoomFactor = Math.min(zoomFactorWidth, zoomFactorHeight);

        // Ensure the new zoom factor is within allowed bounds
        optimalZoomFactor = Math.max(ZOOM_MIN, Math.min(optimalZoomFactor, ZOOM_MAX));

        // Apply the new zoom factor
        updateZoom(optimalZoomFactor);

        // Calculate the new center coordinates with the new zoom factor
        double newCenterX = centerX * optimalZoomFactor;
        double newCenterY = centerY * optimalZoomFactor;

        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        double newHValue = (newCenterX - viewportWidth / 2) / (canvasWidth - viewportWidth);
        double newVValue = (newCenterY - viewportHeight / 2) / (canvasHeight - viewportHeight);

        // Clamp the values to be between 0 and 1
        newHValue = Math.max(0, Math.min(newHValue, 1));
        newVValue = Math.max(0, Math.min(newVValue, 1));

        // Apply the new scroll values
        scrollPane.setHvalue(newHValue);
        scrollPane.setVvalue(newVValue);
    }
}

