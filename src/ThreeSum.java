import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author Martijn Sparnaaij
 * @date 16-08-2018
 */
public class ThreeSum {
    
    public static int countZeroSum(int[] numbers) {
        Arrays.sort(numbers); // N^2 or better -> ~N^2
        int count = 0; // constant -> ~1
        int twoSum; // constant -> ~1
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i+1; j < numbers.length - 1; j++) {
                twoSum = -(numbers[i] + numbers[j]); //N*(N-1)/2! -> ~N^2
                if (isNumberInArray(twoSum, j, numbers)) { // ~log(N)
                    count++;
                }
            }
        }        
        return count;
    }
    
    private static boolean isNumberInArray(int number, int currentIndex, int[] numberArray) {
        int index = doBinarySearch(numberArray, number, currentIndex + 1);
        return index >= 0;
    }
    
    private static int doBinarySearch(int[] array, int key, int lowerBound) {
        int low = lowerBound;
        int high = array.length - 1;
        
        while (low <= high) {
            int mid = (low + high) / 2;
            if (array[mid] < key) {
                low = mid + 1;
            } else if (array[mid] > key) {
                high = mid - 1;
            } else if (array[mid] == key) {
                return mid;
            }
        }
        return -1;
    }
     
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] numbers = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            numbers[i] = Integer.parseInt(args[i]);
        }
        int count = ThreeSum.countZeroSum(numbers);
        StdOut.printf("Count = %d\n", count);          
    }

}
