import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
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
	
	private int[] tiles;
	private final int dimension;
	private int manhattan;
	private Board twin;
		
    public Board(int[][] blocks) {
    	// construct a board from an n-by-n array of blocks
    	// (where blocks[i][j] = block in row i, column j)
    	if (blocks == null) throw new IllegalArgumentException("Input cannot be null.");
    	
    	dimension = blocks[0].length;
    	tiles = new int[dimension*dimension + 1]; // Do not use the zero
    	tiles[0] = -1;
    	for (int i = 0; i < dimension; i++) {
        	for (int j = 0; j < dimension; j++) {
        		tiles[rowColToIndex(i,j)] = blocks[i][j];
        	}    		
    	}
    	manhattan = manhattanCalc();
    }
                           
    private Board(int[] tiles, int dimension) {
    	this.dimension = dimension;
    	this.tiles = tiles;
    	manhattan = manhattanCalc();
    }
    
    public int dimension() {
    	// board dimension n
    	return dimension;
    }
    
    public int hamming() {
    	// number of blocks out of place
    	int outOfPlaceCount = 0;
    	for (int i = 1; i < tiles.length; i++) {
    		if (tiles[i] != i && tiles[i] != 0) outOfPlaceCount++;
    	}    	
    	return outOfPlaceCount;
    }
    
    public int manhattan() {
    	return manhattan;
    }
    
    private int manhattanCalc() {
    	// sum of Manhattan distances between blocks and goal
    	int manhattanSum = 0;
    	for (int i = 1; i < tiles.length; i++) {
			if (tiles[i] != 0) manhattanSum += stepsToLoc(i);
    	}
    	return manhattanSum;
    }
        
    public boolean isGoal() {
    	// is this board the goal board?
    	return hamming() == 0;
    }
    
    public Board twin() {
    	// a board that is obtained by exchanging any pair of blocks
    	if (twin == null) twin = createTwin();
    	return twin;
    }
    
    private Board createTwin() {
      	int[] newTiles = tiles.clone();
    	int firstIndex = getRandomIndex();
    	while (!isBlock(firstIndex)) {
    		firstIndex = getRandomIndex();
    	}
    	int secondIndex = getRandomIndex();
    	while (!isBlock(secondIndex) || secondIndex == firstIndex) {
    		secondIndex = getRandomIndex();
    	}
    	
    	switchTiles(newTiles, firstIndex, secondIndex);
    	
    	return new Board(newTiles, dimension);
    }
    
    private int getRandomIndex() {
    	return StdRandom.uniform(dimension*dimension-1) + 1;
    }
    
    private boolean isBlock(int index) {
    	return tiles[index] != 0;
    }
    
    private void switchTiles(int[] tiles, int firstIndex, int secondIndex) {
    	 int firstValue = tiles[firstIndex];
    	 int secondValue = tiles[secondIndex];
    	 tiles[firstIndex] = secondValue;
    	 tiles[secondIndex] = firstValue;
    }
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	Board that;
    	if (y instanceof Board) {
			that = (Board) y;
		} else {
			return false;
		}
    	
    	return compare(that);
    }    

    public Iterable<Board> neighbors() {
    	// all neighboring boards
    	Queue<Board> neighbours = new Queue<>();    	
    	int emptyTileIndex = getEmptyTileIndex();
    	int baseRow = getRow(emptyTileIndex);
    	int baseCol = getCol(emptyTileIndex);
    	
    	if (baseRow > 0) neighbours.enqueue(getSwitchedBoard(baseRow, baseCol, baseRow - 1, baseCol));
    	if (baseCol > 0) neighbours.enqueue(getSwitchedBoard(baseRow, baseCol, baseRow, baseCol - 1));
    	if (baseRow < dimension - 1) neighbours.enqueue(getSwitchedBoard(baseRow, baseCol, baseRow + 1, baseCol));
    	if (baseCol < dimension - 1) neighbours.enqueue(getSwitchedBoard(baseRow, baseCol, baseRow, baseCol + 1));
    	
    	return neighbours;
    }
    
    public String toString() {
    	// string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 1; i < tiles.length; i++) {
            s.append(String.format("%2d ", tiles[i]));
            if (i % dimension == 0) s.append("\n");
        }
        return s.toString();
    }
    
    private int stepsToLoc(int index) {
    	int tile = tiles[index];
    	return Math.abs(getRow(tile) - getRow(index)) + Math.abs(getCol(tile) - getCol(index));    	
    }

    private int getRow(int index) {
    	return (index - 1)/dimension;
    }
    
    private int getCol(int index) {
    	return (index - 1)%dimension;
    }
        
    private int rowColToIndex(int row, int col) {
    	return row*dimension + col + 1;
    }
    
    private boolean compare(Board that) {
    	if (dimension != that.dimension) return false;
    	for (int i = 1; i < tiles.length; i++) {
    		if (tiles[i] != that.tiles[i]) return false;
    	}    	    	
    	return true;
    }
    
    private Board getSwitchedBoard(int row, int col, int otherRow, int otherCol) {
    	int[] newTiles = tiles.clone();
    	int firstIndex = rowColToIndex(row, col);
    	int secondIndex = rowColToIndex(otherRow, otherCol);
    	
    	switchTiles(newTiles, firstIndex, secondIndex);
    	
    	return new Board(newTiles, dimension);
    }
    
    private int getEmptyTileIndex() {
    	for (int i = 1; i < tiles.length; i++) {
    		if (tiles[i] == 0) return i;
    	}
    	return -1;
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
                
        StdOut.println("Neighbouring boards:");
        Iterable<Board> neighbours = initial.neighbors();
        for (Board board: neighbours) {
        	StdOut.println(board.toString());
        }
        
    }
}
