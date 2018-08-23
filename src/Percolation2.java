
/**
 * 
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author Martijn Sparnaaij
 * @date 15 aug. 2018
 *
 */

public class Percolation2 {

    private static final byte BLOCKED = 0;
    private static final byte OPEN = 1;
    private static final byte CONNECTED_TO_TOP_ROW = 2;
    private static final byte CONNECTED_TO_BOTTOM_ROW = 4;
    
    private int N; // Number of rows and columns
    private WeightedQuickUnionUF wquUF;
    private byte[] siteStatus;
    private int openSiteCount;
    private boolean percolates;

    private boolean connectedToTop;
    private boolean connectedToBottom;
    
    public Percolation2(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be an integer larger than 0");

        openSiteCount = 0;
        N = n;
        siteStatus = new byte[N*N];
        wquUF = new WeightedQuickUnionUF(N*N);        
    }

    public void open(int row, int col) {
        checkIfRangeIsValid(row, col);
        if (isOpen(row, col)) return;

        int siteIndex = rowColTo1D(row, col);

        connectedToTop = false;
        connectedToBottom = false;
        if (row == 1) {
            siteStatus[siteIndex] = CONNECTED_TO_TOP_ROW;
            connectedToTop = true;
        } else if (row == N) {
            siteStatus[siteIndex] = CONNECTED_TO_BOTTOM_ROW;
            connectedToBottom = true;
        } else {
            siteStatus[siteIndex] = OPEN;
        }
        openSiteCount++;
        // Open -> perform unions with all open sites around the current site
        connectToSite(row - 1, col, siteIndex);
        connectToSite(row + 1, col, siteIndex);
        connectToSite(row, col - 1, siteIndex);
        connectToSite(row, col + 1, siteIndex);
        
        if (connectedToTop) setParentStatus(siteIndex, CONNECTED_TO_TOP_ROW);
        else if (connectedToBottom) setParentStatus(siteIndex, CONNECTED_TO_BOTTOM_ROW);
        
        if (connectedToTop && connectedToBottom) {
            percolates = true;
        }        
    }

    public boolean isOpen(int row, int col) {
        checkIfRangeIsValid(row, col);
        return siteStatus[rowColTo1D(row, col)] != BLOCKED;
    }

    public boolean isFull(int row, int col) {
        checkIfRangeIsValid(row, col);
        return siteStatus[wquUF.find(rowColTo1D(row, col))] == CONNECTED_TO_TOP_ROW;
    }

    public int numberOfOpenSites() {
        return openSiteCount;
    }

    public boolean percolates() {
        return percolates;
    }

    private int rowColTo1D(int row, int col) {
        return N * (row - 1) + col - 1;
    }

    private void checkIfRangeIsValid(int row, int col) {
        if (!isRangeValid(row, col))
            throw new IllegalArgumentException("row or column index is invalid");
    }

    private boolean isRangeValid(int row, int col) {
        return row > 0 && row <= N && col > 0 && col <= N;
    }
    
    private void connectToSite(int row, int col, int siteIndex) {
        if (!isRangeValid(row, col))
            return;
        if (isOpen(row, col)) {
            byte otherStatus = siteStatus[wquUF.find(rowColTo1D(row, col))];
            wquUF.union(rowColTo1D(row, col), siteIndex);            
            if (otherStatus == CONNECTED_TO_TOP_ROW) connectedToTop = true;
            else if (otherStatus == CONNECTED_TO_BOTTOM_ROW) connectedToBottom = true;
        }
    }
    
    private void setParentStatus(int siteIndex, byte status) {
        siteStatus[wquUF.find(siteIndex)] = status;
    }

    public static void main(String[] args) {

    }

}
