import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class QuickSort {
   
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

  
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    
    private static <Item extends Comparable> void partition(Queue<Item> unsorted, Item pivot, Queue<Item> less,
            Queue<Item> equal, Queue<Item> greater) {
        for (Item i : unsorted) {
            if (i.compareTo(pivot) < 0) less.enqueue(i);
            else if (i.compareTo(pivot) == 0) equal.enqueue(i);
            else greater.enqueue(i);
        }
    }

    public static <Item extends Comparable> Queue<Item> quickSort(Queue<Item> items) {
        if (items.size() == 1 || items.size() == 0) return items;
        Item pivot = getRandomItem(items);
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        partition(items, pivot, less, equal, greater);
        Queue<Item> sortedLess = quickSort(less);
        Queue<Item> sortedGreater = quickSort(greater);
        Queue<Item> resultTemp = catenate(sortedLess, equal);
        return catenate(resultTemp, sortedGreater);
    }

    private <Item extends Comparable> boolean isQueueSorted(Queue<Item> q) {
        Item first = null;
        for (Item i : q) {
            if (first != null && first.compareTo(i) > 0) {
                return false;
            }
            first = i;
        }
        return true;
    }

    @Test
    public void testQuickSort() {
        Queue<Integer> students = new Queue<>();
        int testNum = 10000;
        for (int i = 0; i < testNum; i++) {
            students.enqueue(StdRandom.uniform(100));
        }
        Queue<Integer> sortedStudent = quickSort(students);
		assertTrue(isQueueSorted(sortedStudent));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(QuickSort.class);
    }
}
