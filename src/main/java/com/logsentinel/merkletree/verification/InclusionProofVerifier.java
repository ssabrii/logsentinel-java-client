package com.logsentinel.merkletree.verification;

import com.logsentinel.merkletree.utils.ArrayUtils;
import com.logsentinel.merkletree.utils.CryptoUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Merkle inclusion proof verification
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class InclusionProofVerifier {

    /**
     * Verifies an inclusion proof for a given element
     *
     * @param path inclusion proof for the given element
     * @param hash hash value of the given element
     * @param leafIndex leaf index for the given element
     * @param treeSize current Merkle tree size
     * @param rootHash current Merkle Tree Hash (MTH)
     * @return verification status
     */
    public static Boolean verify(List<byte[]> path, byte[] hash, int leafIndex, int treeSize, byte[] rootHash) {
        if (leafIndex > treeSize) {
            // leaf index greater than tree size, fail the proof verification
            return false;
        }

        int fn = leafIndex;
        int sn = treeSize - 1;
        byte[] r = hash;

        for (byte[] p : path) {
            if (sn == 0) {
                // stop the iteration and fail the proof verification
                return false;
            }

            if (fn == sn || (fn & ((1 << 1) - 1)) == 1) {
                r = CryptoUtils.hash(ArrayUtils.addByteToArray(ArrayUtils.concat(p, r), (byte) 0x01));

                if ((fn & ((1 << 1) - 1)) == 0) {
                    FnSn fnSn = rightShiftFnSnUntilLsbFnSetOrFnZ(fn, sn);
                    fn = fnSn.getFn();
                    sn = fnSn.getSn();
                }
            } else {
                r = CryptoUtils.hash(ArrayUtils.addByteToArray(ArrayUtils.concat(r, p), (byte) 0x01));
            }

            sn = sn >> 1;
            fn = fn >> 1;
        }

        return sn == 0 && Arrays.equals(r, rootHash);

    }

    /**
     * Right-shifts fn and sn equally until either LSB(fn) is set or fn=0
     */
    private static FnSn rightShiftFnSnUntilLsbFnSetOrFnZ(int fn, int sn) {
        do {
            fn = fn >> 1;
            sn = sn >> 1;
        }
        while ((fn & ((1 << 1) - 1)) == 0 && fn != 0);

        FnSn fnSn = new FnSn();
        FnSn.setFn(fn);
        FnSn.setSn(sn);

        return fnSn;
    }
}
