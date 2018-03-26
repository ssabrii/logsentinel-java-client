package com.logsentinel;

import com.logsentinel.util.EncryptUtil;

/**
 * Serializer performing symmetric encryption based on a nested original serializer's output
 *
 * @author bozho
 */
public class EncryptingBodySerializer implements BodySerializer {

    private byte[] encryptionKey;
    private BodySerializer originalSerializer;

    public EncryptingBodySerializer(byte[] encryptionKey, BodySerializer originalSerializer) {
        this.encryptionKey = encryptionKey;
        this.originalSerializer = originalSerializer;
    }

    @Override
    public String serialize(Object object) {
        String content = originalSerializer.serialize(object);
        return encryptConent(content, encryptionKey);

    }

    private String encryptConent(String content, byte[] encryptionKey) {
        try {
            return EncryptUtil.encrypt(content, encryptionKey);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to perform symmetric encryption", ex);
        }
    }

}
