package com.logsentinel;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Serializer performing symmetric encryption based on a nested original serializer's output 
 * 
 * @author bozho
 *
 */
public class EncryptingBodySerializer implements BodySerializer {

    private static final String ALGORITHM = "AES";
    
    private byte[] encryptionKey;
    private BodySerializer originalSerializer;
    public EncryptingBodySerializer(byte[] encryptionKey, BodySerializer originalSerializer) {
        this.encryptionKey = encryptionKey;
        this.originalSerializer = originalSerializer;
    }
    @Override
    public String serialize(Object object) {
        String content = originalSerializer.serialize(object);
        try {
            Key key = new SecretKeySpec(encryptionKey, ALGORITHM);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(content.getBytes(Charset.forName("UTF-8")));
            return Base64.getEncoder().encodeToString(encValue);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to perform symmetric encryption", ex);
        }
        
    }
    
}
