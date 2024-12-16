/*
 * Niharika Agrawal
 * CSC 172
 */

public class DijkstraAlgorithm {

    /**
     * Find the shortest path from start to end using Dijkstra's algorithm.
     * 
     * @param graph The graph to search in.
     * @param startID The ID of the start node.
     * @param endID The ID of the end node.
     * @return A list of nodes representing the shortest path from start to end.
     */
    public static URArrayList<String> findShortestPath(Graph graph, String startID, String endID) {
        // Initialize the distance map with infinity
        URMaps<String, Double> distances = new URMaps<>();
        URMaps<String, String> previousNodes = new URMaps<>();
        URArrayList<String> visited = new URArrayList<>();

        // Initialize all nodes' distances to infinity
        for (Node node : graph.getAllNodes()) {
            distances.put(node.getId(), Double.POSITIVE_INFINITY);
        }
        distances.put(startID, 0.0);

        // Initialize the list of nodes to process, starting with the start node
        URArrayList<String> nodesToProcess = new URArrayList<>();
        nodesToProcess.add(startID);

        while (!nodesToProcess.isEmpty()) {
            // Get the node with the smallest distance from the list of nodes to process
            String currentNodeID = getNodeWithMinDistance(nodesToProcess, distances);
            nodesToProcess.remove(currentNodeID);  // Remove this node from the processing list

            // If we've already visited this node, skip it
            if (visited.contains(currentNodeID)) {
                continue;
            }

            visited.add(currentNodeID);

            // If we've reached the end node, stop the search
            if (currentNodeID.equals(endID)) {
                break;
            }

            // Process all adjacent nodes (edges) of the current node
            for (Edge edge : graph.getEdgesFrom(currentNodeID)) {
                String neighborID = edge.getTo().getId();

                if (visited.contains(neighborID)) {
                    continue;  // Skip visited nodes
                }

                double newDist = distances.get(currentNodeID) + edge.getDistance();
                if (newDist < distances.get(neighborID)) {
                    distances.put(neighborID, newDist);
                    previousNodes.put(neighborID, currentNodeID);

                    // Add the neighboring node to the list to be processed
                    nodesToProcess.add(neighborID);
                }
            }
        }

        // Reconstruct the path
        URArrayList<String> path = new URArrayList<>();
        String currentNode = endID;
        while (currentNode != null) {
            path.add(0, currentNode);
            currentNode = previousNodes.get(currentNode);
        }

        // If no valid path was found, return null
        if (path.size() == 1 && !path.get(0).equals(startID)) {
            return null;
        }

        return path;
    }

    /**
     * Calculate the total distance of the shortest path from start to end.
     * 
     * @param graph The graph to search in.
     * @param startID The ID of the start node.
     * @param endID The ID of the end node.
     * @return The total distance of the shortest path.
     */
    public static double getTotalDistance(Graph graph, String startID, String endID) {
        // Run Dijkstra's algorithm to get the shortest path
        URArrayList<String> path = findShortestPath(graph, startID, endID);

        if (path == null) {
            return -1; // No valid path found
        }

        // Calculate the total distance by summing the edges in the path
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String fromID = path.get(i);
            String toID = path.get(i + 1);

            // Find the edge between 'fromID' and 'toID'
            for (Edge edge : graph.getEdgesFrom(fromID)) {
                if (edge.getTo().getId().equals(toID)) {
                    totalDistance += edge.getDistance();
                    break;
                }
            }
        }

        return totalDistance;
    }

    /**
     * Helper method to get the node with the minimum distance from the list of nodes to process.
     * 
     * @param nodes The list of nodes to process.
     * @param distances The map of distances for each node.
     * @return The node ID with the minimum distance.
     */
    private static String getNodeWithMinDistance(URArrayList<String> nodes, URMaps<String, Double> distances) {
        String minNode = null;
        double minDistance = Double.POSITIVE_INFINITY;

        // Find the node with the minimum distance
        for (String nodeID : nodes) {
            double dist = distances.get(nodeID);
            if (dist < minDistance) {
                minDistance = dist;
                minNode = nodeID;
            }
        }

        return minNode;
    }
}
