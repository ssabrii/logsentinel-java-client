package com.logsentinel;

import java.security.PrivateKey;

import com.logsentinel.client.AuditLogControllerApi;
import com.logsentinel.client.HashControllerApi;

/**
 * Builder used to create an instance of the LogSentinel client.
 * @author bozho
 *
 */
public class LogSentinelClientBuilder {

    private String applicationId;
    private String organizationId;
    private String secret;
    
    private byte[] encryptionKey;
    private PrivateKey signingKey;
    private BodySerializer bodySerializer;
    private String basePath;
    private String contentType;
    
    public static LogSentinelClientBuilder create(String applicationId, String organizationId, String secret) {
        LogSentinelClientBuilder builder = new LogSentinelClientBuilder();
        return builder.setApplicationId(applicationId)
                .setOrganizationId(organizationId)
                .setSecret(secret);
    }

    public LogSentinelClient build() {
        ApiClient apiClient = new ApiClient();
        if (basePath != null) {
            apiClient.setBasePath(basePath);
        }
        apiClient.setUsername(organizationId);
        apiClient.setPassword(secret);
        apiClient.addDefaultHeader("Application-Id", applicationId);
        
        BodySerializer serializer = bodySerializer != null ? bodySerializer : new JsonBodySerializer(apiClient.getJSON());
        if (encryptionKey != null) {
            serializer = new EncryptingBodySerializer(encryptionKey, serializer);
        }
        BodySigner signer = null;
        if (signingKey != null) {
            signer = new BodySigner(signingKey);
        }
        if (contentType == null) {
            contentType = "application/json;charsets=UTF-8";
        }
        
        AuditLogControllerApi auditLogActions = new AuditLogControllerApi(apiClient, serializer, signer, contentType);
        HashControllerApi hashActions = new HashControllerApi(apiClient, serializer, signer, contentType);
        
        LogSentinelClient client = new LogSentinelClient(auditLogActions, hashActions);
        return client;
    }
    
    public String getApplicationId() {
        return applicationId;
    }

    public LogSentinelClientBuilder setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public LogSentinelClientBuilder setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public LogSentinelClientBuilder setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    /**
     * Sets the (symmetric) key used to encrypt outgoing messages. If not set, messages are
     * set unencrypted. Refer to the LogSentniel documentation to get more
     * information on when and why you should encrypt the requests
     * 
     * @param encryptionKey the symmetric (AES) encryption key
     * @return the builder
     */
    public LogSentinelClientBuilder setEncryptionKey(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
        return this;
    }

    public BodySerializer getBodySerializer() {
        return bodySerializer;
    }

    /**
     * Sets a custom body serializer. If none is specified, JSON serializer is used for the body
     * 
     * @param bodySerializer an implementation of body serializer
     * @return the builder
     */
    public LogSentinelClientBuilder setBodySerializer(BodySerializer bodySerializer) {
        this.bodySerializer = bodySerializer;
        return this;
    }

    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets a custom base path for the API, other than logsentinel.com. Should
     * be used when running a local/hosted instance rather than using the cloud
     * one
     * 
     * @param basePath the root url of the logsentinel installation
     * @return
     */
    public LogSentinelClientBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }
   
    /**
     * Sets a signing key for this client. The signing key is used to sign request details in order
     * to make sure no attacker can insert fake records if they gain control on a logging server
     * 
     * @param signingKey the private key to use for request body signing
     * @return
     */
    public LogSentinelClientBuilder setSigningKey(PrivateKey signingKey) {
        this.signingKey = signingKey;
        return this;
    }

    /**
     * Sets the content type for sending requests
     * 
     * @param contentType
     * @return
     */
    public LogSentinelClientBuilder setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
