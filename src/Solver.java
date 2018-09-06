import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 * 27 aug. 2018
 *
 */

public class Solver {
	
	private MinPQ<Move> minMovePQ;  
	private MinPQ<Move> minMovePQtwin;
	private boolean isSolvable = false;
	private Stack<Board> solution;
	private int moveCount = -1;
	
    public Solver(Board initial) {
    	// find a solution to the initial board (using the A* algorithm)
    	if (initial == null) throw new IllegalArgumentException("Input cannot be null.");
    	
    	minMovePQ = new MinPQ<>();
    	minMovePQtwin = new MinPQ<>();
    	minMovePQ.insert(new Move(initial, null));
    	minMovePQtwin.insert(new Move(initial.twin(), null));
    	solve();
    	//doNextStep();
    }
    
    private void solve() {
    	while (minMovePQ.size() > 0 && minMovePQtwin.size() > 0) {
    		Move searchNode = minMovePQ.delMin();
    		Move searchNodeTwin = minMovePQtwin.delMin();
    		if (searchNode.board.isGoal()) {
    			isSolvable = true;
    			createSolutionIterable(searchNode);
    			moveCount = searchNode.moveCount;
    			break;
    		}
    		if (searchNodeTwin.board.isGoal()) {
    			break;
    		}
    		
    		addNeighbours(minMovePQ, searchNode);
    		addNeighbours(minMovePQtwin, searchNodeTwin);
    	}    	

    }
    
    private void addNeighbours(MinPQ<Move> minPQ, Move searchNode) {
	  	Iterable<Board> neighbours = searchNode.board.neighbors();
    	for (Board board: neighbours) {
    		if (searchNode.previous != null && board.equals(searchNode.previous.board)) continue;
    		minPQ.insert(new Move(board, searchNode));
    	}
    }
    
/*    private void doNextStep() {
    	Board searchNode = minMovePQ.delMin();
    	if (searchNode.isGoal()) {
			isSolvable = true;
			createSolutionIterable(searchNode);
			return;
		}
    	Iterable<Board> neighbours = searchNode.neighbors();
    	for (Board board: neighbours) {
    		if (searchNode.parent != null && board.equals(searchNode.parent)) continue;
    		minMovePQ.insert(board);
    	}
    	moves++;
    	doNextStep();
    }*/
    
    private void createSolutionIterable(Move goalNode) {
    	solution = new Stack<>();
    	
    	Move curMove = goalNode;
    	while (curMove != null) {    		
    		solution.push(curMove.board);
    		curMove = curMove.previous;
    	}
    }
    
    public boolean isSolvable() {
    	// is the initial board solvable?
    	return isSolvable;
    }
    
    public int moves() {
    	// min number of moves to solve initial board; -1 if unsolvable
    	return moveCount;
    }
    
    public Iterable<Board> solution() {
    	// sequence of boards in a shortest solution; null if unsolvable
    	return solution;
    }
    
    private class Move implements Comparable<Move> {
    	private Move previous;
    	private Board board;
    	private int moveCount = 0;
    	private int cachedPriority = -1;
    	
        public Move(Board board, Move previous) {
            this.board = board;
            this.previous = previous;
            if (previous != null) this.moveCount = previous.moveCount + 1;
        }
    	
	    public int compareTo(Move move) {
	    	if (this.priority() < move.priority()) return -1;
	    	
	    	if (this.priority() > move.priority()) return +1;

	    	return 0;
	    	
/*	    	int diffManhattan = manhattanPriority(this) - manhattanPriority(move);
	    	if (diffManhattan < 0) return -1;
	    	if (diffManhattan > 0) return 1;
	    	
	    	int diffHamming = hammingPriority(this) - hammingPriority(move);
	    	if (diffHamming < 0) return -1;
	    	if (diffHamming > 0) return 1;
	    	
	    	return 0;*/
	    }
	    private int priority() {
		    if (cachedPriority == -1) {
	            cachedPriority = moveCount + board.manhattan();
	        }
	        return cachedPriority;
	    }
	    
	    private int manhattanPriority(Move move) {
	    	return move.board.manhattan() + move.moveCount;
	    }
	    
	    private int hammingPriority(Move move) {
	    	return move.board.hamming() + move.moveCount;
	    }
    	
    }
    
    public static void main(String[] args) {
    	// solve a slider puzzle (given below)
    	In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}