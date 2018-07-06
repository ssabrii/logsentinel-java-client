package com.logsentinel.merkletree.verification;

import com.logsentinel.merkletree.utils.ArrayUtils;
import com.logsentinel.merkletree.utils.CryptoUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Merkle consistency proof verification
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class ConsistencyProofVerifier {

    /**
     * Verifies consistency between two tree heads
     *
     * @param consistencyProof consistency proof for a Merkle Tree Hash (MTH) and a previously advertised hash (MTH)
     *                         of the first m leaves
     * @param firstHash first Merkle Tree Hash (MTH)
     * @param treeSizeFirst tree size of first Merkle Tree Hash (MTH), 0 &lt; treeSizeFirst &lt; treeSizeSecond
     * @param secondHash second Merkle Tree Hash (MTH)
     * @param treeSizeSecond tree size of second Merkle Tree Hash (MTH), 0 &lt; treeSizeFirst &lt; treeSizeSecond
     * @return verification status
     */
    public static Boolean verify(List<byte[]> consistencyProof, byte[] firstHash, int treeSizeFirst,
                              byte[] secondHash, int treeSizeSecond) {
        if (treeSizeFirst > treeSizeSecond) {
            // first tree size greater than second tree size, fail verification
            return false;
        }

        if (Arrays.equals(firstHash, secondHash) && treeSizeFirst == treeSizeSecond) {
            // first MTH = second MTH, same tree
            return true;
        }

        if (consistencyProof.size() == 0) {
            // consistency proof empty, fail verification
            return false;
        }

        if ((treeSizeFirst & (treeSizeFirst - 1)) == 0) {
            // first tree size is a an exact power of 2, prepend first MTH to consistency path
            consistencyProof.add(0, firstHash);
        }

        int fn = treeSizeFirst - 1;
        int sn = treeSizeSecond - 1;

        if ((fn & ((1 << 1) - 1)) == 1) {
            FnSn fnSn = rightShiftFnSnUntilLsbFnUnset(fn, sn);
            fn = fnSn.getFn();
            sn = fnSn.getSn();
        }

        byte[] fr = consistencyProof.get(0);
        byte[] sr = consistencyProof.get(0);

        for (byte[] c : consistencyProof.subList(1, consistencyProof.size())) {
            if (sn == 0) {
                return false;
            }

            if (fr == null || sr == null) {
                return false;
            }

            if (fn == sn || (fn & ((1 << 1) - 1)) == 1) {
                fr = CryptoUtils.hash(ArrayUtils.addByteToArray(ArrayUtils.concat(c, fr), (byte) 0x01));
                sr = CryptoUtils.hash(ArrayUtils.addByteToArray(ArrayUtils.concat(c, sr), (byte) 0x01));

                if ((fn & ((1 << 1) - 1)) == 0) {
                    FnSn fnSn = rightShiftFnSnUntilLsbFnSetOrFnZ(fn, sn);
                    fn = fnSn.getFn();
                    sn = fnSn.getSn();
                }
            } else {
                sr = CryptoUtils.hash(ArrayUtils.addByteToArray(ArrayUtils.concat(sr, c), (byte) 0x01));

            }

            fn = fn >> 1;
            sn = sn >> 1;
        }

        return Arrays.equals(fr, firstHash) && Arrays.equals(sr, secondHash) && sn == 0;

    }

    /**
     * Right-shifts fn and sn equally until LSB(fn) is unset
     */
    private static FnSn rightShiftFnSnUntilLsbFnUnset(int fn, int sn) {
        do {
            fn = fn >> 1;
            sn = sn >> 1;
        }
        while ((fn & ((1 << 1) - 1)) == 1);

        FnSn fnSn = new FnSn();
        FnSn.setFn(fn);
        FnSn.setSn(sn);

        return fnSn;
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
