package lab06;

import javax.swing.*;

/**
 * This class provides methods to calculate various sequences related to Fibonacci numbers.
 */
public class FibonacciVariants {

    /**
     * Calculates the Fibonacci sequence for the specified index.
     */
    public static int fibonacci(SwingWorker<Integer, Void> w, long n) {
        if (w.isCancelled()) return -1;
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fibonacci(w, n - 1) + fibonacci(w, n - 2);
    }

    /**
     * Calculates the Tribonacci sequence for the specified index.
     */
    public static int tribonacci(SwingWorker<Integer, Void> w, long n) {
        if (w.isCancelled()) return -1;
        if (n == 0 || n == 1) return 0;
        if (n == 2) return 1;
        return tribonacci(w, n - 1) + tribonacci(w, n - 2) + tribonacci(w, n - 3);
    }

    /**
     * Calculates the Tetranacci sequence for the specified index.
     */
    public static int tetranacci(SwingWorker<Integer, Void> w, long n) {
        if (w.isCancelled()) return -1;
        if (n == 0 || n == 1 || n == 2) return 0;
        if (n == 3) return 1;
        return tetranacci(w, n - 1) + tetranacci(w, n - 2) + tetranacci(w, n - 3) + tetranacci(w, n - 4);
    }

    /**
     * Calculates the Pentanacci sequence for the specified index.
     */
    public static int pentanacci(SwingWorker<Integer, Void> w, long n) {
        if (w.isCancelled()) return -1;
        if (n == 0 || n == 1 || n == 2 || n == 3) return 0;
        if (n == 4) return 1;
        return pentanacci(w, n - 1) + pentanacci(w, n - 2) + pentanacci(w, n - 3) + pentanacci(w, n - 4) + pentanacci(w, n - 5);
    }

    /**
     * Calculates the Hexanacci sequence for the specified index.
     */
    public static int hexanacci(SwingWorker<Integer, Void> w, long n) {
        if (w.isCancelled()) return -1;
        if (n == 0 || n == 1 || n == 2 || n == 3 || n == 4) return 0;
        if (n == 5) return 1;
        return hexanacci(w, n - 1) + hexanacci(w, n - 2) + hexanacci(w, n - 3) + hexanacci(w, n - 4) + hexanacci(w, n - 5) + hexanacci(w, n - 6);
    }

    /**
     * Calculates the Octanacci sequence for the specified index.
     */
    public static int octanacci(SwingWorker<Integer, Void> w, long n) {
        if (w.isCancelled()) return -1;
        if (n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5 || n == 6) return 0;
        if (n == 7) return 1;
        return octanacci(w, n - 1) + octanacci(w, n - 2) + octanacci(w, n - 3) + octanacci(w, n - 4) + octanacci(w, n - 5) + octanacci(w, n - 6) + octanacci(w, n - 7) + octanacci(w, n - 8);
    }
}