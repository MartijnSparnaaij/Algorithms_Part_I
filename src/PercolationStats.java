import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 * @date 13 aug. 2018
 *
 */
public class PercolationStats {

    private final int trialCount;
    private final int n;
    private double[] percolationThresholds;

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        if (n <= 0) {
            throw new IllegalArgumentException("n should be an integer larger than 0");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trails should be an integer larger than 0");
        }
        trialCount = trials;
        this.n = n;
        percolationThresholds = new double[trialCount];
        performTrials();
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(percolationThresholds);
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(percolationThresholds);
    }

    public double confidenceLo() {
        // low endpoint of 95% confidence interval
        return mean() - (1.96*stddev())/Math.sqrt(trialCount);
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return mean() + (1.96*stddev())/Math.sqrt(trialCount);
    }

    private void performTrials() {
        Percolation percolation;
        for (int i = 0; i < trialCount; i++) {
            percolation = new Percolation(n);
            percolationThresholds[i] = performTrial(percolation);
        }
        
    }

    private double performTrial(Percolation percolation) {
        int row;
        int col;
        while (!percolation.percolates() || percolation.numberOfOpenSites() == (n*n)) {
            row = StdRandom.uniform(n) + 1;
            col = StdRandom.uniform(n) + 1;
            percolation.open(row, col);
        }
        return percolation.numberOfOpenSites()/((double) (n*n));
    }

    public static void main(String[] args) {
        // test client (described below)
        int n = Integer.parseInt(args[0]);
        int trialCount = Integer.parseInt(args[1]);
        PercolationStats perStats = new PercolationStats(n, trialCount);
        StdOut.printf("%% java-algs4 PercolationStats %d %d\n", n, trialCount);
        StdOut.printf("%-23s = %f\n", "mean", perStats.mean());
        StdOut.printf("%-23s = %f\n", "stddev", perStats.stddev());
        StdOut.printf("%-23s = [%f, %f]\n", "95% confidence interval", perStats.confidenceLo(), perStats.confidenceHi());
    }

}
