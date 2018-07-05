package com.logsentinel.client.model;

public class LogInfo {
    private String hashAlgorithmOid;
    private String leafHashAlgorithmOid;
    private String timestampAlgorithmOid;
    private String timestampPublicKey;

    public LogInfo(String hashAlgorithmOid, String leafHashAlgorithmOid, String timestampAlgorithmOid, String publicKey) {
        this.hashAlgorithmOid = hashAlgorithmOid;
        this.leafHashAlgorithmOid = leafHashAlgorithmOid;
        this.timestampAlgorithmOid = timestampAlgorithmOid;
        this.timestampPublicKey = publicKey;
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
        return timestampPublicKey;
    }
}
