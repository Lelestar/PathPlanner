package com.ap4b.pathplanner.model;

import java.util.Iterator;
import java.util.Vector;

class Node {
    int x, y;
    int delta_plus; // edge starts from this node
    int delta_minus; // edge terminates at this node
    int dist; // distance from the start node
    int prev; // previous node of the shortest path
    int succ, pred; // node in Sbar with finite dist.
    int w, h, pw, dx, dy;
    String name;
    int id;
}

class Edge {
    int rndd_plus; // initial vertex of this edge
    int rndd_minus; // terminal vertex of this edge
    int delta_plus; // edge starts from rndd_plus
    int delta_minus; // edge terminates at rndd_minus
    int len; // length
    String name;
    int id;
}

public class ShortestPath {

    /** The zoom. */
    float zoom;

    /** The number of nodes and edges. */
    int n, m;

    /** The start and end nodes. */
    int u, snode, enode;

    /** The first and last nodes of pre_s. */
    int pre_s_first, pre_s_last;

    /** Indicates if the graph is directed. */
    boolean isdigraph;

    /** The iteration and step counts. */
    int iteration, step;

    /** The nodes and edges arrays. */
    Node[] v;
    Edge[] e;

    /** The road network. */
    RoadNetwork network;

    /** The path points. */
    int p1, p2;

    /** The path. */
    Vector<ItineraryState> path = new Vector<>();

    /** Indicates if the goal is found. */
    boolean findGoal = false;

    /**
     * Find node by name.
     *
     * @param name the name
     * @return the node index
     */
    int findNode(String name) {
        for (int i = 0; i < n; i++) {
            if (v[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find node by id.
     *
     * @param id the id
     * @return the node index
     */
    int findNode(int id) {
        for (int i = 0; i < n; i++) {
            if (v[i].id == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find edge between two nodes.
     *
     * @param numNode1 the first node
     * @param numNode2 the second node
     * @return the edge index
     */
    public int findEdge(int numNode1, int numNode2) {
        int r = -1;
        for (int i = 0; i < m; i++) {
            int numPt1 = v[e[i].rndd_minus].id;
            int numPt2 = v[e[i].rndd_plus].id;
            if ((numNode1 == numPt1 && numNode2 == numPt2) || (numNode1 == numPt2 && numNode2 == numPt1)) {
                r = i;
                break;
            }
        }
        return r;
    }

    /**
     * Get the name of an edge.
     *
     * @param id the edge id
     * @return the edge name
     */
    public String getEdgeName(int id) {
        return e[id].name;
    }

    /**
     * Get the length of an edge.
     *
     * @param id the edge id
     * @return the edge length
     */
    public int getEdgeLength(int id) {
        return e[id].len;
    }

    /**
     * Get the coordinates of a node.
     *
     * @param id the node id
     * @return the node coordinates
     */
    public Point getNodeCoords(int id) {
        Node node = v[findNode(id)];
        return new Point(node.x, node.y);
    }

    /**
     * Get the id of a node by index.
     *
     * @param id the node index
     * @return the node id
     */
    public int getNodeId(int id) {
        return v[id].id;
    }

    /**
     * Find the closest node to a given point.
     *
     * @param pt the point
     * @return the closest node id
     */
    public int findNearestNode(Point pt) {
        // Find the closest node to the given point
        int idClosestNode = -1;
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            Node node = v[i];
            Point currentPoint = new Point(node.x, node.y);
            double currentDistance = currentPoint.distance(pt);
            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                idClosestNode = node.id;
            }
        }
        return idClosestNode;
    }

    /**
     * Input graph.
     *
     * @param res the road network
     */
    void input_graph(RoadNetwork res) {
        n = res.getPointCount();
        m = res.getConnectionCount();
        isdigraph = false;
        double z = zoom;

        // Initialize nodes
        int i = 0;
        for (Iterator<Integer> f = res.getPointsList().iterator(); f.hasNext(); ) {
            int pointNum = f.next();
            Point pt = res.getPoint(pointNum);
            Node node = new Node();
            node.name = String.valueOf(pointNum);
            node.id = pointNum;
            node.x = (int) (pt.getX() * z);
            node.y = (int) (pt.getY() * z);
            v[i++] = node;
        }

        // Initialize edges
        i = 0;
        for (Iterator<String> f = res.getRoadsList().iterator(); f.hasNext(); ) {
            String roadName = f.next();
            Road route = res.getRoad(roadName);
            Integer previousPt = null;
            for (int ipt = 0; ipt < route.getNumberOfPoints(); ipt++) {
                Integer currentPt = route.getPoint(ipt);
                if (previousPt != null) {
                    Edge edge = new Edge();
                    edge.name = roadName;
                    edge.id = i;
                    edge.rndd_plus = findNode(currentPt);
                    edge.rndd_minus = findNode(previousPt);
                    edge.len = (int) res.getPoint(currentPt).distance(res.getPoint(previousPt));
                    e[i++] = edge;
                }
                previousPt = currentPt;
            }
        }

        for (i = 0; i < n; i++) {
            v[i].succ = v[i].pred = -2;
            v[i].prev = v[i].dist = -1;
            v[i].pw = 0;
        }
        iteration = step = 0;
    }

    /**
     * Initialize adjacency lists.
     */
    void rdb() {
        for (int i = 0; i < n; i++) {
            v[i].delta_plus = v[i].delta_minus = -1;
        }
        for (int i = 0; i < m; i++) {
            e[i].delta_plus = e[i].delta_minus = -1;
        }
        for (int i = 0; i < m; i++) {
            int k = e[i].rndd_plus;
            if (v[k].delta_plus == -1) {
                v[k].delta_plus = i;
            } else {
                k = v[k].delta_plus;
                while (e[k].delta_plus >= 0) {
                    k = e[k].delta_plus;
                }
                e[k].delta_plus = i;
            }
            k = e[i].rndd_minus;
            if (v[k].delta_minus == -1) {
                v[k].delta_minus = i;
            } else {
                k = v[k].delta_minus;
                while (e[k].delta_minus >= 0) {
                    k = e[k].delta_minus;
                }
                e[k].delta_minus = i;
            }
        }
    }

    /**
     * Append node to pre_s list.
     *
     * @param i the node index
     */
    void append_pre_s(int i) {
        v[i].succ = v[i].pred = -1;
        if (pre_s_first < 0) {
            pre_s_first = i;
        } else {
            v[pre_s_last].succ = i;
        }
        v[i].pred = pre_s_last;
        pre_s_last = i;
    }

    /**
     * Remove node from pre_s list.
     *
     * @param i the node index
     */
    void remove_pre_s(int i) {
        int succ = v[i].succ;
        int pred = v[i].pred;
        if (succ >= 0) {
            v[succ].pred = pred;
        } else {
            pre_s_last = pred;
        }
        if (pred >= 0) {
            v[pred].succ = succ;
        } else {
            pre_s_first = succ;
        }
    }

    /**
     * Step 1: Initialize the search.
     */
    void step1() {
        u = snode;
        for (int i = 0; i < n; i++) {
            v[i].succ = v[i].pred = -2;
            v[i].prev = v[i].dist = -1;
        }
        v[u].succ = -3;
        v[u].dist = 0;
        pre_s_first = pre_s_last = -1;
    }

    /**
     * Step 2: Replace labels.
     */
    void step2() {
        int j = v[u].delta_plus;
        while (j >= 0) {
            int i = e[j].rndd_minus;
            if ((v[i].succ >= -2) && ((v[i].dist < 0) || (v[i].dist > v[u].dist + e[j].len))) {
                if (v[i].dist < 0) {
                    append_pre_s(i);
                }
                v[i].dist = v[u].dist + e[j].len;
                v[i].prev = u; // label
            }
            j = e[j].delta_plus;
        }
        if (!isdigraph) {
            j = v[u].delta_minus;
            while (j >= 0) {
                int i = e[j].rndd_plus;
                if ((v[i].succ >= -2) && ((v[i].dist < 0) || (v[i].dist > v[u].dist + e[j].len))) {
                    if (v[i].dist < 0) {
                        append_pre_s(i);
                    }
                    v[i].dist = v[u].dist + e[j].len;
                    v[i].prev = u; // label
                }
                j = e[j].delta_minus;
            }
        }
        v[u].succ = -4;
    }

    /**
     * Step 3: Find the shortest node in Sbar.
     */
    void step3() {
        int rho = -1;
        for (int i = pre_s_first; i >= 0; i = v[i].succ) {
            if ((rho < 0) || (rho > v[i].dist)) {
                rho = v[i].dist;
                u = i;
            }
        }
        remove_pre_s(u);
        v[u].succ = -3;
        if (v[u].id == p2) {
            int k = 0;
            while (true) {
                findGoal = true;
                path.insertElementAt(new ItineraryState(v[u].id, v[u].prev), 0);
                if (v[u].id == p1) {
                    break;
                }
                if (v[u].prev >= 0) {
                    u = v[u].prev;
                } else {
                    path.clear();
                    break;
                }
                k++;
                if (k > n) {
                    path.clear();
                    break;
                }
            }
        }
    }

    /**
     * Step 4: Finalize the step.
     */
    void step4() {
        v[u].succ = -4;
    }

    /**
     * Calculate weight.
     *
     * @param n the node
     * @param x the x component
     * @param y the y component
     * @return the weight
     */
    double weight(Node n, double x, double y) {
        double w = 0, z, xx, yy;
        for (int j = n.delta_plus; j >= 0; j = e[j].delta_plus) {
            xx = v[e[j].rndd_minus].x - n.x;
            yy = v[e[j].rndd_minus].y - n.y;
            z = (x * xx + y * yy) / Math.sqrt((x * x + y * y) * (xx * xx + yy * yy)) + 1.0;
            w += Math.pow(z, 4);
        }
        for (int j = n.delta_minus; j >= 0; j = e[j].delta_minus) {
            xx = v[e[j].rndd_plus].x - n.x;
            yy = v[e[j].rndd_plus].y - n.y;
            z = (x * xx + y * yy) / Math.sqrt((x * x + y * y) * (xx * xx + yy * yy)) + 1.0;
            w += Math.pow(z, 4);
        }
        return w;
    }

    /**
     * Initialize sub-steps.
     */
    void init_sub() {
        int[] x = {1, 0, -1, 1, 0, -1};
        int[] y = {1, 1, 1, -1, -1, -1};
        for (int i = 0; i < n; i++) {
            int k = 0;
            double w = weight(v[i], x[0], y[0]);
            for (int j = 1; j < 6; j++) {
                double z = weight(v[i], x[j], y[j]);
                if (z < w) {
                    w = z;
                    k = j;
                }
            }
            v[i].dx = x[k];
            v[i].dy = y[k];
        }
    }

    /**
     * Initialize the shortest path algorithm.
     *
     * @param res the road network
     * @param z the zoom level
     */
    public void init(RoadNetwork res, float z) {
        network = res;
        zoom = z;
        n = network.getPointCount();
        m = network.getConnectionCount();
        v = new Node[n];
        e = new Edge[m];
        input_graph(network);
        findGoal = false;
        path.clear();
        iteration = step = 0;
        snode = 0;
        rdb();
        init_sub();
    }

    /**
     * Solve the shortest path problem.
     *
     * @param ps the start node id
     * @param pe the end node id
     * @return the path as a vector of itinerary states
     */
    public Vector<ItineraryState> solve(int ps, int pe) {
        iteration = step = 0;
        p1 = ps;
        p2 = pe;
        findGoal = false;
        path.clear();
        if (p1 == p2) {
            path.insertElementAt(new ItineraryState(p1, p2), 0);
            return path;
        }
        snode = findNode(p1);
        enode = findNode(p2);
        rdb();
        init_sub();
        Exec();
        return path;
    }

    /**
     * Calculate the coordinates.
     *
     * @param a the a component
     * @param b the b component
     * @param w the width
     * @param h the height
     * @return the coordinates as an array
     */
    int[] xy(int a, int b, int w, int h) {
        int[] x = new int[2];
        if (Math.abs(w * b) >= Math.abs(h * a)) {
            x[0] = ((b >= 0) ? 1 : -1) * a * h / b / 2;
            x[1] = ((b >= 0) ? 1 : -1) * h / 2;
        } else {
            x[0] = ((a >= 0) ? 1 : -1) * w / 2;
            x[1] = ((a >= 0) ? 1 : -1) * b * w / a / 2;
        }
        return x;
    }

    /**
     * Execute the shortest path algorithm.
     */
    public void Exec() {
        while (iteration < n) {
            if (iteration == 0) {
                step1();
                iteration++;
                step = 2;
            } else {
                if (step == 2) {
                    step2();
                    step = 3;
                } else {
                    step3();
                    iteration++;
                    step = 2;
                }
            }
            if (findGoal) {
                break;
            }
        }
        step4();
    }
}
