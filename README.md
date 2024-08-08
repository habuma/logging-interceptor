Spring HTTP Logging Interceptor
===
This is a simple HTTP logging interceptor for Spring Boot applications. 
It logs the request and response payloads from `RestTemplate` and
`RestClient`, making it easy to inspect and debug the traffic your app
sends and receives with APIs.

Building
---
To build the project and install it in your local Maven repository, run:

```shell
mvn clean install
```

Using the Interceptor
---
After building the project and installing it in your local Maven
repository, add it as a dependency in your Spring Boot application's
`pom.xml`:

```xml
<dependency>
    <groupId>com.habuma</groupId>
    <artifactId>logging-interceptor</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

The library comes with auto-configuration that automatically registers
the interceptor in the Spring application context. So there's no need
for explicit configuration.

However, the interceptor logs at debug level, so you'll need to make
sure that your logging configuration is set to log debug messages. For
example, in `application.properties`:

```properties
logging.level.com.habuma=DEBUG
```

