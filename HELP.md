# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.4/maven-plugin/build-image.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/3.4.4/specification/configuration-metadata/annotation-processor.html)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/3.4.4/reference/data/nosql.html#data.nosql.mongodb)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/3.4.4/reference/data/nosql.html#data.nosql.redis)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.4/reference/using/devtools.html)
* [Apache Freemarker](https://docs.spring.io/spring-boot/3.4.4/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [RSocket](https://rsocket.io/)
* [Validation](https://docs.spring.io/spring-boot/3.4.4/reference/io/validation.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.4/reference/web/servlet.html)
* [WebSocket](https://docs.spring.io/spring-boot/3.4.4/reference/messaging/websockets.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

