[![Build Status](https://travis-ci.org/LogSentinel/logsentinel-java-client.svg?branch=master)](https://travis-ci.org/LogSentinel/logsentinel-java-client)

# LogSentinel Java client

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.logsentinel</groupId>
    <artifactId>logsentinel-java-client</artifactId>
    <version>0.0.3</version>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.logsentinel:logsentinel-java-client:0.0.3"
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