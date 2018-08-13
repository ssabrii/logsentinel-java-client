[![Build Status](https://travis-ci.org/LogSentinel/logsentinel-java-client.svg?branch=master)](https://travis-ci.org/LogSentinel/logsentinel-java-client)

# LogSentinel Java client

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.logsentinel</groupId>
    <artifactId>logsentinel-java-client</artifactId>
    <version>0.0.7</version>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.logsentinel:logsentinel-java-client:0.0.7"
```

## Getting Started

After you've obtained the client dependency, you can use it as follows:

```java

public class AuditLogControllerApiExample {

    public static void main(String[] args) {
        //credentials obtained after registration
        LogSentinelClientBuilder builder = LogSentinelClientBuilder
            .create(applicationId, organizationId, secret);
        LogSentinelClient client = builder.build();
        
        try {
            LogResponse result = client.getAuditLogActions().log(
                new ActorData(actorId).setActorDisplayName(username).setActorRoles(roles), 
                new ActionData(details).setAction(action)
            );
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuditLogControllerApi#logAuthAction");
            e.printStackTrace();
        }
    }
}

```

Note: the client is thread-safe, so ideally you should have a single instance in your multithreaded application.

## Encryption of payload
add following lines before builder.build():

```java

byte[] key = <AES-128 or AES-256 encryption key >;
builder.setEncryptionKey(key);

```
By setting this, keywordExtractor is configured as well. It extracts keywords from details before encryption, encrypts them
separately and send them as URL param, this way enabling search in encrypted details in dashboard.
If you want to use the key in dashboard it must be base64 encoded string. So it's recommended to use it like this:

```java
String keyBase64 = "MTIzNDU2Nzg5MDEyMzQ1Ng==";
byte[] key = Base64.getDecoder().decode(keyBase64);
builder.setEncryptionKey(key);

```

### Logback

You can configure logback to sent (some of) the logs to logsentinel.
To use filtering janino library is needed
Example logback.xml:
```xml

<configuration>
    <appender name="logsentinel" class="com.logsentinel.logging.LogSentinelLogbackAppender">

        <basePath>https://app.logsentinel.com</basePath>
        <applicationId>ba2f0680-5424-11e8-b88d-6f2c1b6625e8</applicationId>
        <organizationId>ba2cbc90-5424-11e8-b88d-6f2c1b6625e8</organizationId>
        <secret>d8b63c3d82a6deb56b005a3b8617bf376b6aa6c181021abd0d37e5c5ac9911a1</secret>

        <async>true</async>

        <maskIP>true</maskIP>
        <maskCreditCard>true</maskCreditCard>
        <maskEmail>true</maskEmail>

        <actorIdRegex>\b(?:actorId=)([^,]+)\b</actorIdRegex>
        <actorNameRegex>\b(?:actorName=)([^,]+)\b</actorNameRegex>
        <actionRegex>\b(?:action=)([^,]+)\b</actionRegex>
        <entityRegex>\b(?:entity=)([^,]+)\b</entityRegex>

        <!-- defines regex filter which discards all messages not containing 'logsentinel'-->
        <filter class="ch.qos.logback.core.filter.EvaluatorFilter">
                    <evaluator>
                        <matcher>
                            <Name>custom</Name>
                            <regex>.*logsentinel.*</regex>
                        </matcher>

                        <expression>custom.matches(formattedMessage)</expression>
                    </evaluator>
                    <OnMismatch>DENY</OnMismatch>
                    <OnMatch>NEUTRAL</OnMatch>
                </filter>
    </appender>

    <root level="info">
        <appender-ref ref="logsentinel" />
    </root>
</configuration>
```

You can find more info about logback functions here: https://logback.qos.ch/manual/

### Log4j

You can configure log4j to sent (some of) the logs to logsentinel.
To use filtering apache-log4j-extras library is needed
Example log4j.xml:
```xml

<log4j:configuration debug="false" xmlns:log4j="http://jakarta.apache.org/log4j/">

    <appender name="logsentinel" class="com.logsentinel.logging.LogSentinelLog4jAppender">
        <param name="basePath" value="https://api.logsentinel.com"/>
        <param name="applicationId" value="ba2f0680-5424-11e8-b88d-6f2c1b6625e8"/>
        <param name="organizationId" value="ba2cbc90-5424-11e8-b88d-6f2c1b6625e8"/>
        <param name="secret" value="d8b63c3d82a6deb56b005a3b8617bf376b6aa6c181021abd0d37e5c5ac9911a1"/>
        <param name="async" value="true"/>
        <param name="maskIP" value="true"/>
        <param name="maskCreditCard" value="true"/>
        <param name="maskEmail" value="true"/>
        <param name="actorIdRegex" value="\\b(?:actorId=)([^,]+)\\b"/>
        <param name="actorNameRegex" value="\\b(?:actorName=)([^,]+)\\b"/>
        <param name="actionRegex" value="\\b(?:action=)([^,]+)\\b"/>
        <param name="entityRegex" value="\\b(?:entity=)([^,]+)\\b"/>

        <!-- defines regex filter which discards all messages not containing 'logsentinel'-->
        <filter class="org.apache.log4j.filter.ExpressionFilter">
            <param name="expression" value="MSG LIKE .*logsentinel.*" />
            <param name="acceptOnMatch" value="false"/>
        </filter>
    </appender>

    <root>
        <level value="INFO"/>
        <appender-ref ref="logsentinel"/>
    </root>

</log4j:configuration>
```