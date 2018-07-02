package com.logsentinel;

import com.logsentinel.client.*;

public class LogSentinelClient {

    private AuditLogControllerApi auditLogActions;
    private HashControllerApi hashActions;
    private OrganizationUsersControllerApi userActions;
    private ManageApplicationControllerApi applicationActions;
    private AuditLogSearchControllerApi searchActions;
    private ApiVerificationControllerApi verificationActions;

    public LogSentinelClient(AuditLogControllerApi auditLogActions, HashControllerApi hashActions,
                             OrganizationUsersControllerApi userActions, ManageApplicationControllerApi applicationActions,
                             AuditLogSearchControllerApi searchActions, ApiVerificationControllerApi verificationActions) {
        this.auditLogActions = auditLogActions;
        this.hashActions = hashActions;
        this.userActions = userActions;
        this.applicationActions = applicationActions;
        this.searchActions = searchActions;
        this.verificationActions = verificationActions;
    }

    public AuditLogControllerApi getAuditLogActions() {
        return auditLogActions;
    }

    public HashControllerApi getHashActions() {
        return hashActions;
    }

    public OrganizationUsersControllerApi getUserActions() {
        return userActions;
    }

    public ManageApplicationControllerApi getApplicationActions() {
        return applicationActions;
    }

    public AuditLogSearchControllerApi getSearchActions() {
        return searchActions;
    }

    public ApiVerificationControllerApi getVerificationActions() { return verificationActions; }
}
