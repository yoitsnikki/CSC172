/*
 * Niharika Agrawal
 * CSC 172
 */

public class Edge {
    private Node from;
    private Node to;
    private double distance; // Distance between 'from' and 'to'

    // Constructor that initializes the edge with two nodes and a distance
    public Edge(Node from, Node to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    // Getters for the edge properties
    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getDistance() {
        return distance;  // Return the distance of the edge
    }
}
