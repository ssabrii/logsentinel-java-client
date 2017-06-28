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
    public void setAction(String action) {
        this.action = action;
    }
    public String getEntityId() {
        return entityId;
    }
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    public String getEntityType() {
        return entityType;
    }
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public AuditLogEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(AuditLogEntryType entryType) {
        this.entryType = entryType;
    }
}
