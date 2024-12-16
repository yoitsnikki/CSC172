/*
 * Niharika Agrawal
 * CSC 172
 */

public class Node {
    private String id;
    private double latitude;
    private double longitude;
    private URArrayList<Edge> edges;

    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new URArrayList<>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public URArrayList<Edge> getEdges() {
        return edges;
    }
}
