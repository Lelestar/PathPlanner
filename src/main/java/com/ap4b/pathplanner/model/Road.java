package com.ap4b.pathplanner.model;


import java.util.Vector;

/**
 * Represents a road consisting of a series of connected points.
 */
public class Road {

    /** Direction of the road, can denote one-way or two-way. */
    private int direction;

    /** List of point identifiers that make up the road. */
    private Vector<Integer> points;

    /**
     * Constructs a new Road.
     *
     * @param direction the direction of the road (e.g., 0 for two-way, 1 for one-way)
     */
    public Road(int direction) {
        this.points = new Vector<>();
        this.direction = direction;
    }

    /**
     * Adds a point identifier to the road.
     *
     * @param pointId the point identifier to add
     */
    public void addPoint(Integer pointId) {
        points.add(pointId);
    }

    /**
     * Retrieves a point identifier at the specified position in the road.
     *
     * @param position the position of the point in the vector
     * @return the point identifier at the specified position
     */
    public Integer getPoint(int position) {
        return points.elementAt(position);
    }

    /**
     * Returns the number of points that make up the road.
     *
     * @return the number of points in the road
     */
    public int getNumberOfPoints() {
        return points.size();
    }

    /**
     * Returns the list of all point identifiers making up the road.
     *
     * @return a vector of point identifiers
     */
    public Vector<Integer> getPoints() {
        return points;
    }

    /**
     * Returns the direction of the road.
     *
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }
}