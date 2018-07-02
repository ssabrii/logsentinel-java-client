package com.logsentinel.merkletree.generation;

import com.logsentinel.merkletree.model.TreeLeaf;
import com.logsentinel.merkletree.utils.ArrayUtils;
import com.logsentinel.merkletree.utils.CryptoUtils;
import com.logsentinel.merkletree.utils.MathUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Merkle Tree Hash / Head (MTH) generation
 * MODIFIED FOR CLIENT
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class TreeHeadGeneration {

    /***
     * The hash of an empty string
     */
    private static final byte[] EMPTY_STRING_HASH = Base64.getDecoder().decode("47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=");

    /**
     * Returns a Merkle Tree Hash (MTH)
     *
     * @param dN list of data entries; these entries will be hashed to form the leaves of the Merkle Hash Tree.
     * @return a single Merkle tree hash
     */
    public static byte[] getMth(List<TreeLeaf> dN) {
        // k - the largest power of two smaller than n (i.e., k < n <= 2k)
        int k = MathUtils.calculateK(dN.size());

        if (dN.size() == 0) {
            //  The hash of an empty list is the hash of an empty string
            return EMPTY_STRING_HASH;
        } else if (dN.size() == 1) {
            // The hash of a list with one entry (also known as a leaf hash) is HASH(0x00 || d[0])
            return CryptoUtils.hash(ArrayUtils.addByteToArray(dN.get(0).getData(), (byte) 0x00));
        }

        // D[0:k]
        List<TreeLeaf> newEntries = new ArrayList<TreeLeaf>();

        for (int i = 0; i <= k - 1; i++) {
            newEntries.add(dN.get(i));
        }
        // Generate MTH(D[0:k])
        byte[] hash1 = getMth(newEntries);

        // D[k:n]
        List<TreeLeaf> newEntries2 = new ArrayList<TreeLeaf>();

        for (int i = k; i <= dN.size() - 1; i++) {
            newEntries2.add(dN.get(i));
        }

        // Generate MTH(D[k:n]
        byte[] hash2 = getMth(newEntries2);

        // The Merkle Tree Hash of an n-element list D_n is defined recursively
        // as HASH(0x01 || MTH(D[0:k]) || MTH(D[k:n]))
        return CryptoUtils.hash(ArrayUtils.addByteToArray(ArrayUtils.concat(hash1, hash2), (byte) 0x01));
    }
}
