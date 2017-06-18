package com.logsentinel;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class BodySigner {

    private PrivateKey privateKey;

    public BodySigner(PrivateKey privateKey) {
        super();
        this.privateKey = privateKey;
    }

    public String computeSignature(String requestBody) {
        Signature sig;
        try {
            sig = Signature.getInstance("RSA");
            sig.initSign(privateKey);
            sig.update(requestBody.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
