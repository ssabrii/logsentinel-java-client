package com.logsentinel.merkletree.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Crypto utilities
 *
 * @author Stefan Genchev
 * @author https://stefan.genchev.io/
 * @version 1.0
 */
public class CryptoUtils {

    /**
     * Generates a cryptographic hash (SHA-256) over a byte array
     *
     * @param d byte array to hash
     * @return cryptographic hash
     */
    public static byte[] hash(byte[] d) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            return digest.digest(d);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
