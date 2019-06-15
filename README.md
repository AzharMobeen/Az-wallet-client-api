### Az-wallet-server-api ###
It's gRPC client application in which I have configured Spring Boot and gRPC framework with [LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter "LogNet Detail")

##### Technologies #####
* Java 8+
* Spring Boot 2.1.5
* Spring Data JPA
* Hibernate
    * Hibernate Core {5.3.10.Final}
    * Hibernate Commons Annotations {5.0.4.Final}
* MySQL 5.6
* gRPC
    * io.github.lognet:grpc-spring-boot-starter:3.3.0
    * io.grpc:protoc-gen-grpc-java:1.21.0
    * com.google.protobuf:protoc:3.7.1
* Gradle
    * protobuf-gradle-plugin:0.8.8
* JUnit

#### My Important choices: ####
* I have configured multi-threading with respect to my machine
    * My machine: *3rd Generation Intel Core i5-3320M (2.60 GHz, 3MB L3 cache, 35 W, 2 cores)*
    * In Client application I'm using Spring boot default SimpleAsyncTaskExecutor for threads.      
* Logging
* Comments on Class level, Method level and some time in side method for important logic.
* I'm sending requests and receive response from Wallet server in two different ways:
    * Transaction v1: *Send all the request concurrently to server after that receive response concurrently*
    * Transaction v2: *Send and Receive request and response concurrently from server*

##### How to Run *[Help](https://spring.io/guides/gs/spring-boot/ "Help")*: #####

* We can run in different ways: 
    * From project folder run this command 
  ```
  gradlew build && java -jar build/libs/wallet-client-0.0.1-SNAPSHOT.jar   
  ```
    * Or you can run
  ```
  gradle :bootrun
  ```
* Az-Wallet-Server-App [Link](https://github.com/AzharMobeen/Az-wallet-server-app)