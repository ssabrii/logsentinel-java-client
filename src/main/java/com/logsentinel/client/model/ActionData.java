package com.logsentinel.client.model;

import org.javers.core.diff.Diff;

/**
 * Details about actions performed
 * 
 * @param <T> the type of the details (body)
 */
public class ActionData<T> {

    private String action;
    private String entityId;
    private String entityType;
    private T details;
    private Diff diffDetails;
    private byte[] encryptionKey;
    
    private AuditLogEntryType entryType;
    
    public ActionData(T details) {
        this.details = details;
    }
    
    public String getAction() {
        return action;
    }
    
    /**
     * Sets the action name, i.e. what action this entry represents  
     * 
     * @param action the name of the action
     */
    public ActionData<T> setAction(String action) {
        this.action = action;
        return this;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    /**
     * Sets the entity ID (optional). If the event is about a particular model entity, 
     * you can set its ID here.
     * 
     * @param entityType the ID of the entity
     */
    public ActionData<T> setEntityId(String entityId) {
        this.entityId = entityId;
        return this;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    /**
     * Sets the entity type (optional). If the event is about a particular model entity, 
     * you can set it here (e.g. via <code>entity.getClass().getSimpleName()</code>)
     * 
     * @param entityType the type of the entity
     */
    public ActionData<T> setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    public T getDetails() {
        return details;
    }
    
    /**
     * Sets the details (body) of the action. You can put any data in any form here. 
     * It will get serialized with the serializer supplied to the client object
     * @param details the details object
     * @return
     */
    public ActionData<T> setDetails(T details) {
        this.details = details;
        return this;
    }

    public AuditLogEntryType getEntryType() {
        return entryType;
    }

    /**
     * Sets the entry type. By default it's BUSINESS_LOGIC_ENTRY
     * 
     * @param entryType the entry type
     */
    public ActionData<T> setEntryType(AuditLogEntryType entryType) {
        this.entryType = entryType;
        return this;
    }

	public Diff getDiffDetails() {
		return diffDetails;
	}

	/**
	 * Sets the diffDetails - you can use Javers to provide a diff which will be used as a body. 
	 * Diff details and details are mutually exclusive
	 * @param diffDetails Javers diff
	 */
	public ActionData<T> setDiffDetails(Diff diffDetails) {
		this.diffDetails = diffDetails;
		return this;
	}

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    /**
     * Sets the encryption key in case the application wants to encrypt data with more granular keys, for example if
     * personal data is stored and crypto-erasure needs to be performed
     * 
     * @param encryptionKey the AES encryption key
     */
    public ActionData<T> setEncryptionKey(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
        return this;
    }
    
}
