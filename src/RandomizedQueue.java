import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Array implementation of RandomizedQueue
 * Must be array because the worse-case must be in constant amortized time, in 
 * case of an linked-list implementation this would be constant. 
 */

/**
 * @author Martijn Sparnaaij
 * @date 17-08-2018
 *  
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] itemArray;
    private int size = 0;
    
    public RandomizedQueue() {
        // construct an empty randomized queue
        itemArray = (Item[]) new Object[1];
    }
    
    private RandomizedQueue(RandomizedQueue<Item> source) {
        this.itemArray = source.itemArray.clone();
        this.size = source.size;
    }
    
    public boolean isEmpty() {
        // is the randomized queue empty?
        return size == 0;
    }
    
    public int size() {
        // return the number of items on the randomized queue
        return size;
    }
    
    public void enqueue(Item item) {        
        if (item == null) throw new IllegalArgumentException("The item cannot be null.");
        // add the item
        if (size == itemArray.length) resize(2*itemArray.length);
        itemArray[size++] = item;
    }
    
    public Item dequeue() {
        // remove and return a random item
        checkForEmpty();
        
        int index = getRandomPosition();
        Item temp = itemArray[index];
        itemArray[index] = itemArray[size - 1];
        itemArray[size - 1] = temp;
        
        Item item = itemArray[size - 1];
        itemArray[size - 1] = null;
        size--;
        if (size > 0 && size == itemArray.length/4) resize(itemArray.length/2);
        return item;
    }
    
    public Item sample() {
        // return a random item (but do not remove it)
        checkForEmpty();
        return itemArray[getRandomPosition()];
    }
    
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomQueueIterator(this);
    }
    
    
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = itemArray[i];
        }
        itemArray = copy;
    }
    
    private int getRandomPosition() {
        return StdRandom.uniform(size());
    }
    
    private void checkForEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("You cannot dequeue from empty queue");
        }
    }
    
    private class RandomQueueIterator implements Iterator<Item> {
                
        private int currentIndex;
        private Item[] iteratorArray;
        
        public RandomQueueIterator(RandomizedQueue<Item> randomQueue) {
            currentIndex = 0;
            iteratorArray = (Item[]) new Object[randomQueue.size()];
            RandomizedQueue<Item> tempQueue = new RandomizedQueue<Item>(randomQueue);
            int index = 0;
            while (!tempQueue.isEmpty()) {
                iteratorArray[index++] = tempQueue.dequeue();                
            }            
        }
        
        public boolean hasNext() { 
            return currentIndex < size;
        }
        
        public void remove() { 
            throw new UnsupportedOperationException("The remove method is not supported."); 
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("The queue is empty.");
            Item item = iteratorArray[currentIndex];
            currentIndex++;
            return item;
        }
    }
    
    private static void testIterator(RandomizedQueue<Integer> randomQueue, int number) {
        Iterator<Integer> iter = randomQueue.iterator();
        StdOut.printf("Iterator %2d = ", number);
        while (iter.hasNext()) {
            StdOut.printf("%d, ", iter.next());    
        }
        StdOut.printf("\n");
    }
    
    public static void main(String[] args) {
        // unit testing (optional)
        RandomizedQueue<Integer> randomQueue = new RandomizedQueue<>();
        StdOut.printf("Isempty = %b\n", randomQueue.isEmpty());
        randomQueue.enqueue(0);
        StdOut.printf("Size = %d\n", randomQueue.size());
        randomQueue.enqueue(1);
        StdOut.printf("Size = %d\n", randomQueue.size());
        randomQueue.enqueue(2);
        StdOut.printf("Size = %d\n", randomQueue.size());
        StdOut.printf("Sample = %d\n", randomQueue.sample());
        StdOut.printf("Sample = %d\n", randomQueue.sample());
        StdOut.printf("Sample = %d\n", randomQueue.sample());
        StdOut.printf("Sample = %d\n", randomQueue.sample());
        StdOut.printf("Dequeue = %d\n", randomQueue.dequeue());
        randomQueue.enqueue(3);  
        
        for (int i = 0; i < 16; i++) {
            testIterator(randomQueue, i);
        }
    } 
    
}
