import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 * 27 aug. 2018
 *
 */

public class Board {
	
	private int[][] blocks;
	private final int dimension;
	
    public Board(int[][] blocks) {
    	// construct a board from an n-by-n array of blocks
    	// (where blocks[i][j] = block in row i, column j)
    	if (blocks == null) throw new IllegalArgumentException("Input cannot be null.");
    	
    	this.blocks = blocks;
    	dimension = blocks[0].length;
    	
    }
                                           
    public int dimension() {
    	// board dimension n
    	return dimension;
    }
    
    public int hamming() {
    	// number of blocks out of place
    	int outOfPlaceCount = 0;
    	for (int i = 0; i < dimension; i++) {
    		for (int j = 0; j < dimension; j++) {
    			if (rowColToIndex(i,j) != blocks[i][j] && blocks[i][j] != 0) {
    				outOfPlaceCount++;
    			}    			
    		}    		
    	}    	
    	return outOfPlaceCount;
    }
    
    public int manhattan() {
    	// sum of Manhattan distances between blocks and goal
    	int manhattanSum = 0;
    	for (int i = 0; i < dimension; i++) {
    		for (int j = 0; j < dimension; j++) {   
    			if (blocks[i][j] != 0) {
    				manhattanSum += stepsToLoc(i,j,blocks[i][j]);
    			}
    		}    		
    	}
    	return manhattanSum;
    }
    
    
    private int stepsToLoc(int row, int col, int index) {
    	int[] rowCol = indexToRowCol(index);
    	return Math.abs(rowCol[0] - row) + Math.abs(rowCol[1] - col);    	
    }

    private int[] indexToRowCol(int index) {
    	int row;
    	int col;
    	if (index == 0) {
    		row = dimension - 1;
    		col = dimension - 1;
    	} else {
    		row = (index - 1)/dimension;
    		col = (index - 1)%dimension;
    	}
    	
    	int[] rowCol = {row, col};
    	return rowCol;
    }
    
    private int rowColToIndex(int row, int col) {
    	return row*dimension + col + 1;
    }

    
    public boolean isGoal() {
    	// is this board the goal board?
    	return hamming() == 0;
    }
    
    public Board twin() {
    	// a board that is obtained by exchanging any pair of blocks
    	int[][] newBlocks = blocks.clone();
    	int firstIndex = getRandomIndex();
    	while (!isBlock(firstIndex)) {
    		firstIndex = getRandomIndex();
    	}
    	int secondIndex = getRandomIndex();
    	while (!isBlock(secondIndex) && secondIndex == firstIndex) {
    		secondIndex = getRandomIndex();
    	}
    	
    	switchBlocks(newBlocks, firstIndex, secondIndex);
    	
    	return new Board(newBlocks);
    }
    
    private int getRandomIndex() {
    	return StdRandom.uniform(dimension*dimension-1) + 1;
    }
    
    private boolean isBlock(int index) {
    	return getValueOnIndex(blocks, index) != 0;
    }
    
    private void switchBlocks(int[][] blocks, int firstIndex, int secondIndex) {
    	 int firstValue = getValueOnIndex(blocks, firstIndex);
    	 int secondValue = getValueOnIndex(blocks, secondIndex);
    	 setValueOnIndex(blocks, firstIndex, secondValue);
    	 setValueOnIndex(blocks, secondIndex, firstValue);
    }
    
    private int getValueOnIndex(int[][] blocks, int index) {
    	int[] rowCol = indexToRowCol(index);
    	return blocks[rowCol[0]][rowCol[1]];
    }
    
    private void setValueOnIndex(int[][] blocks, int index, int value) {
    	int[] rowCol = indexToRowCol(index);
    	blocks[rowCol[0]][rowCol[1]] = value;
    }
    
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	Board that;
    	if (y instanceof Board) {
			that = (Board) y;
		} else {
			throw new IllegalArgumentException("Object must be of the type board!");
		}
    	
    	return compare(that);
    }
    
    private boolean compare(Board that) {
    	if (dimension != that.dimension) return false;
    	for (int i = 0; i < dimension; i++) {
    		for (int j = 0; j < dimension; j++) {   
    			if (blocks[i][j] != that.blocks[i][j]) {
    				return false;
    			}
    		}
    	}    	
    	return true;
    }
    
    public Iterable<Board> neighbors() {
    	// all neighboring boards
    	return null;
    }
    
    public String toString() {
    	// string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
    	// unit tests (not graded)
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);
        int hamming =  initial.hamming();
        int manhattan =  initial.manhattan();
        StdOut.printf("Hamming: %d\n", hamming);
        StdOut.printf("Manhattan: %d\n", manhattan);
        
        StdOut.println("");
        StdOut.println(initial.toString());
        Board twin1 = initial.twin();
        StdOut.println("First twin:");
        StdOut.println(twin1.toString());
        Board twin2 = initial.twin();
        StdOut.println("Second twin:");
        StdOut.println(twin2.toString());
        
    }
}
