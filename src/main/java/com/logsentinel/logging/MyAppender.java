package com.logsentinel.logging;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.logsentinel.ApiCallbackAdapter;
import com.logsentinel.ApiException;
import com.logsentinel.LogSentinelClient;
import com.logsentinel.LogSentinelClientBuilder;
import com.logsentinel.client.model.ActionData;
import com.logsentinel.client.model.ActorData;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logback appender which sends logs to logsentinel API.
 * Extends AppenderBase, so all logback functionalities can be attached (Filters, Matchers, etc.)
 * Additionally, supports masking IP addresses, e-mails, credit card numbers
 * and extraction by regex of some basic fields needed for logsentinel API
 */
public class MyAppender extends AppenderBase {

    private static final String CREDIT_CARD_REGEX = "\\b(?:\\d[ -]*?){16}\\b";
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile(CREDIT_CARD_REGEX);

    private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String IP_REGEX = "\\b(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\b";
    private static final Pattern IP_PATTERN = Pattern.compile(IP_REGEX);

    // configurable properties from logback.xml
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

    private Pattern actorIdPattern;
    private Pattern actorNamePattern;
    private Pattern actionPattern;
    private Pattern entityPattern;

    LogSentinelClient client;

    @Override
    public void start() {
        super.start();

        LogSentinelClientBuilder builder;
        builder = LogSentinelClientBuilder.create(applicationId, organizationId, secret);
        builder.setBasePath(basePath);
        builder.setContentType("application/json");
        client = builder.build();

        if (actorIdRegex != null) {
            actorIdPattern = Pattern.compile(actorIdRegex);
        }
        if (actorNameRegex != null) {
            actorNamePattern = Pattern.compile(actorNameRegex);
        }
        if (actionRegex != null) {
            actionPattern = Pattern.compile(actionRegex);
        }
        if (entityRegex != null) {
            entityPattern = Pattern.compile(entityRegex);
        }
    }


    @Override
    protected void append(Object o) {
        LoggingEvent event = (ch.qos.logback.classic.spi.LoggingEvent) o;
        String msg = event.getMessage();

        if (maskCreditCard) {
            msg = hideCreditCard(msg);
        }
        if (maskEmail) {
            msg = hideEmail(msg);
        }
        if (maskIP) {
            msg = hideIP(msg);
        }

        ActorData actorData = new ActorData(extractActorId(msg))
                .setActorDisplayName(extractActorName(msg))
                .setActorRoles(new ArrayList<>());

        ActionData actionData = new ActionData(msg)
                .setAction(extractAction(msg))
                .setEntityId(extractEntity(msg))
                .setBinaryContent(false);

        try {
            if (async) {
                client.getAuditLogActions().logAsync(actorData, actionData, new ApiCallbackAdapter());
            } else {
                client.getAuditLogActions().log(actorData, actionData);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private String hideCreditCard(String unmasked) {
        Matcher matcher = CREDIT_CARD_PATTERN.matcher(unmasked);
        if (matcher.find()) {
            return matcher.replaceAll("<credit card>");
        }
        return unmasked;
    }

    private String hideIP(String unmasked) {
        Matcher matcher = IP_PATTERN.matcher(unmasked);
        if (matcher.find()) {
            return matcher.replaceAll("<ip address>");
        }
        return unmasked;
    }

    private String hideEmail(String unmasked) {
        Matcher matcher = EMAIL_PATTERN.matcher(unmasked);
        if (matcher.find()) {
            return matcher.replaceAll("<e-mail>");
        }
        return unmasked;
    }

    private String extractActorId(String msg) {
        if (actorIdPattern == null) {
            return "";
        }
        Matcher matcher = actorIdPattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private String extractActorName(String msg) {
        if (actorNamePattern == null) {
            return "";
        }
        Matcher matcher = actorNamePattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private String extractAction(String msg) {
        if (actionPattern == null) {
            return "";
        }
        Matcher matcher = actionPattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private String extractEntity(String msg) {
        if (entityPattern == null) {
            return "";
        }
        Matcher matcher = entityPattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public boolean isMaskCreditCard() {
        return maskCreditCard;
    }

    public void setMaskCreditCard(boolean maskCreditCard) {
        this.maskCreditCard = maskCreditCard;
    }

    public boolean isMaskIP() {
        return maskIP;
    }

    public void setMaskIP(boolean maskIP) {
        this.maskIP = maskIP;
    }

    public boolean isMaskEmail() {
        return maskEmail;
    }

    public void setMaskEmail(boolean maskEmail) {
        this.maskEmail = maskEmail;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getActorIdRegex() {
        return actorIdRegex;
    }

    public void setActorIdRegex(String actorIdRegex) {
        this.actorIdRegex = actorIdRegex;
    }

    public String getActionRegex() {
        return actionRegex;
    }

    public void setActionRegex(String actionRegex) {
        this.actionRegex = actionRegex;
    }

    public String getEntityRegex() {
        return entityRegex;
    }

    public void setEntityRegex(String entityRegex) {
        this.entityRegex = entityRegex;
    }

    public String getActorNameRegex() {
        return actorNameRegex;
    }

    public void setActorNameRegex(String actorNameRegex) {
        this.actorNameRegex = actorNameRegex;
    }
}
