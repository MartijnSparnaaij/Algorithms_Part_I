import java.util.Iterator;

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
public class Queue<Item> implements Iterable<Item> {

    private Item[] itemArray;
    private int size = 0;
    private int head = 0;
    private int tail = 0;
    
    
    @SuppressWarnings("unchecked")
    public Queue() {
        // construct an empty randomized queue
        itemArray = (Item[]) new Object[2];
        
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
        // add the item
        if (size == itemArray.length) resize(2*itemArray.length);
        itemArray[tail] = item;
        tail = (tail + 1) % itemArray.length; 
        size++;
    }
    
    public Item dequeue() {
        // remove and return a random item
        Item item = itemArray[head];
        itemArray[head] = null;
        head = (head + 1) % itemArray.length; 
        size--;
        if (size > 0 && size == itemArray.length/4) resize(itemArray.length/2);
        return item;
    }
    
    public Item sample() {
        // return a random item (but do not remove it)
        return null;
    }
    
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return null;
    }
    
    
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int startPoint;
        if (head < tail) {
            startPoint = head;
        } else {
            startPoint = 0;
        }
        for (int i = 0; i < startPoint + size; i++) {
            copy[i] = itemArray[i + startPoint];
        }
        tail = getNewIndex(tail, startPoint);
        head = getNewIndex(head, startPoint);
        itemArray = copy;
    }
    
    private int getNewIndex(int oldIndex, int startPoint) {
        return oldIndex - startPoint;        
    }
    
    public static void main(String[] args) {
        // unit testing (optional) 
    }
    
}