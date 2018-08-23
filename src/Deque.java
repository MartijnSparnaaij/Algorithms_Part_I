import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

/**
 * Linked-list implementation of deque
 * Must be linked-list because the worse-case time must be constant, in 
 * case of an array implementation this would be 3N 
 */

/**
 * @author Martijn Sparnaaij
 * @date 17-08-2018
 */
public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last; // 2*8=16
    private int count = 0; // 8
    
    private class Node { // 16 + 8 + 8 + 8 + 8 = 48
        Item item; // 8
        Node next; // 8
        Node previous; // 8
    }
    
    public boolean isEmpty() {
        // is the deque empty?
        return first == null;
    }
 
    public int size() {
        // return the number of items on the deque
        return count;
    }
    
    public void addFirst(Item item) {
        // add the item to the front
        checkForNull(item);
        
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (oldFirst != null) oldFirst.previous = first;
        
        if (count == 0) last = first;
        
        count++;                
    }
    
    public void addLast(Item item) {
        // add the item to the end
        checkForNull(item);
        
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (oldLast != null) oldLast.next = last;
        
        if (count == 0) first = last;
        
        count++;        
    }
    
    public Item removeFirst() {
        // remove and return the item from the front
        checkForNoElement();
        Item item = first.item;
        first = first.next;
        if (first != null) first.previous = null;
        
        count--;
        if (count == 0) {
            first = null;
            last = null;
        }   

        if (count == 1) last = first;
        return item;
    }
    
    public Item removeLast() {
        // remove and return the item from the end
        checkForNoElement();
        Item item = last.item;
        last = last.previous;
        if (last != null) last.next = null;
        
        count--;

        if (count == 0) {
            first = null;
            last = null;
        }
        if (count == 1) first = last;
        return item;
    }
    
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }
    
    private void checkForNull(Item item) {
        if (item == null) throw new IllegalArgumentException("The item cannot be null.");        
    }
    
    private void checkForNoElement() {
        if (isEmpty()) throw new NoSuchElementException("The deque is empty.");        
    }
    
    private class DequeIterator implements Iterator<Item> {
        
        private Node current = first;
        
        public boolean hasNext() { 
            return current != null;
        }
        
        public void remove() { 
            throw new UnsupportedOperationException("The remove method is not supported."); 
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("The deque is empty.");
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    public static void main(String[] args) {
        // unit testing (optional)
        Deque<String> deque = new Deque<>();
        StdOut.printf("Isempty = %b\n", deque.isEmpty());
        deque.addFirst("1");
        StdOut.printf("Size = %d\n", deque.size());
        deque.removeLast();
        
        deque.addLast("1");
        deque.removeFirst();
        
        
        deque.addFirst("2");
        StdOut.printf("Size = %d\n", deque.size());
        deque.addLast("0");
        StdOut.printf("Size = %d\n", deque.size());        
        
        Deque<Integer> dequeInt = new Deque<Integer>();
        dequeInt.addFirst(1);
        dequeInt.removeFirst();
        dequeInt.addFirst(3);
        dequeInt.removeLast();
        dequeInt.addLast(5);
        dequeInt.addFirst(6);
        dequeInt.addLast(7);
        dequeInt.removeLast();
        Iterator<Integer> iter = dequeInt.iterator();
        int elCount = 0;
        while (iter.hasNext()) {
            StdOut.printf("%d, ", iter.next());
            elCount++;
        }
        StdOut.printf("\nSize = %d\n", elCount);
        
    }

}
