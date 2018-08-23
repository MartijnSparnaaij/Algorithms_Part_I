import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 *
 */

public class FastCollinearPoints {
    
   private int segmentCount = 0;
   private LineSegment[] segments;
   
   public FastCollinearPoints(Point[] points) {
       // finds all line segments containing 4 or more points
       // For every point (N)
       // Calculate slopes between it and all other points (N)
       // Sort slopes
       // Loop through the sorted array to find all collinear points (N)
       for (int i = 0; i < points.length-3; i++) {
           Point basePoint = points[i];
           Arrays.sort(points, i+1, points.length, basePoint.slopeOrder());
           int curCount = 1;
           int startIndex = i + 1;
           double curSlope = basePoint.slopeTo(points[startIndex]);
           double nextSlope;           
           for (int j = i+2; j < points.length; j++) {
               nextSlope = points[j - 1].slopeTo(points[j]);
               if (nextSlope == curSlope) {
                   curCount++;
                   continue;
               }
               if (curCount >= 3) {
                   //Create line segment
                   addLineSegement(basePoint, points, startIndex, j-1);
               } 
               curSlope = nextSlope;
               curCount = 1;             
               startIndex = j;
           }
           if (curCount >= 3) {
               addLineSegement(basePoint, points, startIndex, points.length);
           }
       }
   }
   
   private void addLineSegement(Point basePoint, Point[] points, int startIndex, int stopIndex) {
       if (segmentCount == segments.length) resize(2*segments.length);
       Point startPoint = basePoint;
       Point endPoint = basePoint;
       for (int i = startIndex; i <= stopIndex; i++) {
           updateStartEnd(startPoint, endPoint, points[i]);
       }
       segments[segmentCount++] = new LineSegment(startPoint, endPoint);
   }
   
   private void updateStartEnd(Point startPoint, Point endPoint, Point otherPoint) {
       if (otherPoint.compareTo(startPoint) < 0) startPoint = otherPoint;
       if (otherPoint.compareTo(endPoint) > 0) endPoint = otherPoint;
   }
   
   private void resize(int capacity) {
       LineSegment[] copy = new LineSegment[capacity];
       for (int i = 0; i < segmentCount; i++) {
           copy[i] = segments[i];
       }
       segments = copy;
   }
   
   public int numberOfSegments() {
       // the number of line segments
       return segmentCount;
   }
   
   public LineSegment[] segments() {
       // the line segments
       return segments;
   }
   
   public static void main(String[] args) {

       // read the n points from a file
       In in = new In(args[0]);
       int n = in.readInt();
       Point[] points = new Point[n];
       for (int i = 0; i < n; i++) {
           int x = in.readInt();
           int y = in.readInt();
           points[i] = new Point(x, y);
       }

       // draw the points
       StdDraw.enableDoubleBuffering();
       StdDraw.setXscale(0, 32768);
       StdDraw.setYscale(0, 32768);
       for (Point p : points) {
           p.draw();
       }
       StdDraw.show();
       
       // print and draw the line segments
       BruteCollinearPoints collinear = new BruteCollinearPoints(points);
       StdOut.println(collinear.numberOfSegments());
       for (LineSegment segment : collinear.segments()) {
           StdOut.println(segment);
           segment.draw();
       }
       StdDraw.show();
   }
   
}