package com.ap4b.pathplanner.model;

import java.util.Vector;

/**
 * A simple class to represent a point in a two-dimensional space.
 */
public class Point {
    private int x;
    private int y;
    private Vector<String> infos;

    /**
     * Constructs and initializes a point at the specified (x,y) location.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate.
     *
     * @return the x coordinate of this point
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate.
     *
     * @return the y coordinate of this point
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the new y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    public void setInfos(Vector<String> infos) {
    	this.infos = infos;
    }

    public Vector<String> getInfos() {
    	return infos;
    }

    /**
     * Returns the distance between this point and the specified point.
     *
     * @param point the point to calculate the distance to
     */
    public double distance(Point point) {
        return Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
    }

    /**
     * Returns a string representation of this point and its location in the
     * (x,y) coordinate space.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}