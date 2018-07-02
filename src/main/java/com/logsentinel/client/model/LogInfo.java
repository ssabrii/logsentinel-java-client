package com.logsentinel.client.model;

public class LogInfo {
    private String hashAlgorithmOid;
    private String leafHashAlgorithmOid;
    private String timestampAlgorithmOid;
    private String publicKey;

    public LogInfo(String hashAlgorithmOid, String leafHashAlgorithmOid, String timestampAlgorithmOid, String publicKey) {
        this.hashAlgorithmOid = hashAlgorithmOid;
        this.leafHashAlgorithmOid = leafHashAlgorithmOid;
        this.timestampAlgorithmOid = timestampAlgorithmOid;
        this.publicKey = publicKey;
    }

    public String getHashAlgorithmOid() {
        return hashAlgorithmOid;
    }

    public String getLeafHashAlgorithmOid() {
        return leafHashAlgorithmOid;
    }

    public String getTimestampAlgorithmOid() {
        return timestampAlgorithmOid;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
