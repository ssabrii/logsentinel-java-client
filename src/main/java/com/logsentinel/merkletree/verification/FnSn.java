package com.logsentinel.merkletree.verification;

/**
 * Object holding the fn and sn parameters that are used for verification
 *
 * Fn and sn are both used as a temporary index when building subtrees during verification.
 * See the Merkle tree documentation for more information.
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
class FnSn {
    private static int fn;
    private static int sn;

    static int getFn() {
        return fn;
    }

    static int getSn() {
        return sn;
    }

     static void setFn(int fnC) {
        fn = fnC;
    }

    static void setSn(int snC) {
        sn = snC;
    }
}
