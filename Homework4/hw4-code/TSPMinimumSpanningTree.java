import java.util.*;

public class TSPMinimumSpanningTree {
    public static ArrayList<Line2D> computeMST(ArrayList<Point2D> points) {
        ArrayList<Line2D> tour = new ArrayList<>();
        Queue<Line2D> edgeList = new PriorityQueue<>(Comparator.comparingDouble(line -> line.getP1().distance(line.getP2())));
        List<HashSet<Point2D>> clusters = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                edgeList.add(new Line2D(points.get(i), points.get(j)));
            }
        }
        /* Using Kruskal's Algorithm to find MST */
        while (tour.size() < points.size() - 1 && !edgeList.isEmpty()) {
            Line2D edge = edgeList.poll();
            if (!hasCycle(clusters, edge)) {
                updateClusters(clusters, edge);
                tour.add(edge);
                edge.getP1().setVisited(true);
                edge.getP2().setVisited(true);
            }
        }

        return tour;
    }

    private static boolean hasCycle(List<HashSet<Point2D>> clusters, Line2D edge) {
        for (HashSet<Point2D> cluster : clusters) {
            if (cluster.contains(edge.getP1()) && cluster.contains(edge.getP2())) return true;
        }
        return false;
    }

    private static void updateClusters(List<HashSet<Point2D>> clusters, Line2D edge) {
        for (int i = 0; i < clusters.size(); i++) {
            HashSet<Point2D> cluster1 = clusters.get(i);
            if (cluster1.contains(edge.getP1()) ^ cluster1.contains(edge.getP2())) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    HashSet<Point2D> cluster2 = clusters.get(j);
                    if (cluster2.contains(edge.getP1()) ^ cluster2.contains(edge.getP2())) {
                        cluster1.addAll(cluster2);
                        clusters.remove(cluster2);
                        return;
                    }
                }
                clusters.get(i).add(edge.getP1());
                clusters.get(i).add(edge.getP2());
                return;
            }
        }
        HashSet<Point2D> newCluster = new HashSet<>();
        newCluster.add(edge.getP1());
        newCluster.add(edge.getP2());
        clusters.add(newCluster);
    }

    public static ArrayList<Line2D> computeDFSTour(ArrayList<Point2D> points, ArrayList<Line2D> mst) {
        ArrayList<Line2D> tour = new ArrayList<>();
        ArrayList<Point2D> tourVertices = new ArrayList<>();
        Stack<Point2D> stack = new Stack<>();

        for (Point2D point : points) point.setVisited(false);
        stack.push(points.get(0));
        while (!stack.isEmpty()) {
            Point2D current = stack.pop();
            if (current.isVisited()) continue;
            current.setVisited(true);
            tourVertices.add(current);
            for (Line2D edge : mst) {
                if (edge.getP1().equals(current)) stack.push(edge.getP2());
                else if (edge.getP2().equals(current)) stack.push(edge.getP1());
            }
        }
        for (int i = 0; i < tourVertices.size() - 1; i++) tour.add(new Line2D(tourVertices.get(i), tourVertices.get(i + 1)));
        tour.add(new Line2D(tourVertices.get(tourVertices.size() - 1), tourVertices.get(0)));
        return tour;
    }
}
