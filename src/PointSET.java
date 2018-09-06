import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij 
 * @date 6 sep. 2018
 *
 */
public class PointSET {

    private final SET<Point2D> points;

    public PointSET() {
        // construct an empty set of points
        points = new SET<>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return points.isEmpty();
    }

    public int size() {
        // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        checkPointInput(p);
        points.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        checkPointInput(p);
        return points.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        checkRectangleInput(rect);
        ArrayList<Point2D> pointsInRange = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p))
                pointsInRange.add(p);
        }
        return pointsInRange;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        double distance;
        for (Point2D pOther : points) {
            if (pOther == p)
                continue;
            distance = p.distanceSquaredTo(pOther);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = pOther;
            }
        }

        return nearest;
    }

    private void checkPointInput(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point cannot be null");
    }

    private void checkRectangleInput(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Rectangle cannot be null");

    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}
