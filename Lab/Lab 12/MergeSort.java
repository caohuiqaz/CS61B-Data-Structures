import edu.princeton.cs.algs4.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MergeSort {
    
    private static <Item extends Comparable> Item getMin(Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }
private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> wrapperQueue = new Queue<>();
        for (Item i : items) {
            Queue<Item> singleItemQueue = new Queue<>();
            singleItemQueue.enqueue(i);
            wrapperQueue.enqueue(singleItemQueue);
        }
        return wrapperQueue;
    }

    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> mergedQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            mergedQueue.enqueue(getMin(q1, q2));
        }
        return mergedQueue;
    }
 public static <Item extends Comparable> Queue<Item> mergeSort(Queue<Item> items) {
        Queue<Queue<Item>> helperQueue = makeSingleItemQueues(items);
        while (helperQueue.size() != 1) {
            Queue<Item> first = helperQueue.dequeue();
            Queue<Item> second = helperQueue.dequeue();
            Queue<Item> mergedQueue = mergeSortedQueues(first, second);
            helperQueue.enqueue(mergedQueue);
        }
        return helperQueue.dequeue();
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
    public void testMergeSort() {
        Queue<Integer> students = new Queue<>();
        int testNum = 10000;
        for (int i = 0; i < testNum; i++) {
            students.enqueue(StdRandom.uniform(100));
        }
        Queue<Integer> sortedStudent = mergeSort(students);
        assertTrue(isQueueSorted(sortedStudent));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(MergeSort.class);
    }
}
