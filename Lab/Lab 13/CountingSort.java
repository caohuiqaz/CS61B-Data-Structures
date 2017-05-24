public class CountingSort {
    public static int[] naiveCountingSort(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i] += 1;
        }
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }
        return sorted;
    }
    public static int[] betterCountingSort(int[] toSort) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i : toSort) {
            if (i > max) {
                max = i;
            }
            if (i < min) {
                min = i;
            }
        }
        int offset = min;

        int[] counts = new int[max - min + 1];
        for (int i : toSort) {
            counts[i - offset] += 1;
        }

        int[] sorted = new int[toSort.length];
        int k = 0;
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[i]; j++, k++) {
                sorted[k] = i + offset;
            }
        }

        return sorted;
    }
}
