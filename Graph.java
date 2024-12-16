/*
 * Niharika Agrawal
 * CSC 172
 */

public class Graph {
    private URMaps<String, Node> nodes; // Maps node IDs to Node objects
    private URArrayList<Edge> edges; // Stores all edges in the graph

    // Constructor for creating a new graph
    public Graph() {
        nodes = new URMaps<>(); // Initialize the map of nodes
        edges = new URArrayList<>(); // Initialize the list of edges
    }

    // Adds a node to the graph by accepting the node ID, latitude, and longitude
    public void addNode(String id, double latitude, double longitude) {
        Node node = new Node(id, latitude, longitude); // Create the node with given parameters
        nodes.put(id, node); // Store the node in the map with the ID as the key
    }

    // Retrieves a node by its ID
    public Node getNode(String id) {
        return nodes.get(id); // Return the node associated with the given ID
    }

    // Adds an edge to the graph by accepting the road ID, from node ID, to node ID, and distance
    public void addEdge(String roadID, String fromId, String toId, double distance) {
        Node fromNode = getNode(fromId); // Get the starting node by ID
        Node toNode = getNode(toId); // Get the ending node by ID

        if (fromNode != null && toNode != null) {
            Edge newEdge = new Edge(fromNode, toNode, distance); // Create a new edge
            edges.add(newEdge); // Add the edge to the list of edges
            System.out.println("Added edge from " + fromId + " to " + toId + " with distance: " + distance);
        } else {
            System.out.println("Error: One or both nodes not found for edge: " + roadID);
        }
    }

    // Retrieves all edges connected to the given node ID
    public URArrayList<Edge> getEdgesFrom(String nodeId) {
        URArrayList<Edge> result = new URArrayList<>(); // Create a new list to store the results
        for (Edge edge : edges) { // Iterate over all edges in the graph
            if (edge.getFrom().getId().equals(nodeId)) { // Check if the edge starts from the node with nodeId
                result.add(edge); // Add the edge to the result list
            }
        }
        return result; // Return the list of edges starting from the given node ID
    }

    // Retrieves all nodes in the graph
    public URArrayList<Node> getAllNodes() {
        URArrayList<Node> nodeList = new URArrayList<>(); // Create a list to store all nodes
        for (String nodeId : nodes.keySet()) { // Iterate over all node IDs in the map
            nodeList.add(nodes.get(nodeId)); // Add the node to the list
        }
        return nodeList; // Return the list of nodes
    }

    // Retrieves all edges in the graph
    public URArrayList<Edge> getAllEdges() {
        return edges; // Return the list of all edges in the graph
    }
}
