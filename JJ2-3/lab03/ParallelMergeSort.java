package lab03;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

/**
 * This class extends RecursiveTask to perform parallel merge sort on an integer array.
 *
 * It represents a parallel implementation of the merge sort algorithm using the Fork/Join framework.
 * It extends the RecursiveTask class, allowing the sorting task to be split into subtasks and executed
 * concurrently on multiple threads.
 *
 * The core logic involves dividing the array into two halves, recursively
 * sorting each half, and then merging the sorted halves back together.
 */
public class ParallelMergeSort extends RecursiveTask<int[]> {

    private final int[] array;

    public ParallelMergeSort(int[] array) {
        this.array = array;
    }

    @Override
    protected int[] compute() {
        if (array.length <= 10) {
            return mergeSort(array);
        } else {

            int midpoint = array.length / 2;
            int[] left = Arrays.copyOfRange(array, 0, midpoint);
            int[] right = Arrays.copyOfRange(array, midpoint, array.length);

            ParallelMergeSort leftTask = new ParallelMergeSort(left);
            ParallelMergeSort rightTask = new ParallelMergeSort(right);

            leftTask.fork();
            rightTask.fork();

            int[] leftResult = leftTask.join();
            int[] rightResult = rightTask.join();

            return merge(leftResult, rightResult);
        }
    }

    private int[] mergeSort(int[] array) {
        if (array.length <= 1) {
            return array;
        }

        int midpoint = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, midpoint);
        int[] right = Arrays.copyOfRange(array, midpoint, array.length);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    /**
     * Merges two sorted arrays (left array + right array) into a single sorted array.
     */
    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];

        int i = 0; // index left array
        int j = 0; // index right array
        int k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k] = left[i];
                i++;
            } else {
                result[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < left.length) {
            result[k] = left[i];
            i++;
            k++;
        }

        while (j < right.length) {
            result[k] = right[j];
            j++;
            k++;
        }
        return result;
    }
}
