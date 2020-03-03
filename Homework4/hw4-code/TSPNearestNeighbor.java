import javax.print.DocFlavor;
import java.awt.*;
import java.nio.channels.Pipe;
import java.util.*;
import java.util.List;

public class TSPNearestNeighbor {
    public static ArrayList<Line2D> computeNearestNeighbor(ArrayList<Point2D> points) {
        if (points.isEmpty()) return null;
        resetVisited(points);

        ArrayList<Point2D> pointsCopy = new ArrayList<>(points);
        ArrayList<Point2D> tourVertices = new ArrayList<>();
        ArrayList<Line2D> tour = new ArrayList<>();
        Map<Point2D, Double> hm = new HashMap<>();

        Point2D current = pointsCopy.get(0);

        for (int i = 0; i < pointsCopy.size(); i++) hm.put(points.get(i), current.distance(points.get(i)));

        while (!pointsCopy.isEmpty()) {
            tourVertices.add(current);
            current.setVisited(true);
            Point2D nearest = current;
            Double nearestDistance = Double.MAX_VALUE;
            resetHashMap(hm, current);

            for (Point2D p : hm.keySet()) {
                if (current.distance(p) < nearestDistance && !p.isVisited()) {
                    nearest = p;
                    nearestDistance = current.distance(p);
                }
            }
            pointsCopy.remove(current);
            current = nearest;
        }

        for (int i = 0; i < tourVertices.size() - 1; i++) {
            tour.add(new Line2D(tourVertices.get(i), tourVertices.get(i + 1)));
        }
        tour.add(new Line2D(tourVertices.get(tourVertices.size() - 1), tourVertices.get(0)));
        return tour;

    }

    private static void resetHashMap(Map<Point2D, Double> hm, Point2D point) {
        for (Point2D p : hm.keySet()) {
            if (!p.equals(point)) hm.put(p, p.distance(point));
            else hm.put(p, Double.MAX_VALUE);
        }
    }

    private static void resetVisited(ArrayList<Point2D> points) {
        for (Point2D point : points) point.setVisited(false);
    }

}
