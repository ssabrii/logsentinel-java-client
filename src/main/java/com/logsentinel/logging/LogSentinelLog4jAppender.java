package com.logsentinel.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4j appender which sends logs to logsentinel API.
 * Extends AppenderSkeleton, so all log4j functionalities can be attached (Filters, etc.)
 * Additionally, supports masking IP addresses, e-mails, credit card numbers
 * and extraction by regex of some basic fields needed for logsentinel API
 */
public class LogSentinelLog4jAppender extends AppenderSkeleton {

    LogSentinelAppender appender;

    // configurable properties from log4j.xml
    private String basePath;
    private String applicationId;
    private String organizationId;
    private String secret;

    private boolean maskCreditCard;
    private boolean maskIP;
    private boolean maskEmail;
    private boolean async = true;

    private String actorIdRegex;
    private String actorNameRegex;
    private String actionRegex;
    private String entityRegex;
    // end of configurable properties

    private boolean initialized;

    public void init(){
        LogSentinelAppender.LogSentinelAppenderBuilder builder = new LogSentinelAppender.LogSentinelAppenderBuilder();

        builder.setBasePath(basePath)
                .setOrganizationId(organizationId)
                .setSecret(secret)
                .setAsync(async)
                .setActionRegex(actionRegex)
                .setActorIdRegex(actorIdRegex)
                .setActorNameRegex(actorNameRegex)
                .setEntityRegex(entityRegex)
                .setApplicationId(applicationId)
                .setMaskCreditCard(maskCreditCard)
                .setMaskEmail(maskEmail)
                .setMaskIp(maskIP);

        appender = builder.build();
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(!initialized){
            init();
            initialized = true;
        }
        appender.append(loggingEvent.getMessage().toString());
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setMaskCreditCard(boolean maskCreditCard) {
        this.maskCreditCard = maskCreditCard;
    }

    public void setMaskIP(boolean maskIP) {
        this.maskIP = maskIP;
    }

    public void setMaskEmail(boolean maskEmail) {
        this.maskEmail = maskEmail;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setActorIdRegex(String actorIdRegex) {
        this.actorIdRegex = actorIdRegex;
    }

    public void setActorNameRegex(String actorNameRegex) {
        this.actorNameRegex = actorNameRegex;
    }

    public void setActionRegex(String actionRegex) {
        this.actionRegex = actionRegex;
    }

    public void setEntityRegex(String entityRegex) {
        this.entityRegex = entityRegex;
    }
}
