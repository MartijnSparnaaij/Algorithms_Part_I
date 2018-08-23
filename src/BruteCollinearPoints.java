import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 * @date 21-08-2018
 *
 */

public class BruteCollinearPoints {
    
   private int segmentCount = 0;
   private LineSegment[] segments;
   private Point[] points; 
   
   public BruteCollinearPoints(Point[] points) {
       // finds all line segments containing 4 points
       // For every permutation of 4 points run pointOnALine.
       // If it return true, increment segmentCount and create and add the line segment to the set
       // Think of how to implement the array (resizing)
       segments = new LineSegment[1];
       this.points = points;
       double firstSlope;
       double secondSlope;
       for (int i = 0; i < points.length-3; i++) {
           for (int j = i+1; j < points.length-2; j++) {
               firstSlope = points[i].slopeTo(points[j]); 
               for (int k = j+1; k < points.length-1; k++) {
               // If not conlinear continue
                   secondSlope = points[j].slopeTo(points[k]);
                   if (secondSlope != firstSlope) continue;
                   for (int l = k+1; l < points.length; l++) {
                       // If not conlinear continue
                       if (secondSlope == points[k].slopeTo(points[l])) {
                           if (segmentCount == segments.length) resize(2*segments.length);
                           //StdOut.printf("%d, %d, %d, %d\n", i, j, k, l);
                           int[] startAndEndIndex = getStartAndEndIndex(i, j, k, l);
                           segments[segmentCount++] = new LineSegment(points[startAndEndIndex[0]], points[startAndEndIndex[1]]);
                       }                       
                  }           
               } 
           }           
       }
       
   }
   
   private int[] getStartAndEndIndex(int i, int j, int k, int l) {
       int[] startAndEndIndex = new int[2];
       startAndEndIndex[0] = i;
       startAndEndIndex[1] = i;
       
       updateIndices(startAndEndIndex, j);
       updateIndices(startAndEndIndex, k);
       updateIndices(startAndEndIndex, l);
       
       return startAndEndIndex;
   }
   
   private void updateIndices(int[] startAndEndIndex, int index) {
       if (points[startAndEndIndex[0]].compareTo(points[index]) < 0) startAndEndIndex[0] = index;
       if (points[startAndEndIndex[1]].compareTo(points[index]) > 0) startAndEndIndex[1] = index;
   }
   
   public int numberOfSegments() {
       // the number of line segments
       return segmentCount;
   }
   
   public LineSegment[] segments() {
       // the line segments
       resize(segmentCount);
       return segments;
   }
   
   private void resize(int capacity) {
       LineSegment[] copy = new LineSegment[capacity];
       for (int i = 0; i < segmentCount; i++) {
           copy[i] = segments[i];
       }
       segments = copy;
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