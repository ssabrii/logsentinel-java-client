package com.logsentinel.merkletree.utils;

/**
 * Math utilities
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class MathUtils {
    private static final double logSubtractFromNumber = 0.01;

    /**
     * Calculates the parameter k - the largest power of 2 smaller than n (i.e., k < n <= 2k)
     *
     * @param n the parameter n that is used to calculate the parameter k
     * @return the calculated parameter k
     */
    public static int calculateK(int n) {
        int k;
        double l;

        if (n == 1) {
            return 0;
        }

        if ((Math.log(n) / Math.log(2)) % 1 == 0) {
            // n is an exact power of 2, calculate k for n - 0.01
            k = (int) Math.pow(2, (int) (Math.log(n - logSubtractFromNumber) / Math.log(2)));
        } else {
            l = (Math.log(n) / Math.log(2));

            if (l == 1) {
                k = 2;
            } else {
                k = (int) Math.pow(2, (int) (Math.log(n) / Math.log(2)));
            }
        }

        return k;
    }
}
