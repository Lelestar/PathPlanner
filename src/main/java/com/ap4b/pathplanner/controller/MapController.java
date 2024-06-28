package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.model.Point;
import com.ap4b.pathplanner.view.AppWindow;
import com.ap4b.pathplanner.view.Map;
import javafx.geometry.Bounds;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class MapController {
    private Application app;
    private Map map;
    private AppWindow appWindow;
    private Vector<Point> tempStreetPoints = new Vector<>();

    public MapController(Application app, Map map, AppWindow appWindow) {
        this.app = app;
        this.map = map;
        this.appWindow = appWindow;
        initialize();
    }

    private void initialize() {
        map.setOnMouseMoved(this::onMouseMoved);
        map.setOnMouseClicked(this::onMouseClicked);
    }

    private void onMouseMoved(MouseEvent event) {
        double scrollX = map.getScrollPane().getHvalue() * (map.getCanvas().getWidth() - map.getScrollPane().getViewportBounds().getWidth());
        double scrollY = map.getScrollPane().getVvalue() * (map.getCanvas().getHeight() - map.getScrollPane().getViewportBounds().getHeight());
        double zoomFactor = map.getZoomFactor();

        double adjustedX = (event.getX() + scrollX) / zoomFactor;
        double adjustedY = (event.getY() + scrollY) / zoomFactor;

        Bounds zoomControlBounds = appWindow.getZoomControlBoundsInScene();

        if (!zoomControlBounds.contains(event.getX(), event.getY())) {
            // Ignore mouse events on the zoom control
            app.updateNearestPoint((int)adjustedX, (int)adjustedY);
        }
    }

    private void onMouseClicked(MouseEvent event) {
        Bounds zoomControlBounds = appWindow.getZoomControlBoundsInScene();
        if (zoomControlBounds.contains(event.getX(), event.getY())) {
            // Ignore clicks on the zoom control
            return;
        }

        double scrollX = map.getScrollPane().getHvalue() * (map.getCanvas().getWidth() - map.getScrollPane().getViewportBounds().getWidth());
        double scrollY = map.getScrollPane().getVvalue() * (map.getCanvas().getHeight() - map.getScrollPane().getViewportBounds().getHeight());
        double zoomFactor = map.getZoomFactor();

        double adjustedX = (event.getX() + scrollX) / zoomFactor;
        double adjustedY = (event.getY() + scrollY) / zoomFactor;

        if (app.isModeIsEdition()) {
            if (event.isShiftDown()) {
                Point clickedPoint = new Point((int) adjustedX, (int) adjustedY);
                Point nearestPoint = findNearestPoint(clickedPoint, map.getRoadGraph(), map.getNewPoints(), 50.0);
                if (nearestPoint != null) {
                    // Shift + click on an existing point -> remove point
                    map.removePoint(nearestPoint);
                } else {
                    map.addNewPoint(clickedPoint);
                }
                map.draw();
            } else if (event.isAltDown()) {
                if (map.getNearestPoint() != null) {
                    // Alt + click on existing point -> add to temporary street points
                    Point selectedPoint = map.getNearestPoint();
                    if(!tempStreetPoints.contains(selectedPoint)){
                        tempStreetPoints.add(selectedPoint);
                        map.addNewPoint(selectedPoint);
                    }

                    // If we have at least two points, prompt for street name
                    if (tempStreetPoints.size() >= 2) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Street Name");
                        dialog.setHeaderText("Enter the name of the street:");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(streetName -> {
                            map.addStreet(tempStreetPoints);
                            tempStreetPoints.clear();
                        });
                    }
                }
            }
        } else {
            if (event.getButton() == MouseButton.SECONDARY) { // Right button
                app.setNearestPointAsArrival();
            }

            if (event.getButton() == MouseButton.PRIMARY) { // Left button
                app.setNearestPointAsStart();
            }
        }
    }

    /**
     * Finds the nearest point to the specified point within the given tolerance.
     * Searches both the roadGraph and newPoints collections.
     *
     * @param targetPoint The point to search around.
     * @param roadGraph The collection of points in the road graph.
     * @param newPoints The collection of new points.
     * @param tolerance The distance tolerance to consider a point as "near".
     * @return The nearest point if found within the tolerance, otherwise null.
     */
    private Point findNearestPoint(Point targetPoint, Vector<Point> roadGraph, Vector<Point> newPoints, double tolerance) {
        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Point p : roadGraph) {
            double distance = targetPoint.distance(p);
            if (distance < minDistance && distance <= tolerance) {
                minDistance = distance;
                nearestPoint = p;
            }
        }

        for (Point p : newPoints) {
            double distance = targetPoint.distance(p);
            if (distance < minDistance && distance <= tolerance) {
                minDistance = distance;
                nearestPoint = p;
            }
        }

        return nearestPoint;
    }
}
