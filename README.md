# LogSentinel Java client

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.logsentinel</groupId>
    <artifactId>logsentinel-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.logsentinel:logsentinel-client:1.0.0"
```

## Getting Started

After you've obtained the client dependency, you can use it as follows:

```java

public class AuditLogControllerApiExample {

    public static void main(String[] args) {
        //credentials obtained after registration
        LogSentinelClientBuilder builder = LogSentinelClientBuilder.create(applicationId, organizationId, secret);
        LogSentinelClient client = builder.build();
        
        try {
            LogResponse result = client.getAuditLogActions().log(actorId, action, details, String actorDisplayName, String actorRole) throws ApiException {
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuditLogControllerApi#logAuthAction");
            e.printStackTrace();
        }
    }
}

```

Note: the client is thread-safe, so ideally you should have a single instance in your multithreaded application.

