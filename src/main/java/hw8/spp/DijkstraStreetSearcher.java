package hw8.spp;

import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;


public class DijkstraStreetSearcher extends StreetSearcher {
    private HashSet<Vertex<String>> explored;
    private HashMap<Vertex<String>, Double> distances;
    private PriorityQueue<Vertex<String>> distancesPQ;

    /**
     * Create a StreetSearcher object.
     *
     * @param graph an implementation of Graph ADT.
     */
    public DijkstraStreetSearcher(Graph<String, String> graph) {
        super(graph);
        explored = new HashSet<>();
        distances = new HashMap<>();
        distancesPQ = new PriorityQueue<>(11, new VertexComparator());
    }

    @Override
    public void findShortestPath(String startName, String endName) {
        Vertex<String> start = vertices.get(startName);
        Vertex<String> end = vertices.get(endName);

        double totalDist = dijkstra(start, end);

        // These method calls will create and print the path
        List<Edge<String>> path = getPath(end, start);
        if (VERBOSE) {
            printPath(path, totalDist);
        }
    }

    private double dijkstra(Vertex<String> start, Vertex<String> end) {
        // Initialize previous to null and distance to infinity
        for (Vertex<String> v : graph.vertices()) {
            if (v.equals(start)) {
                distances.put(start, 0.0);
            } else {
                distances.put(v, Double.MAX_VALUE);
            }

            graph.label(v, null);
            distancesPQ.add(v);
        }

        while (!distancesPQ.isEmpty()) {
            Vertex<String> curVertex = distancesPQ.poll();
            explored.add(curVertex);
            navigateEdges(curVertex);
        }

        return distances.get(end);
    }

    private void navigateEdges(Vertex<String> curVertex) {
        for (Edge<String> adjEdge : graph.outgoing(curVertex)) {
            Vertex<String> adjVertex = graph.to(adjEdge);

            if (!explored.contains(adjVertex)) {
                double oldDistance = distances.get(adjVertex);
                double newDistance = distances.get(curVertex) + (double) (graph.label(adjEdge));
                if (newDistance < oldDistance) {
                    distances.put(adjVertex, newDistance);
                    graph.label(adjVertex, adjEdge);

                    distancesPQ.remove(adjVertex);
                    distancesPQ.add(adjVertex);
                }
            }
        }
    }

    private class VertexComparator implements Comparator<Vertex<String>> {
        @Override
        public int compare(Vertex<String> o1, Vertex<String> o2) {
            if (distances.get(o1) < distances.get(o2)) {
                return -1;
            } else if (distances.get(o1) > distances.get(o2)) {
                return 1;
            }
            return 0;
        }
    }
}
