package com.logsentinel.client.model;

public class ActionData {

    private String action;
    private String entityId;
    private String entityType;
    private String details;
    private AuditLogEntryType entryType;
    
    public ActionData(String details) {
        this.details = details;
    }
    
    public String getAction() {
        return action;
    }
    public ActionData setAction(String action) {
        this.action = action;
        return this;
    }
    public String getEntityId() {
        return entityId;
    }
    public ActionData setEntityId(String entityId) {
        this.entityId = entityId;
        return this;
    }
    public String getEntityType() {
        return entityType;
    }
    public ActionData setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    public String getDetails() {
        return details;
    }
    public ActionData setDetails(String details) {
        this.details = details;
        return this;
    }

    public AuditLogEntryType getEntryType() {
        return entryType;
    }

    public ActionData setEntryType(AuditLogEntryType entryType) {
        this.entryType = entryType;
        return this;
    }
}
