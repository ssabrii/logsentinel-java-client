package com.logsentinel.merkletree.utils;

/**
 * Array utilities
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class ArrayUtils {

    /**
     * Prepends a byte to a byte array
     *
     * @param bArray byte array to be prepended
     * @param newByte byte to prepend to the byte array
     * @return prepended array
     */
    public static byte[] addByteToArray(byte[] bArray, byte newByte) {
        byte[] newArray = new byte[bArray.length + 1];

        System.arraycopy(bArray, 0, newArray, 1, bArray.length);
        newArray[0] = newByte;

        return newArray;
    }

    /**
     * Concatenates two byte arrays
     *
     * @param bArray1 byte array #1
     * @param bArray2 byte array #2
     * @return concatenated array
     */
    public static byte[] concat(byte[] bArray1, byte[] bArray2) {
        byte[] arr = new byte[bArray1.length + bArray2.length];

        System.arraycopy(bArray1, 0, arr, 0, bArray1.length);
        System.arraycopy(bArray2, 0, arr, bArray1.length, bArray2.length);

        return arr;
    }
}
