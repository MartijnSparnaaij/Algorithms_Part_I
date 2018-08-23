
/**
 * 
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author Martijn Sparnaaij
 * @date 13 aug. 2018
 *
 */

public class Percolation {

    private final int n; // Number of rows and columns
    private final WeightedQuickUnionUF wquUF, wquUFIsFull;
    private boolean[] siteStatus;
    private int openSiteCount;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be an integer larger than 0");

        openSiteCount = 0;
        this.n = n;
        siteStatus = new boolean[n*n];
        wquUF = new WeightedQuickUnionUF(n*n + 2);
        wquUFIsFull = new WeightedQuickUnionUF(n*n + 1);
    }

    public void open(int row, int col) {
        checkIfRangeIsValid(row, col);
        if (isOpen(row, col)) return;

        int siteIndex = rowColTo1D(row, col);
        siteStatus[siteIndex] = true;
        openSiteCount++;
        // Open -> perform unions with all open sites around the current site
        connectToSite(row - 1, col, siteIndex);
        connectToSite(row + 1, col, siteIndex);
        connectToSite(row, col - 1, siteIndex);
        connectToSite(row, col + 1, siteIndex);
        
        // Special cases: Top row, also include virtual top site, bottom row ....
        if (row == 1) {
            wquUF.union(rowColTo1D(row, col), n*n);
            wquUFIsFull.union(rowColTo1D(row, col), n*n);
        }
        if (row == n) {
            wquUF.union(rowColTo1D(row, col), n*n + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        checkIfRangeIsValid(row, col);
        return siteStatus[rowColTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        checkIfRangeIsValid(row, col);
        return wquUFIsFull.connected(rowColTo1D(row, col), n*n);
    }

    public int numberOfOpenSites() {
        return openSiteCount;
    }

    public boolean percolates() {
        return wquUF.connected(n*n, n*n + 1);
    }

    private int rowColTo1D(int row, int col) {
        return n*(row - 1) + col - 1;
    }

    private void checkIfRangeIsValid(int row, int col) {
        if (!isRangeValid(row, col))
            throw new IllegalArgumentException("row or column index is invalid");
    }

    private boolean isRangeValid(int row, int col) {
        return row > 0 && row <= n && col > 0 && col <= n;
    }

    private void connectToSite(int row, int col, int siteIndex) {
        if (!isRangeValid(row, col))
            return;
        if (isOpen(row, col)) {
            wquUF.union(rowColTo1D(row, col), siteIndex);
            wquUFIsFull.union(rowColTo1D(row, col), siteIndex);
        }
    }
}
