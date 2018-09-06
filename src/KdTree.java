import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij 
 * @date 6 sep. 2018
 *
 */
public class KdTree {

    private static boolean HORIZONTAL = true;
    private static boolean VERTICAL = false;
    
    private Node root = null;
    private int size = 0;
    //public int stepCount;

    public KdTree() {
        // construct an empty set of points
    }

    public boolean isEmpty() {
        // is the set empty?
        return root == null;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        checkPointInput(p);
        if (contains(p))
            return;
        root = put(root, null, p, VERTICAL);
        size++;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        checkPointInput(p);
        //stepCount = 0;
        return get(root, p, VERTICAL) != null;
    }

    public void draw() {
        // draw all points to standard draw
        drawNode(root, VERTICAL);        
    }
    
    private void drawNode(Node h, boolean orientation) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);        
        h.p.draw();        
        
        StdDraw.setPenRadius();
        boolean newOrientation;
        if (orientation == HORIZONTAL) {
            drawHorizontalLine(h);
            newOrientation = VERTICAL;
        } else {
            drawVerticalLine(h);        
            newOrientation = HORIZONTAL;
        }
        
        if (h.lb != null) drawNode(h.lb, newOrientation);
        if (h.rt != null) drawNode(h.rt, newOrientation);
    }
    

    private void drawHorizontalLine(Node h) {
        StdDraw.setPenColor(StdDraw.BLUE);
        double x0 = h.rect.xmin();
        double x1 = h.rect.xmax();
        double y0 = h.p.y();
        double y1 = h.p.y();
        
        StdDraw.line(x0, y0, x1, y1);
    }
    
    private void drawVerticalLine(Node h) {
        StdDraw.setPenColor(StdDraw.RED);
        double x0 = h.p.x();
        double x1 = h.p.x();
        double y0 = h.rect.ymin();
        double y1 = h.rect.ymax();
        
        StdDraw.line(x0, y0, x1, y1);
    }
    
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        checkRectangleInput(rect);
        ArrayList<Point2D> points = new ArrayList<>();
        
        return points;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        double minDistance = root.p.distanceSquaredTo(p);
        double distance;
        Node curNode = root;
        while (true) {
            if (curNode.lb != null) {
                distance = curNode.lb.p.distanceSquaredTo(p);
                if (distance < minDistance) {
                    minDistance = distance;
                    curNode = curNode.lb;
                    continue;
                }
            }
            if (curNode.rt != null) {
                distance = curNode.rt.p.distanceSquaredTo(p);
                if (distance < minDistance) {
                    minDistance = distance;
                    curNode = curNode.rt;
                    continue;
                }
            }
            break;            
        }
        
        return curNode.p;
    }

    
    
    private void checkPointInput(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point cannot be null");
    }

    private void checkRectangleInput(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Rectangle cannot be null");

    }
    
    private Node put(Node h, Node parent, Point2D p, boolean orientation) {
        if (h == null) return getNewNode(parent, p, orientation);
        boolean nextOrientation;
        if (orientation == HORIZONTAL) {
            nextOrientation = VERTICAL;
        } else {
            nextOrientation = HORIZONTAL;
        }
        if (isLeft(h, p, orientation)) h.lb  = put(h.lb, h,  p, nextOrientation);
        else h.rt  = put(h.rt, h,  p, nextOrientation);
        
        return h;
    }
    
    private Node getNewNode(Node h, Point2D p, boolean orientation) {
        double x0 = 0;
        double x1 = 1;
        double y0 = 0;
        double y1 = 1;
        
        if (h != null) {
            x0 = h.rect.xmin();
            x1 = h.rect.xmax();
            y0 = h.rect.ymin();
            y1 = h.rect.ymax();
            if (orientation == HORIZONTAL) {
                if (isLeft(h, p, VERTICAL)) {
                    x1 = h.p.x();
                } else {
                    x0 = h.p.x();
                }
            } else {
                if (isLeft(h, p, HORIZONTAL)) {
                    y1 = h.p.y();
                } else {
                    y0 = h.p.y();
                } 
            }
        }
        
        return new Node(p, null, null, new RectHV(x0,y0,x1,y1));
    }
    
    private boolean isLeft(Node h, Point2D p, boolean orientation) {
        double cmp;
        if (orientation == HORIZONTAL) {
            cmp = p.y() - h.p.y();
        } else {            
            cmp = p.x() - h.p.x();
        }
        return cmp < 0;
    }
  
    private Node get(Node h, Point2D p, boolean orientation) {
        //stepCount++;
        if (h == null) return null;
        boolean nextOrientation;
        if (orientation == HORIZONTAL) {
            nextOrientation = VERTICAL;
        } else {
            nextOrientation = HORIZONTAL;
        }
        if (h.p.equals(p)) return h;
        if (isLeft(h, p, orientation)) return get(h.lb,  p, nextOrientation);
        else return get(h.rt,  p, nextOrientation);
    }
    
    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        
        public Node(Point2D p, Node lb, Node rt, RectHV rect) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
            this.rect = rect;
        }
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        ArrayList<Point2D> points = new ArrayList<>();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            points.add(p);
        }
        
        //for (Point2D p: points) {
        //    StdOut.printf("%s: Contains=%b, Steps=%d\n" , p.toString(), kdtree.contains(p), kdtree.stepCount);
        //}
        
        Point2D p = new Point2D(0.81, 0.30);
        Point2D nearest = kdtree.nearest(p);
        StdOut.printf("Nearest to %s = %s\n", p.toString(), nearest.toString());
        
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        kdtree.draw();
        StdDraw.show();
    }
}
