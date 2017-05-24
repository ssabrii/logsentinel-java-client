# LogSentinel Java client

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.logsentinel</groupId>
    <artifactId>client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.logsentinel:client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

After you've obtained the client dependency, you can use it as follows:

```java

public class AuditLogControllerApiExample {

    public static void main(String[] args) {
        //credentials obtained after registration
        LogSentinelClientBuilder builder = LogSentinelClientBuilder.create(applicationId, organizationId, secret);
        LogSentinelClient client = builder.build();
        
        try {
            LogResponse result = client.getAuditLogActions().logAuthAction(actorId, authAction, details, applicationId, userId, authorization, signedLoginChallenge, userPublicKey, actorDisplayName, actorRole);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuditLogControllerApi#logAuthAction");
            e.printStackTrace();
        }
    }
}

```

Note: the client is thread-safe, so ideally you should have a single instance in your multithreaded application.

## Documentation for API Endpoints

All URIs are relative to *https://logsentinel.com/*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuditLogControllerApi* | [**logAuthAction**](docs/AuditLogControllerApi.md#logAuthAction) | **POST** /api/log/{actorId}/auth/{authAction} | Log an authentication event with the option to pass actor public key and signature
*AuditLogControllerApi* | [**logSimple**](docs/AuditLogControllerApi.md#logSimple) | **POST** /api/log/simple | Log an event by providing just the body without any additional metadata. The body can be fully encrypted
*AuditLogControllerApi* | [**logStandardAction**](docs/AuditLogControllerApi.md#logStandardAction) | **POST** /api/log/{actorId}/{action}/{entityType}/{entityId} | Log an event by providing full details
*AuditLogControllerApi* | [**log**](docs/AuditLogControllerApi.md#log) | **POST** /api/log/{actorId}/{action} | Log an event by a given actor
*AuditLogControllerApi* | [**searchUsingGET**](docs/AuditLogControllerApi.md#searchUsingGET) | **GET** /api/search | Search logged entries
*AuditLogControllerApi* | [**verify**](docs/AuditLogControllerApi.md#verify) | **POST** /api/verify | Verify whether a given hash is present, indicating that the log is intact
*HashControllerApi* | [**getHashableContentForAuthAction**](docs/HashControllerApi.md#getHashableContentForAuthAction) | **POST** /api/getHashableContent/{actorId}/auth/{authAction} | Get the hash of a request for auth actions
*HashControllerApi* | [**getHashableContentForStandardAction**](docs/HashControllerApi.md#getHashableContentForStandardAction) | **POST** /api/getHashableContent/{actorId}/{action}/{entityType}/{entityId} | Get the hash of a request for standard actions
*HashControllerApi* | [**getHashableContentSimple**](docs/HashControllerApi.md#getHashableContentSimple) | **POST** /api/getHashableContent | Get the hash of a request without any additional metadata (including encrypted request bodies)
*HashControllerApi* | [**getHashableContent**](docs/HashControllerApi.md#getHashableContent) | **POST** /api/getHashableContent/{actorId}/{action} | Get the hash of a request for simple (minimial metadata) actions



