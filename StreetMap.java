/*
 * Niharika Agrawal
 * CSC 172
 */

import java.io.*;
import javax.swing.*;

public class StreetMap {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java StreetMap <filename> [--show] [--directions <startIntersection> <endIntersection>]");
            return;
        }

        // Parse command-line arguments
        String filename = args[0];
        boolean showMap = false;
        boolean calculateDirections = false;
        String startIntersection = null;
        String endIntersection = null;

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--show":
                    showMap = true;
                    break;
                case "--directions":
                    calculateDirections = true;
                    if (i + 2 < args.length) {
                        startIntersection = args[++i];
                        endIntersection = args[++i];
                    } else {
                        System.out.println("Error: Missing start or end intersection for --directions.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Error: Unknown argument " + args[i]);
                    return;
            }
        }

        // Construct the graph by reading data
        Graph graph = new Graph();
        try {
            readData(filename, graph);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Process directions if required
        if (calculateDirections) {
            if (startIntersection == null || endIntersection == null) {
                System.out.println("Error: Missing intersections for --directions.");
                return;
            }

            System.out.println("Calculating shortest path from " + startIntersection + " to " + endIntersection);
            URArrayList<String> path = DijkstraAlgorithm.findShortestPath(graph, startIntersection, endIntersection);
            if (path != null) {
                System.out.println("Shortest Path:");
                for (String step : path) {
                    System.out.println(step);
                }
                double distance = DijkstraAlgorithm.getTotalDistance(graph, startIntersection, endIntersection);
                System.out.println("Total Distance: " + distance + " miles");
            } else {
                System.out.println("No path found between " + startIntersection + " and " + endIntersection);
            }
        }

        // Display the map if requested
        if (showMap) {
            System.out.println("Displaying map...");
            JFrame frame = new JFrame("Street Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.add(new MapPanel(graph, calculateDirections ? startIntersection : null, calculateDirections ? endIntersection : null));
            frame.setVisible(true);
        }
    }

    /**
     * Reads intersections and roads from the file and populates the graph.
     */
    private static void readData(String filename, Graph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens[0].equals("i")) {
                    // Intersection
                    String id = tokens[1];
                    double latitude = Double.parseDouble(tokens[2]);
                    double longitude = Double.parseDouble(tokens[3]);
                    graph.addNode(id, latitude, longitude);
                    System.out.println("Added intersection: " + id);
                } else if (tokens[0].equals("r")) {
                    // Road
                    String roadID = tokens[1];
                    String fromID = tokens[2];
                    String toID = tokens[3];
                    Node fromNode = graph.getNode(fromID);
                    Node toNode = graph.getNode(toID);

                    if (fromNode == null || toNode == null) {
                        System.out.println("Error: Road references invalid intersections: " + line);
                        continue; // Skip invalid roads
                    }

                    // Calculate the distance using Dijkstra's algorithm
                    double distance = DijkstraAlgorithm.getTotalDistance(graph, fromID, toID);

                    // Add edge to the graph
                    graph.addEdge(roadID, fromID, toID, distance);
                    System.out.println("Added road " + roadID + " between " + fromID + " and " + toID + " with distance: " + distance);
                }
            }
        }
    }
}
