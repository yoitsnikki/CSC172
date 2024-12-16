/*
 * Niharika Agrawal
 * CSC 172
 */
import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private final Graph graph;
    private final String startIntersection;
    private final String endIntersection;

    public MapPanel(Graph graph, String startIntersection, String endIntersection) {
        this.graph = graph;
        this.startIntersection = startIntersection;
        this.endIntersection = endIntersection;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLACK);

        // Draw all roads (edges) in gray
        for (Edge edge : graph.getAllEdges()) {
            Node from = edge.getFrom();
            Node to = edge.getTo();

            Point fromPoint = convertToPoint(from);
            Point toPoint = convertToPoint(to);

            g2.setColor(Color.GRAY);
            g2.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
        }

        // Draw all intersections (nodes) in red
        g2.setColor(Color.RED);
        for (Node node : graph.getAllNodes()) {
            Point p = convertToPoint(node);
            g2.fillOval(p.x - 3, p.y - 3, 6, 6);  // Draw small circle for intersection
        }

        // Highlight the shortest path if available
        if (startIntersection != null && endIntersection != null) {
            URArrayList<Edge> pathEdges = getPathEdges(graph, startIntersection, endIntersection);
            if (pathEdges != null) {
                g2.setColor(Color.BLUE);  // Path color
                g2.setStroke(new BasicStroke(3));  // Thicker line for path
                for (Edge edge : pathEdges) {
                    Point from = convertToPoint(edge.getFrom());
                    Point to = convertToPoint(edge.getTo());
                    g2.drawLine(from.x, from.y, to.x, to.y);  // Draw path as blue line
                }
            }
        }
    }

    private Point convertToPoint(Node node) {
        int width = getWidth();
        int height = getHeight();
        double x = (node.getLongitude() + 180) / 360 * width; // Normalize longitude
        double y = (90 - node.getLatitude()) / 180 * height;  // Normalize latitude
        return new Point((int) x, (int) y);
    }

    // Get the edges of the shortest path using your custom classes
    private URArrayList<Edge> getPathEdges(Graph graph, String startID, String endID) {
        URArrayList<Edge> pathEdges = new URArrayList<>();  // Using your URArrayList
        URArrayList<String> path = DijkstraAlgorithm.findShortestPath(graph, startID, endID);
        if (path == null || path.size() < 2) {
            return null; // No valid path
        }

        // Traverse the path and add the edges
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);

            // Find the edge between 'from' and 'to'
            for (Edge edge : graph.getEdgesFrom(from)) {
                if (edge.getTo().getId().equals(to)) {
                    pathEdges.add(edge);  // Add the edge to the path
                    break;
                }
            }
        }

        return pathEdges;  // Return the list of path edges
    }
}
