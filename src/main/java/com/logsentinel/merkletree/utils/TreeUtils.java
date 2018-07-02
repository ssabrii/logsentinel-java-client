package com.logsentinel.merkletree.utils;

/**
 * Merkle tree utilities
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class TreeUtils {

    /**
     * Calculate the number of nodes in a Merkle inclusion proof
     *
     * @param treeSize size of the current Merkle tree
     * @param leafIndex index of the leaf for which the number of nodes in the Merkle inclusion proof is calculated
     * @return number of nodes
     */
    public static int calculateInclusionProofSize(int treeSize, int leafIndex) {
        int length = 0;
        double lastNode = treeSize - 1;
        double tmpIndex = leafIndex;

        while (lastNode > 0) {
            if (((tmpIndex % 2) > 0) || (tmpIndex < lastNode)) {
                length++;
            }
            tmpIndex = Math.floor(tmpIndex / 2);
            lastNode = Math.floor(lastNode / 2);
        }

        return length;
    }

    /**
     * Calculates the number of entries in a Merkle consistency proof
     *
     * @param firstTreeSize tree size of the previously advertised Merkle tree of the first m leaves
     * @param secondTreeSize tree size of the current Merkle Tree of size n (0 < m < n)
     * @return number of nodes
     */
    public static int calculateConsistencyProofSize(int firstTreeSize, int secondTreeSize) {
        int length = 0;
        int b = 0;
        double m = firstTreeSize;
        double n = secondTreeSize;

        while (m != n) {
            length++;

            double k = Math.pow(2, Math.floor(Math.log(n - 1) / Math.log(2)));

            if (m <= k) {
                n = k;
            } else {
                m -= k;
                n -= k;
                b = 1;
            }
        }

        length += b;

        return length;
    }
}
