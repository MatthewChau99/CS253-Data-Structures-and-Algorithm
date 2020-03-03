import java.lang.reflect.Array;
import java.util.*;

public class BipartiteGraph {
    // to get the edges incident with a vertex v, just call adjacencyMap.get(v)
    private HashMap<Vertex, ArrayList<Edge>> adjacencyMap;
    private ArrayList<Vertex> leftVertices;
    private ArrayList<Vertex> rightVertices;

    public BipartiteGraph(int nLeft, int nRight, ArrayList<int[]> edges) {
        leftVertices = new ArrayList<>();
        rightVertices = new ArrayList<>();
        adjacencyMap = new HashMap<>();
        for (int i = 0; i < nLeft; i++) {
            Vertex v = new Vertex(i, true);
            leftVertices.add(v);
            adjacencyMap.put(v, new ArrayList<>());
        }
        for (int i = 0; i < nRight; i++) {
            Vertex v = new Vertex(i, false);
            rightVertices.add(v);
            adjacencyMap.put(v, new ArrayList<>());
        }
        for (int[] edge : edges) {
            Vertex u = leftVertices.get(edge[0]);
            Vertex v = rightVertices.get(edge[1]);
            Edge e = new Edge(leftVertices.get(edge[0]), rightVertices.get(edge[1]));
            adjacencyMap.get(u).add(e);
            adjacencyMap.get(v).add(e);
        }
    }

    public ArrayList<Vertex> getVertices() {
        ArrayList<Vertex> verts = new ArrayList<>();
        verts.addAll(leftVertices);
        verts.addAll(rightVertices);
        return verts;
    }

    public ArrayList<Edge> getEdges() {
        ArrayList<Edge> edgeList = new ArrayList<>();
        for (Vertex v : leftVertices) {
            edgeList.addAll(adjacencyMap.get(v));
        }
        return edgeList;
    }

    public boolean augmentFlow(ArrayList<Edge> augmentingPath) {
        if (augmentingPath == null || augmentingPath.size() == 0) {
            return false;
        }

        for (int i = 0; i < augmentingPath.size(); i++) {
            if ((i % 2 == 0) == augmentingPath.get(i).isInMatching()) {
                // just a sanity check to make sure it alternates blue/red
                return false;
            }
        }
        //as long as the order of the edges is correct, augmenting a flow
        //can be done in this way
        for (int i = 0; i < augmentingPath.size(); i++) {
            augmentingPath.get(i).setInMatching(i % 2 == 0);
        }

        return true;
    }

    public ArrayList<Edge> findAugmentingPath() {
        HashSet<Vertex> unmarkedLeftVertices = new HashSet<>();
        HashSet<Vertex> unmarkedRightVertices = new HashSet<>();
        ArrayList<Edge> path;

        outer: for (Vertex leftVertex : leftVertices) {
            for (Edge edge : adjacencyMap.get(leftVertex)) {
                if (edge.isInMatching()) continue outer;
            }
            unmarkedLeftVertices.add(leftVertex);
        }

        outer: for (Vertex rightVertex : rightVertices) {
            for (Edge edge : adjacencyMap.get(rightVertex)) {
                if (edge.isInMatching()) continue outer;
            }
            unmarkedRightVertices.add(rightVertex);
        }

        if (unmarkedLeftVertices.isEmpty() || unmarkedRightVertices.isEmpty()) return null;

        for (Vertex leftVertex : unmarkedLeftVertices) {
            path = findPathBFS(leftVertex, unmarkedRightVertices, getEdges());
            if (path != null) return path;
        }

        return null;
    }

    private ArrayList<Edge> findPathBFS(Vertex startingVertex, HashSet<Vertex> unmarkedRightVertices, ArrayList<Edge> originalEdges) {
        ArrayList<Edge> newEdges = new ArrayList<>();
        Queue<Vertex> queue = new ArrayDeque<>();
        Map<Vertex, ArrayList<Edge>> pathmap = new HashMap<>();
        Map<Edge, Edge> edgeMap = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();


        for (Edge edge : originalEdges) {
            if (edge.isInMatching()) {
                Edge newEdge = new Edge(edge.getTail(), edge.getHead());
                edgeMap.put(newEdge, edge);
                newEdges.add(newEdge);
            } else newEdges.add(edge);
        }

        pathmap.put(startingVertex, new ArrayList<>());
        queue.add(startingVertex);
        visited.add(startingVertex);

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            if (unmarkedRightVertices.contains(vertex)) {
                ArrayList<Edge> augmentedPath = pathmap.get(vertex);
                for (int i = 0; i < augmentedPath.size(); i++) {
                    if (!augmentedPath.get(i).getHead().isLeftVertex()) augmentedPath.set(i, edgeMap.get(augmentedPath.get(i)));
                }
                return augmentedPath;
            }
            for (Edge edge : newEdges) {
                if (edge.getHead() == vertex && !visited.contains(edge.getTail())) {
                    ArrayList<Edge> updatedPath = new ArrayList<>(pathmap.get(edge.getHead()));
                    updatedPath.add(edge);
                    pathmap.put(edge.getTail(), updatedPath);
                    visited.add(edge.getTail());
                    queue.add(edge.getTail());
                }
            }
        }
        return null;
    }
}
