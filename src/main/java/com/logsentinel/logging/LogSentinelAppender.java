package com.logsentinel.logging;

import com.logsentinel.ApiCallbackAdapter;
import com.logsentinel.ApiException;
import com.logsentinel.LogSentinelClient;
import com.logsentinel.LogSentinelClientBuilder;
import com.logsentinel.client.model.ActionData;
import com.logsentinel.client.model.ActorData;
import com.logsentinel.client.model.LogResponse;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogSentinelAppender {

    private static final String CREDIT_CARD_REGEX = "\\b(?:\\d[ -]*?){16}\\b";
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile(CREDIT_CARD_REGEX);

    private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String IP_REGEX = "\\b(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\b";
    private static final Pattern IP_PATTERN = Pattern.compile(IP_REGEX);

    // configurable properties
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

    private LogSentinelAppender(String basePath, String applicationId, String organizationId, String secret,
                                boolean maskCreditCard, boolean maskIP, boolean maskEmail, boolean async, String actorIdRegex,
                                String actorNameRegex, String actionRegex, String entityRegex) {

        this.basePath = basePath;
        this.applicationId = applicationId;
        this.organizationId = organizationId;
        this.secret = secret;
        this.maskCreditCard = maskCreditCard;
        this.maskIP = maskIP;
        this.maskEmail = maskEmail;
        this.async = async;
        this.actorIdRegex = actorIdRegex;
        this.actorNameRegex = actorNameRegex;
        this.actionRegex = actionRegex;
        this.entityRegex = entityRegex;

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

    public void append(String msg) {

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
                LogResponse r = client.getAuditLogActions().log(actorData, actionData);
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

    public static class LogSentinelAppenderBuilder {

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


        public LogSentinelAppender build() {
            return new LogSentinelAppender(this.basePath, this.applicationId, this.organizationId, this.secret,
                    this.maskCreditCard, this.maskIP, this.maskEmail, this.async, this.actorIdRegex, this.actorNameRegex,
                    this.actionRegex, this.entityRegex);
        }

        public LogSentinelAppenderBuilder setBasePath(String arg) {
            basePath = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setApplicationId(String arg) {
            applicationId = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setOrganizationId(String arg) {
            organizationId = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setSecret(String arg) {
            secret = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setMaskCreditCard(boolean arg) {
            maskCreditCard = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setMaskIp(boolean arg) {
            maskIP = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setMaskEmail(boolean arg) {
            maskEmail = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setAsync(boolean arg) {
            async = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setActorIdRegex(String arg) {
            actorIdRegex = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setActorNameRegex(String arg) {
            actorNameRegex = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setActionRegex(String arg) {
            actionRegex = arg;
            return this;
        }

        public LogSentinelAppenderBuilder setEntityRegex(String arg) {
            entityRegex = arg;
            return this;
        }
    }
}
