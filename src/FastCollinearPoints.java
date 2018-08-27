import java.util.ArrayList;
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
   private LineSegment[] segments = new LineSegment[0];
   
   
   public FastCollinearPoints(Point[] points) {
       // finds all line segments containing 4 or more points
       // For every point (N)
       // Calculate slopes between it and all other points (N)
       // Sort slopes
       // Loop through the sorted array to find all collinear points (N)
	   Point[] pointsSorted = checkInput(points);
	   if (points.length < 4) return;
	   performSegmentSearch(points, pointsSorted);
   }
   
   private void performSegmentSearch(Point[] points, Point[] pointsSorted) {
	   ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
	   
       for (int i = 0; i < points.length; i++) {
           Point basePoint = points[i];
          
           Arrays.sort(pointsSorted, basePoint.slopeOrder());
           int curCount = 1;
           int startIndex = 1;
           double curSlope = basePoint.slopeTo(pointsSorted[startIndex]);
           double nextSlope;           
           for (int j = startIndex+1; j < pointsSorted.length; j++) {
               nextSlope = basePoint.slopeTo(pointsSorted[j]);
               if (nextSlope == curSlope) {
                   curCount++;
                   continue;
               }
               if (curCount >= 3) {
                   //Create line segment
                   addLineSegement(segmentList, basePoint, pointsSorted, startIndex, j-1);
               } 
               curSlope = nextSlope;
               curCount = 1;             
               startIndex = j;
           }
           if (curCount >= 3) {
               addLineSegement(segmentList, basePoint, pointsSorted, startIndex, pointsSorted.length-1);
           }
       }
       segments = segmentList.toArray(new LineSegment[segmentList.size()]);
   }
   
   private static Point[] checkInput(Point[] points) {
	   if (points == null) throw new IllegalArgumentException("Input is null");
	   for (int i = 0; i < points.length; i++) {
		   if (points[i] == null) throw new IllegalArgumentException("A point in the input is null");
	   }
	   Point[] pointsSorted = points.clone();
       Arrays.sort(pointsSorted);
       for (int i = 1; i < pointsSorted.length; i++) {
		   if (pointsSorted[i].compareTo(pointsSorted[i-1]) == 0) throw new IllegalArgumentException("The input contains a duplicate point");
       }	  
       return pointsSorted;
   }
   
   private void addLineSegement(ArrayList<LineSegment> segmentList, Point basePoint, Point[] points, int startIndex, int stopIndex) {
       Point startPoint = basePoint;
       Point endPoint = basePoint;
       for (int i = startIndex; i <= stopIndex; i++) {
    	   Point otherPoint = points[i];
    	   if (otherPoint.compareTo(startPoint) < 0) startPoint = otherPoint;
           if (otherPoint.compareTo(endPoint) > 0) endPoint = otherPoint;
       }
       if (startPoint == basePoint) {
    	   segmentList.add(new LineSegment(startPoint, endPoint));
    	   segmentCount++;
       }
   }
  
   public int numberOfSegments() {
       // the number of line segments
       return segmentCount;
   }
   
   public LineSegment[] segments() {
       // the line segments
	   return Arrays.copyOf(segments, numberOfSegments());
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
       FastCollinearPoints collinear = new FastCollinearPoints(points);
       StdOut.println(collinear.numberOfSegments());
       for (LineSegment segment : collinear.segments()) {
           StdOut.println(segment);
           segment.draw();
       }
       StdDraw.show();
   }
   
}