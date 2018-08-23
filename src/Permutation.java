import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 * @date 17-08-2018
 *  
 */
public class Permutation {
    
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String value = StdIn.readString();
            randomQueue.enqueue(value);            
        }
            
        for( int i = 0; i < n; i++) {
            StdOut.println(randomQueue.dequeue());
        }        
    }
    
}
