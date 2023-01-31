# Getting started spring authorization server

![cover](https://i.imgur.com/o9NHjhw.jpg)

This project uses [Spring Authorization Server](https://github.com/spring-projects/spring-authorization-server) to establish an authorization system that conforms to the OAuth 2.1 specification and uses JWT Token to be issued.

Spring Authorization Server This is a community-driven project led by the Spring Security team, focusing on providing authorization server support for the Spring community. This project has also begun to replace the Authorization Server support provided by Spring Security OAuth.  

Spring officially announced on 2021/8/19 that Spring Authorization Server has officially withdrawn from the experimental state and entered the product family of the Spring project!  

Since the announcement of the Spring Authorization Server in April 2020, it has implemented most of the OAuth 2.1 authorization protocol and provided moderate support for OpenID Connect 1.0. As the project enters the next stage of development, its focus will shift to advancing support for OpenID Connect 1.0.  

OAuth 2.1 no longer supports password grant type, so Spring Authorization Server does not implement password grant type authentication.  

## Create demo project

Version info
| Name | Version |
| --- | --- |
| SpringBoot | 2.5.6 |
| PostgreSQL | 13.3 |


要加 ID 在 src/main/java/com/example/demo/infrastructure/repositories/tables/pojos/Oauth2AuthorizationConsent.java  

https://github.com/spring-projects/spring-authorization-server/blob/main/samples/custom-consent-authorizationserver/src/main/java/sample/config/AuthorizationServerConfig.java

https://github.com/spring-projects/spring-authorization-server/blob/main/docs/src/docs/asciidoc/protocol-endpoints.adoc
