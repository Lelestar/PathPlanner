package com.ap4b.pathplanner.model;

/**
 * Represents a state in an itinerary as part of a shortest path computation.
 * This typically includes a node and an associated cost or distance metric.
 */
public class ItineraryState {

    /** The node number (point in the road network). */
    public int node;

    /** The cost or distance from the start or another relevant metric. */
    public int cost;

    /**
     * Constructs a new ItineraryState with the specified node and cost.
     *
     * @param node the node number in the road network
     * @param cost the cost or distance associated with this node
     */
    public ItineraryState(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }
}