package com.logsentinel.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtil {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String encrKey = "1234567890123456789012";

    public static String encrypt(String data, byte[] encryptionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Key key = new SecretKeySpec(Base64.getDecoder().decode(encrKey), "AES");
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(data.getBytes(Charset.forName("UTF-8")));
        return Base64.getEncoder().encodeToString(encValue);
    }
}
