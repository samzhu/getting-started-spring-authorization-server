# Getting started spring authorization server
getting-started-spring-authorization-server

[cover](https://imgur.com/o9NHjhw)

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
  
Download template [Spring Initalizr](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.5.6&packaging=jar&jvmVersion=11&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20auth%20server&packageName=com.example.demo&dependencies=data-jpa,security,web,validation,thymeleaf,liquibase,lombok,devtools,postgresql)  

You can refer to this project [authorizationserver](https://github.com/spring-projects/spring-authorization-server/tree/main/samples/boot/oauth2-integration/authorizationserver) to create an Authserver.

But my requirement here is to use PostgreSQL, so I have to customize my DB Table & OAuth2AuthorizationService. My example uses Liquibase for version control, and the table is built during the service startup phase.  

## Table Schema

If you need reference, you can refer to the following SQL

``` sql
-- ----------------------------
-- Table structure for resource_scope
-- ----------------------------
CREATE TABLE resource_scope (
  id varchar(10) NOT NULL,
  resource varchar(50) NOT NULL,
  scope varchar(50) NOT NULL,
  description varchar(50) NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(36) NOT NULL,
  last_modified_date timestamp DEFAULT NULL,
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table resource_scope
-- ----------------------------
COMMENT ON TABLE resource_scope IS 'Access authority information';
COMMENT ON COLUMN resource_scope.resource IS 'resources name';
COMMENT ON COLUMN resource_scope.scope IS 'access scope';
COMMENT ON COLUMN resource_scope.description IS 'description';

-- ----------------------------
-- Constraint for table resource_scope
-- ----------------------------
ALTER TABLE resource_scope ADD CONSTRAINT pk_resource_scope PRIMARY KEY(id);
ALTER TABLE resource_scope ADD CONSTRAINT uk_resource_scope_scope UNIQUE (scope);
ALTER TABLE resource_scope ADD CONSTRAINT uk_resource_scope_resource_scope UNIQUE (resource, scope);

-- ----------------------------
-- Table structure for user_account
-- ----------------------------
CREATE TABLE user_account (
  id varchar(36) NOT NULL,
  user_name varchar(30) NOT NULL,
  user_password varchar(60) NOT NULL,
  enabled boolean NOT NULL,
  account_non_expired boolean NOT NULL,
  account_non_locked boolean NOT NULL,
  credentials_non_expired boolean NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(36) NOT NULL,
  last_modified_date timestamp NOT NULL,
  last_modified_by varchar(36) NOT NULL
);

-- ----------------------------
-- Comment for table user_account
-- ----------------------------
COMMENT ON TABLE user_account IS 'Account information';
COMMENT ON COLUMN user_account.user_name IS 'user account';
COMMENT ON COLUMN user_account.user_password IS 'user password';
COMMENT ON COLUMN user_account.enabled IS 'enable';
COMMENT ON COLUMN user_account.account_non_expired IS 'expired';
COMMENT ON COLUMN user_account.account_non_locked IS 'account locked';
COMMENT ON COLUMN user_account.credentials_non_expired IS 'certificate has expired ';

-- ----------------------------
-- Constraint for table user_account
-- ----------------------------
ALTER TABLE user_account ADD CONSTRAINT pk_user_account PRIMARY KEY(id);
ALTER TABLE user_account ADD CONSTRAINT uk_user_account UNIQUE (user_name);

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
CREATE TABLE user_group (
  id varchar(10) NOT NULL, 
  code varchar(20) NOT NULL, 
  name varchar(50) NOT NULL, 
  created_date timestamp NOT NULL, 
  created_by varchar(36) NOT NULL, 
  last_modified_date timestamp DEFAULT NULL, 
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table user_group
-- ----------------------------
COMMENT ON TABLE user_group IS 'Group data';
COMMENT ON COLUMN user_group.code IS 'Group code';
COMMENT ON COLUMN user_group.name IS 'Group name';

-- ----------------------------
-- Constraint for table user_group
-- ----------------------------
ALTER TABLE user_group ADD CONSTRAINT pk_user_group PRIMARY KEY(id);
ALTER TABLE user_group ADD CONSTRAINT uk_user_group UNIQUE (name);

-- ----------------------------
-- Table structure for user_group_member
-- ----------------------------
CREATE TABLE user_group_member (
  id varchar(10) NOT NULL,
  user_id varchar(10) NOT NULL,
  user_group_id varchar(10) NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(36) NOT NULL,
  last_modified_date timestamp DEFAULT NULL,
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------
COMMENT ON TABLE user_group_member IS 'User group corresponding data';
COMMENT ON COLUMN user_group_member.user_id IS 'Account ID';
COMMENT ON COLUMN user_group_member.user_group_id IS 'Group ID';

-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE user_group_member ADD CONSTRAINT pk_user_group_member PRIMARY KEY(id);
ALTER TABLE user_group_member ADD CONSTRAINT uk_user_group_member UNIQUE (user_id, user_group_id);
ALTER TABLE user_group_member ADD CONSTRAINT fk_user_group_member_user_id FOREIGN KEY (user_id) REFERENCES user_account(id);
ALTER TABLE user_group_member ADD CONSTRAINT fk_user_group_member_user_group_id FOREIGN KEY (user_group_id) REFERENCES user_group(id);

-- ----------------------------
-- Table structure for user_group_scope
-- ----------------------------
CREATE TABLE user_group_scope (
  id varchar(10) NOT NULL,
  user_group_id varchar(10) NOT NULL,
  resource_scope_id varchar(10) NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(36) NOT NULL,
  last_modified_date timestamp DEFAULT NULL,
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table user_group_scope
-- ----------------------------
COMMENT ON TABLE user_group_scope IS 'Group permissions data';
COMMENT ON COLUMN user_group_scope.user_group_id IS 'Group ID';
COMMENT ON COLUMN user_group_scope.resource_scope_id IS 'Resource scope ID';

-- ----------------------------
-- Constraint for table user_group_authority
-- ----------------------------
ALTER TABLE user_group_scope ADD CONSTRAINT pk_user_group_scope PRIMARY KEY(id);
ALTER TABLE user_group_scope ADD CONSTRAINT uk_user_group_scope UNIQUE (user_group_id, resource_scope_id);
ALTER TABLE user_group_scope ADD CONSTRAINT fk_user_group_scope_user_group_id FOREIGN KEY (user_group_id) REFERENCES user_group(id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE user_group_scope ADD CONSTRAINT fk_user_group_scope_resource_scope_id FOREIGN KEY (resource_scope_id) REFERENCES resource_scope(id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Table structure for oauth2_client
-- ----------------------------
CREATE TABLE oauth2_client (
  id varchar(10) NOT NULL,
  client_id varchar(100) NOT NULL,
  client_id_issued_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  client_secret varchar(200) DEFAULT NULL,
  client_secret_expires_at timestamp NULL DEFAULT NULL,
  client_name varchar(200) NOT NULL,
  created_date timestamp NOT NULL,
  created_by varchar(36) NOT NULL,
  last_modified_date timestamp DEFAULT NULL,
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table oauth2_client
-- ----------------------------
COMMENT ON TABLE oauth2_client IS 'OAuth Client';
COMMENT ON COLUMN oauth2_client.client_id IS 'Client account';
COMMENT ON COLUMN oauth2_client.client_id_issued_at IS 'issuing time';
COMMENT ON COLUMN oauth2_client.client_secret IS 'secret';
COMMENT ON COLUMN oauth2_client.client_secret_expires_at IS 'Secret expiration time';
COMMENT ON COLUMN oauth2_client.client_name IS 'name';

-- ----------------------------
-- Constraint for table oauth2_client
-- ----------------------------
ALTER TABLE oauth2_client ADD CONSTRAINT pk_oauth2_client PRIMARY KEY(id);
ALTER TABLE oauth2_client ADD CONSTRAINT uk_oauth2_client UNIQUE (client_id);

-- ----------------------------
-- Table structure for oauth2_client_authentication_method
-- ----------------------------
CREATE TABLE oauth2_client_authentication_method (
  id varchar(10) NOT NULL, 
  oauth2_client_id varchar(10) NOT NULL, 
  authentication_method varchar(20) NOT NULL, 
  created_date timestamp NOT NULL, 
  created_by varchar(36) NOT NULL, 
  last_modified_date timestamp DEFAULT NULL, 
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table oauth2_client_authentication_method
-- ----------------------------
COMMENT ON TABLE oauth2_client_authentication_method IS 'oauth client verification method';
COMMENT ON COLUMN oauth2_client_authentication_method.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_authentication_method.authentication_method IS 'verification method';

-- ----------------------------
-- Constraint for table oauth2_client_authentication_method
-- ----------------------------
ALTER TABLE oauth2_client_authentication_method ADD CONSTRAINT pk_oauth2_client_authentication_method PRIMARY KEY(id);
ALTER TABLE oauth2_client_authentication_method ADD CONSTRAINT uk_oauth2_client_authentication_method UNIQUE (oauth2_client_id, authentication_method);
ALTER TABLE oauth2_client_authentication_method ADD CONSTRAINT fk_oauth2_client_authentication_method_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);

-- ----------------------------
-- Table structure for oauth2_client_grant_type
-- ----------------------------
CREATE TABLE oauth2_client_grant_type (
  id varchar(10) NOT NULL, 
  oauth2_client_id varchar(10) NOT NULL, 
  grant_type varchar(20) NOT NULL,
  created_date timestamp NOT NULL, 
  created_by varchar(36) NOT NULL, 
  last_modified_date timestamp DEFAULT NULL, 
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table oauth2_client_grant_type
-- ----------------------------
COMMENT ON TABLE oauth2_client_grant_type IS 'oauth client authorization method';
COMMENT ON COLUMN oauth2_client_grant_type.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_grant_type.grant_type IS 'Authorization method';

-- ----------------------------
-- Constraint for table oauth2_client_authentication_method
-- ----------------------------
ALTER TABLE oauth2_client_grant_type ADD CONSTRAINT pk_oauth2_client_grant_type PRIMARY KEY(id);
ALTER TABLE oauth2_client_grant_type ADD CONSTRAINT uk_oauth2_client_grant_type UNIQUE (oauth2_client_id, grant_type);
ALTER TABLE oauth2_client_grant_type ADD CONSTRAINT fk_oauth2_client_grant_type_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);

-- ----------------------------
-- Table structure for oauth2_client_redirect_uri
-- ----------------------------
CREATE TABLE oauth2_client_redirect_uri (
  id varchar(10) NOT NULL, 
  oauth2_client_id varchar(10) NOT NULL, 
  redirect_uri varchar(50) NOT NULL, 
  created_date timestamp NOT NULL, 
  created_by varchar(36) NOT NULL, 
  last_modified_date timestamp DEFAULT NULL, 
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table oauth2_client_redirect_uri
-- ----------------------------
COMMENT ON TABLE oauth2_client_redirect_uri IS 'oauth client redirection URL';
COMMENT ON COLUMN oauth2_client_redirect_uri.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_redirect_uri.redirect_uri IS 'Redirection URL';

-- ----------------------------
-- Constraint for table oauth2_client_redirect_uri
-- ----------------------------
ALTER TABLE oauth2_client_redirect_uri ADD CONSTRAINT pk_oauth2_client_redirect_uri PRIMARY KEY(id);
ALTER TABLE oauth2_client_redirect_uri ADD CONSTRAINT uk_oauth2_client_redirect_uri UNIQUE (oauth2_client_id, redirect_uri);
ALTER TABLE oauth2_client_redirect_uri ADD CONSTRAINT fk_oauth2_client_redirect_uri_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);


-- ----------------------------
-- Table structure for oauth2_client_scope
-- ----------------------------
CREATE TABLE oauth2_client_scope (
  id varchar(10) NOT NULL, 
  oauth2_client_id varchar(10) NOT NULL, 
  resource_scope_id varchar(10) NOT NULL, 
  created_date timestamp NOT NULL, 
  created_by varchar(36) NOT NULL, 
  last_modified_date timestamp DEFAULT NULL, 
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table oauth2_client_scope
-- ----------------------------
COMMENT ON TABLE oauth2_client_scope IS 'client scope';
COMMENT ON COLUMN oauth2_client_scope.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_scope.resource_scope_id IS 'scope ID';

-- ----------------------------
-- Constraint for table oauth2_client_scope
-- ----------------------------
ALTER TABLE oauth2_client_scope ADD CONSTRAINT pk_oauth2_client_scope PRIMARY KEY(id);
ALTER TABLE oauth2_client_scope ADD CONSTRAINT uk_oauth2_client_scope UNIQUE (oauth2_client_id, resource_scope_id);
ALTER TABLE oauth2_client_scope ADD CONSTRAINT fk_oauth2_client_scope_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);
ALTER TABLE oauth2_client_scope ADD CONSTRAINT fk_oauth2_client_scope_resource_scope_id FOREIGN KEY (resource_scope_id) REFERENCES resource_scope(id);

-- ----------------------------
-- Table structure for oauth2_client_setting
-- ----------------------------
CREATE TABLE oauth2_client_setting (
  id varchar(10) NOT NULL, 
  oauth2_client_id varchar(10) NOT NULL, 
  property_type varchar(20) NOT NULL, 
  property_name varchar(50) NOT NULL, 
  property_value varchar(100) NOT NULL, 
  created_date timestamp NOT NULL, 
  created_by varchar(36) NOT NULL, 
  last_modified_date timestamp DEFAULT NULL, 
  last_modified_by varchar(36) DEFAULT NULL
);

-- ----------------------------
-- Comment for table oauth2_client_setting
-- ----------------------------
COMMENT ON TABLE oauth2_client_setting IS 'client config';
COMMENT ON COLUMN oauth2_client_setting.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_setting.property_type IS 'Type of setting';
COMMENT ON COLUMN oauth2_client_setting.property_name IS 'Attribute name';
COMMENT ON COLUMN oauth2_client_setting.property_value IS 'Attribute value';

-- ----------------------------
-- Constraint for table oauth2_client_setting
-- ----------------------------
ALTER TABLE oauth2_client_setting ADD CONSTRAINT pk_oauth2_client_setting PRIMARY KEY(id);
ALTER TABLE oauth2_client_setting ADD CONSTRAINT uk_oauth2_client_setting UNIQUE (oauth2_client_id, property_type, property_name);
ALTER TABLE oauth2_client_setting ADD CONSTRAINT fk_oauth2_client_setting_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);

-- ----------------------------
-- OAuth Reference source
-- https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql
-- ----------------------------

CREATE TABLE oauth2_authorization (
  id varchar(100) NOT NULL, 
  registered_client_id varchar(100) NOT NULL, 
  principal_name varchar(200) NOT NULL, 
  authorization_grant_type varchar(100) NOT NULL, 
  attributes varchar(4000) DEFAULT NULL, 
  state varchar(500) DEFAULT NULL, 
  authorization_code_value text DEFAULT NULL, 
  authorization_code_issued_at timestamp DEFAULT NULL, 
  authorization_code_expires_at timestamp DEFAULT NULL, 
  authorization_code_metadata varchar(2000) DEFAULT NULL, 
  access_token_value text DEFAULT NULL, 
  access_token_issued_at timestamp DEFAULT NULL, 
  access_token_expires_at timestamp DEFAULT NULL, 
  access_token_metadata varchar(2000) DEFAULT NULL, 
  access_token_type varchar(100) DEFAULT NULL, 
  access_token_scopes varchar(1000) DEFAULT NULL, 
  oidc_id_token_value text DEFAULT NULL, 
  oidc_id_token_issued_at timestamp DEFAULT NULL, 
  oidc_id_token_expires_at timestamp DEFAULT NULL, 
  oidc_id_token_metadata varchar(2000) DEFAULT NULL, 
  refresh_token_value text DEFAULT NULL, 
  refresh_token_issued_at timestamp DEFAULT NULL, 
  refresh_token_expires_at timestamp DEFAULT NULL, 
  refresh_token_metadata varchar(2000) DEFAULT NULL
);
ALTER TABLE oauth2_authorization ADD CONSTRAINT pk_oauth2_authorization PRIMARY KEY(id);

CREATE TABLE oauth2_authorization_consent (
  registered_client_id varchar(100) NOT NULL, 
  principal_name varchar(200) NOT NULL, 
  authorities varchar(1000) NOT NULL
);
ALTER TABLE oauth2_authorization_consent ADD CONSTRAINT pk_oauth2_authorization_consent PRIMARY KEY(registered_client_id, principal_name);
```

and initialization data

``` sql
INSERT INTO resource_scope(id, resource, scope, description, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', 'oauth', 'openid', 'Identification', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO resource_scope(id, resource, scope, description, created_date, created_by, last_modified_date, last_modified_by)
VALUES('2', 'user', 'profile', 'Personal information', CURRENT_TIMESTAMP, '１', CURRENT_TIMESTAMP, '1');

INSERT INTO user_account
(id, user_name, user_password, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', 'admin', '$2a$10$oaeeokjBmMdYIRQBzz8cde2Z1CxPIE7kfStPI4WeKNwfItbYJxE/W', true, false, false, false, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO user_group(id, code, name, created_date, created_by, last_modified_date, last_modified_by) 
VALUES ('1', 'ADMIN', 'Manage groups', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO user_group_member(id, user_id, user_group_id, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO user_group_scope(id, user_group_id, resource_scope_id, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO oauth2_client(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', 'democlient', CURRENT_TIMESTAMP, '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', NULL, 'demo', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO oauth2_client_authentication_method(id, oauth2_client_id, authentication_method, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', 'client_secret_post', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_authentication_method(id, oauth2_client_id, authentication_method, created_date, created_by, last_modified_date, last_modified_by)
VALUES('2', '1', 'client_secret_basic', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO oauth2_client_grant_type(id, oauth2_client_id, grant_type, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', 'authorization_code', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_grant_type(id, oauth2_client_id, grant_type, created_date, created_by, last_modified_date, last_modified_by)
VALUES('2', '1', 'refresh_token', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_grant_type(id, oauth2_client_id, grant_type, created_date, created_by, last_modified_date, last_modified_by)
VALUES('3', '1', 'client_credentials', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO oauth2_client_redirect_uri(id, oauth2_client_id, redirect_uri, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', 'https://oidcdebugger.com/debug', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO oauth2_client_scope(id, oauth2_client_id, resource_scope_id, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_scope(id, oauth2_client_id, resource_scope_id, created_date, created_by, last_modified_date, last_modified_by)
VALUES('2', '1', '2', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', '1', 'client', 'require-proof-key', 'false', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value, created_date, created_by, last_modified_date, last_modified_by)
VALUES('2', '1', 'client', 'require-authorization-consent', 'true', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value, created_date, created_by, last_modified_date, last_modified_by)
VALUES('3', '1', 'token', 'id-token-signature-algorithm', 'RS256', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value, created_date, created_by, last_modified_date, last_modified_by)
VALUES('4', '1', 'token', 'access-token-time-to-live', '300', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value, created_date, created_by, last_modified_date, last_modified_by)
VALUES('5', '1', 'token', 'refresh-token-time-to-live', '3600', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value, created_date, created_by, last_modified_date, last_modified_by)
VALUES('6', '1', 'token', 'reuse-refresh-tokens', 'true', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
```

After executing the initialization sql file, You create a user account ```admin``` password ```1234``` and oauth client name ```democlient``` secret ```123456```.  

## How to implement

Here are a few things that you must implement yourself to meet your needs.

### AbstractUserDetailsAuthenticationProvider

First, you must provide the user's search and authentication password implementation, you can inherit from org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider

``` java
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsAuthenticationProvider
    extends AbstractUserDetailsAuthenticationProvider {

  private UserDetailsService userDetailsService;
  private PasswordEncoder passwordEncoder;

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    log.debug(
        ">> CustomUserDetailsAuthenticationProvider.additionalAuthenticationChecks userDetails={}, authentication={}",
        userDetails,
        authentication);
    if (authentication.getCredentials() == null || userDetails.getPassword() == null) {
      throw new BadCredentialsException("Credentials may not be null.");
    }
    log.debug(
        "credentials={}, password={}", authentication.getCredentials(), userDetails.getPassword());
    if (!passwordEncoder.matches(
        (String) authentication.getCredentials(), userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid username or password");
    }
    log.debug("<< CustomUserDetailsAuthenticationProvider.additionalAuthenticationChecks");
  }

  @Override
  protected UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    log.debug(
        ">> CustomUserDetailsAuthenticationProvider.retrieveUser username={}, authentication={}",
        username,
        authentication);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    log.debug(
        "<< CustomUserDetailsAuthenticationProvider.retrieveUser UserDetails={}", userDetails);
    return userDetails;
  }
}
```

### UserDetailsService

Then provide an implementation of returning org.springframework.security.core.userdetails.UserDetails.  

``` java
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserAccountEntityRepository userAccountRepository;
  private UserGroupMemberEntityRepository userGroupMemberRepository;
  private UserGroupEntityRepository userGroupRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug(">> CustomUserDetailsService.loadUserByUsername username={}", username);
    User userDetails = null;
    Optional<UserAccountEntity> userOptional = userAccountRepository.findByUserName(username);
    // Not found...
    userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    UserAccountEntity user = userOptional.get();
    List<UserGroupMemberEntity> userGroupMembers =
        userGroupMemberRepository.findByUserId(user.getId());
    if (userGroupMembers.size() == 0) {
      userDetails =
          new User(
              user.getUserName(),
              user.getUserPassword(),
              user.getEnabled(), // it's usable or not
              !user.getAccountNonExpired(), // Is it expired
              !user.getCredentialsNonExpired(), // The certificate does not expire is true
              !user.getAccountNonLocked(), // Account is not locked to true
              AuthorityUtils.NO_AUTHORITIES);
    } else {
      List<String> userGroupIds =
          userGroupMembers.stream()
              .map(UserGroupMemberEntity::getUserGroupId)
              .collect(Collectors.toList());
      List<UserGroupEntity> userGroups = userGroupRepository.findByIdIn(userGroupIds);
      Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
      for (UserGroupEntity userGroup : userGroups) {
        grantedAuthorities.add(new SimpleGrantedAuthority(userGroup.getCode()));
      }
      userDetails =
          new User(
              user.getUserName(),
              user.getUserPassword(),
              user.getEnabled(), // it's usable or not
              !user.getAccountNonExpired(), // Is it expired
              !user.getCredentialsNonExpired(), // The certificate does not expire is true
              !user.getAccountNonLocked(), // Account is not locked to true
              grantedAuthorities);
    }
    log.debug("<< CustomUserDetailsService.loadUserByUsername User={}", userDetails);
    return userDetails;
  }
}
```

At this stage, I can complete the user's group corresponding query together.

### oauth2 client

In the original [oauth2-registered-client-schema.sql](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org /springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql) you can find the length field of ```client_settings varchar(2000)```, this is because some attributes are different It is converted into a JSON structure and stored in such a field, so I made a little customized adjustment and stored it in the oauth2_client_setting table. You may not need to do this, but if you need it, you can refer to the following implementation.  

``` java
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

  private Oauth2ClientEntityRepository oauth2ClientEntityRepository;
  private Oauth2ClientAuthenticationMethodEntityRepository
      oauth2ClientAuthenticationMethodEntityRepository;
  private Oauth2ClientGrantTypeEntityRepository oauth2ClientGrantTypeEntityRepository;
  private Oauth2ClientRedirectUriEntityRepository oauth2ClientRedirectUriEntityRepository;
  private Oauth2ClientScopeEntityRepository oauth2ClientScopeEntityRepository;
  private Oauth2ClientSettingEntityRepository oauth2ClientSettingEntityRepository;
  private ResourceScopeEntityRepository resourceScopeEntityRepository;

  @Override
  public void save(RegisteredClient registeredClient) {
    log.debug(">> CustomRegisteredClientRepository.save registeredClient={}", registeredClient);
    // TODO need implement
    log.debug("<< CustomRegisteredClientRepository.save ");
  }

  /** Take this part of the Token process */
  @Override
  public RegisteredClient findById(String id) {
    log.debug(">> CustomRegisteredClientRepository.findById id={}", id);
    Optional<Oauth2ClientEntity> oauth2ClientOptional = oauth2ClientEntityRepository.findById(id);
    RegisteredClient registeredClient = this.getRegisteredClient(oauth2ClientOptional);
    log.debug("<< CustomRegisteredClientRepository.findById registeredClient={}", registeredClient);
    return registeredClient;
  }

  /** The authorization process will go through this */
  @Override
  public RegisteredClient findByClientId(String clientId) {
    log.debug(">> CustomRegisteredClientRepository.findByClientId id={}", clientId);
    Optional<Oauth2ClientEntity> oauth2ClientOptional =
        oauth2ClientEntityRepository.findByClientId(clientId);
    RegisteredClient registeredClient = this.getRegisteredClient(oauth2ClientOptional);
    log.debug(
        "<< CustomRegisteredClientRepository.findByClientId registeredClient={}", registeredClient);
    return registeredClient;
  }

  private RegisteredClient getRegisteredClient(Optional<Oauth2ClientEntity> oauth2ClientOptional) {
    oauth2ClientOptional.orElseThrow(() -> new BadCredentialsException("Invalid client"));
    Oauth2ClientEntity oauth2Client = oauth2ClientOptional.get();
    // This is the association ID of the table
    String oauth2ClientId = oauth2Client.getId();
    // Client account
    String clientId = oauth2Client.getClientId();

    List<Oauth2ClientAuthenticationMethodEntity> oauth2ClientAuthenticationMethods =
        oauth2ClientAuthenticationMethodEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<Oauth2ClientGrantTypeEntity> oauth2ClientGrantTypes =
        oauth2ClientGrantTypeEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<Oauth2ClientRedirectUriEntity> oauth2ClientRedirectUris =
        oauth2ClientRedirectUriEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<String> redirectUris =
        oauth2ClientRedirectUris.stream()
            .map(Oauth2ClientRedirectUriEntity::getRedirectUri)
            .collect(Collectors.toList());
    List<Oauth2ClientScopeEntity> oauth2ClientScopes =
        oauth2ClientScopeEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<String> authorityIds =
        oauth2ClientScopes.stream()
            .map(Oauth2ClientScopeEntity::getResourceScopeId)
            .collect(Collectors.toList());
    List<String> clientScopes =
        resourceScopeEntityRepository.findByIdIn(authorityIds).stream()
            .map(ResourceScopeEntity::getScope)
            .collect(Collectors.toList());
    // @formatter:off
    RegisteredClient.Builder builder =
        RegisteredClient.withId(oauth2Client.getId())
            .clientId(clientId)
            .clientIdIssuedAt(
                oauth2Client.getClientIdIssuedAt() != null
                    ? oauth2Client.getClientIdIssuedAt().toInstant(ZoneOffset.UTC)
                    : null)
            .clientSecret(oauth2Client.getClientSecret())
            .clientSecretExpiresAt(
                oauth2Client.getClientSecretExpiresAt() != null
                    ? oauth2Client.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC)
                    : null)
            .clientName(oauth2Client.getClientName())
            .clientAuthenticationMethods(
                (authenticationMethods) ->
                    oauth2ClientAuthenticationMethods.forEach(
                        authenticationMethod ->
                            authenticationMethods.add(
                                resolveClientAuthenticationMethod(
                                    authenticationMethod.getAuthenticationMethod()))))
            .authorizationGrantTypes(
                (grantTypes) ->
                    oauth2ClientGrantTypes.forEach(
                        grantType ->
                            grantTypes.add(
                                resolveAuthorizationGrantType(grantType.getGrantType()))))
            .redirectUris((uris) -> uris.addAll(redirectUris))
            .scopes((scopes) -> scopes.addAll(clientScopes));
    // @formatter:on
    Oauth2ClientSettingEntity requireProofKey =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Client.TYPE,
            OAuthSettingNames.Client.Property.REQUIRE_PROOF_KEY);
    //
    Oauth2ClientSettingEntity requireAuthorizationConsent =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Client.TYPE,
            OAuthSettingNames.Client.Property.REQUIRE_AUTHORIZATION_CONSENT);

    //
    ClientSettings clientSettings =
        ClientSettings.builder()
            .requireProofKey(Boolean.valueOf(requireProofKey.getPropertyValue()))
            .requireAuthorizationConsent(
                Boolean.valueOf(requireAuthorizationConsent.getPropertyValue()))
            .build();
    log.debug("clientSettings={}", clientSettings);
    builder.clientSettings(clientSettings);
    //
    Oauth2ClientSettingEntity idTokenSignatureAlgorithm =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.TYPE,
            OAuthSettingNames.Token.Property.ID_TOKEN_SIGNATURE_ALGORITHM);
    Oauth2ClientSettingEntity accessTokenTimeToLive =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.TYPE,
            OAuthSettingNames.Token.Property.ACCESS_TOKEN_TIME_TO_LIVE);
    Oauth2ClientSettingEntity refreshTokenTimeToLive =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.TYPE,
            OAuthSettingNames.Token.Property.PREFRESH_TOKEN_TIME_TO_LIVE);
    Oauth2ClientSettingEntity reuseRefreshTokens =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.TYPE,
            OAuthSettingNames.Token.Property.REUSE_REFRESH_TOKENS);
    //

    TokenSettings tokenSettings =
        TokenSettings.builder()
            .accessTokenTimeToLive(
                Duration.ofSeconds(Integer.valueOf(accessTokenTimeToLive.getPropertyValue())))
            .reuseRefreshTokens(Boolean.valueOf(reuseRefreshTokens.getPropertyValue()))
            .refreshTokenTimeToLive(
                Duration.ofSeconds(Integer.valueOf(refreshTokenTimeToLive.getPropertyValue())))
            .idTokenSignatureAlgorithm(
                SignatureAlgorithm.from(idTokenSignatureAlgorithm.getPropertyValue()))
            .build();
    log.debug("tokenSettings={}", tokenSettings);
    builder.tokenSettings(tokenSettings);

    RegisteredClient registeredClient = builder.build();
    return registeredClient;
  }

  private Oauth2ClientSettingEntity findClientSetting(
      String oauth2ClientId, String propertyType, String propertyName) {
    Optional<Oauth2ClientSettingEntity> oauth2ClientSettingOptional =
        oauth2ClientSettingEntityRepository.findByOauth2ClientIdAndPropertyTypeAndPropertyName(
            oauth2ClientId, propertyType, propertyName);
    oauth2ClientSettingOptional.orElseThrow(
        () ->
            new BadCredentialsException(
                String.format(
                    "Invalid client setting %s %s %s",
                    oauth2ClientId, propertyType, propertyName)));
    return oauth2ClientSettingOptional.get();
  }

  private static ClientAuthenticationMethod resolveClientAuthenticationMethod(
      String clientAuthenticationMethod) {
    if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC
        .getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST
        .getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_POST;
    } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.NONE;
    }
    return new ClientAuthenticationMethod(
        clientAuthenticationMethod); // Custom client authentication method
  }

  private static AuthorizationGrantType resolveAuthorizationGrantType(
      String authorizationGrantType) {
    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.AUTHORIZATION_CODE;
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS
        .getValue()
        .equals(authorizationGrantType)) {
      return AuthorizationGrantType.CLIENT_CREDENTIALS;
    } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.REFRESH_TOKEN;
    }
    return new AuthorizationGrantType(authorizationGrantType); // Custom authorization grant type
  }
}
```

### OAuth2AuthorizationService

If you adjust the [oauth2_authorization](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql) form like me, you may also need to customize your own OAuth2AuthorizationService, but my example doesn't change much except to change the ```blob``` field to ```text```, for specific implementation, please refer to this [JdbcOAuth2AuthorizationService.java](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/JdbcOAuth2AuthorizationService.java).  

### OAuth2TokenCustomizer

Finally, if there is a customized requirement in the JWT Token, you can add the required information here.  

``` java
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;

@Slf4j
public class CustomOAuth2Token implements OAuth2TokenCustomizer<JwtEncodingContext> {

  @Override
  public void customize(JwtEncodingContext context) {
    context.getClaims().id(UUID.randomUUID().toString()); // jti
    log.debug("<< Customizer.customize JwtEncodingContext={}", context);
  }
}
```

## Configuration

The required components are ready, and then start to assemble.  

### Generate RSA Key

``` bash
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out key.pem
```

algorithm: Using RSA algorithm  
pkeyopt: The key length is 2048  
out: Export as pem file  
  
Then put key.pem to ```${rootDir}/key/key.pem```  

### AuthorizationServerConfig

``` java
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

  @Value("${authserver.key-path}")
  private File pprivateKeyFile;

  @Value("${authserver.issuer}")
  private String issuer;

  /**
   * Default Ebdpoints
   *
   * <p>
   * Authorization /oauth2/authorize Token Endpoint /oauth2/token Token Revocation
   * /oauth2/revoke Token Introspection /oauth2/introspect JWK Set Ecdpoint
   * /oauth2/jwks Authorization Server Metadata
   * /.well-known/oauth-authorization-server OIDC Provider Configuration
   * /.well-known/openid-configuration
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    // Original page
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    return http.formLogin(Customizer.withDefaults()).build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(
      Oauth2ClientEntityRepository oauth2ClientEntityRepository,
      Oauth2ClientAuthenticationMethodEntityRepository oauth2ClientAuthenticationMethodEntityRepository,
      Oauth2ClientGrantTypeEntityRepository oauth2ClientGrantTypeRepository,
      Oauth2ClientRedirectUriEntityRepository oauth2ClientRedirectUriRepository,
      Oauth2ClientScopeEntityRepository oauth2ClientScopeRepository,
      Oauth2ClientSettingEntityRepository oauth2ClientSettingRepository,
      ResourceScopeEntityRepository resourceScopeRepository) {
    RegisteredClientRepository registeredClientRepository = new CustomRegisteredClientRepository(
        oauth2ClientEntityRepository, oauth2ClientAuthenticationMethodEntityRepository, oauth2ClientGrantTypeRepository,
        oauth2ClientRedirectUriRepository, oauth2ClientScopeRepository, oauth2ClientSettingRepository,
        resourceScopeRepository);
    return registeredClientRepository;
  }

  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> oauth2TokenCustomizer() {
    return new CustomOAuth2Token();
  }

  @Bean
  public UserDetailsService userDetailsService(UserAccountEntityRepository userAccountRepository,
      UserGroupMemberEntityRepository userGroupMemberRepository, UserGroupEntityRepository userGroupRepository) {
    return new CustomUserDetailsService(userAccountRepository, userGroupMemberRepository, userGroupRepository);
  }

  @Bean
  public AbstractUserDetailsAuthenticationProvider abstractUserDetailsAuthenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    return new CustomUserDetailsAuthenticationProvider(userDetailsService, passwordEncoder);
  }

  @Bean
  public OAuth2AuthorizationService authorizationService(
      Oauth2AuthorizationEntityRepository oauth2AuthorizationEntityRepository,
      RegisteredClientRepository registeredClientRepository) {
    OAuth2AuthorizationService OAuth2AuthorizationService = new CustomOAuth2AuthorizationService(
        oauth2AuthorizationEntityRepository, registeredClientRepository);
    return OAuth2AuthorizationService;
  }

  @Bean
  public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
      RegisteredClientRepository registeredClientRepository) {
    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() throws Exception {
    String pemEncodedRSAPrivateKey = new String(Files.readAllBytes(pprivateKeyFile.toPath()), StandardCharsets.UTF_8);
    JWK jwk = JWK.parseFromPEMEncodedObjects(pemEncodedRSAPrivateKey);
    JWKSet jwkSet = new JWKSet(jwk);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public ProviderSettings providerSettings() {
    return ProviderSettings.builder().issuer(issuer).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
```

### DefaultSecurityConfig

``` java
@EnableWebSecurity
public class DefaultSecurityConfig {

  // @formatter:off
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeRequests(authorizeRequests ->
        authorizeRequests.anyRequest().authenticated()
      )
      .formLogin(org.springframework.security.config.Customizer.withDefaults());
    return http.build();
  }
  // @formatter:on
}
```

## test

At this point, you can perform a login test. Here you will see the login default page. Later on the page will tell you how to customize it. First, we will test whether the function is normal.  

### Get public key

``` bash
curl --request GET 'http://localhost:8080/oauth2/jwks'
```

OAuth server response

``` json
{
    "keys": [
        {
            "kty": "RSA",
            "e": "AQAB",
            "n": "n0mpjdlzz__bg8hlJH7ktuTuDPgmCPUQauf00Kd3dGKz9Kr26hqITRQV2POmrHemzpkenO-LP1BasDTgaIfil-tLP6djLDzk8PTEiroviCAo0IGsDrClVXl91M-5Yvl5sStHVO4oDgSbBCNR52jvLr2FjPe4qVL0fqWMwPEKU8RQifrNfGvPufJKB6cXRiSEBJQ9UOIhaZrL51pevM5oQo4TTmuuzaDfHEEZneXyo1HvSvYNKBwR3mWkeFTFdfMYfPvM7cASVS0sEnSO5TUUNyO7OIyus0Ve8Z8Cz8NwkAkjt8c0-J4FBlQ5_7PHKFknSZZ6F_xdzHM9J5_q2EtWXw"
        }
    ]
}
```

### Use OpenID Connect debugger to test

Open the [OpenID Connect debugger](https://oidcdebugger.com/) website and enter the following test data.  

| Name | Value | Desc |
| --- | --- | --- |
| Authorize URI (required) | http://localhost:8080/oauth2/authorize | - |
| Redirect URI (required) | https://oidcdebugger.com/debug | - |
| Client ID (required) | democlient | - |
| Scope (required) | openid | - |
| State (optional) | None | Optional value, carried through the entire process and returned to the client |
| Nonce (optional) | None | A random number (or a number used once) is a random value used to prevent replay attacks |
| Response type (required) | code | Reply with authorization code |
| Response mode (required) | form_post | Use Form Post to Redirect URI |

copy url like this and paste it in the browser address bar.

``` url
http://localhost:8080/oauth2/authorize
?client_id=democlient
&redirect_uri=https://oidcdebugger.com/debug
&scope=openid
&response_type=code
&response_mode=form_post
&nonce=6wvhzv2kwmf
```

Then you will see the login screen
[login page](https://imgur.com/pi7ldhi)

input username ```admin``` and password ```1234``` to login, then it will be redirect back to ```https://oidcdebugger.com/debug```, if successful you can get authorization code

[Imgur](https://imgur.com/6dVgsu2)

Then you can use the authorization code to exchange for jwt token.

``` bash
curl --location --request POST 'localhost:8080/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code=kWRApivv3DjoZOLamU5UlKV3T-U2eqrjQPfWMY9qzKO-EMPcMI-VMRuJbqZm3maNh1q09lePlAIFeuSPVwAMrnFvStJNec3IlwAMkS39J_jFthuZqFTjQCrBF12xft3F' \
--data-urlencode 'client_id=democlient' \
--data-urlencode 'client_secret=123456' \
--data-urlencode 'redirect_uri=https://oidcdebugger.com/debug'
```

OAuth server response

``` json
{
    "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImRlbW9jbGllbnQiLCJuYmYiOjE2MzUyMzUxODMsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOlwvXC9hdXRoLXNlcnZlcjo4MDgwIiwiZXhwIjoxNjM1MjM1NDgzLCJpYXQiOjE2MzUyMzUxODMsImp0aSI6ImNmNDgxNjBjLWU1MDItNDI4ZC05YmIxLTljNmU5OGRjNjc4MyJ9.DR2dj-j7zUmPHKqvicAbRRd2GTWSRTVNYPdGoOtnApB03bcszoEKF9PxfWRGlEgjNi28_d4oPpiQTRo5rnQoa1RY79Ff27fO_Y08uvP6a4GZQGT_XlbymxC2xjg74wtYsyR7ea2TSahM0KJfKohI35I0d92yOQ5WtCEBkKPRuTAagIHkTzw_pzwZeCLqWS82NaAlCrNOhg_zuJzt0iKnDM7LmvWbMiEAGcAIq6NnLc6pShIa49tyHE0W3NFgtRgt_oavatMxn9QPCurgGTSXyZv_Cj0t-qKz2NldlSHftdfEENWSFNIwEUs3pkQnrDYfgFWRdJjpctjAQQk0UfyGFw",
    "refresh_token": "qknzEz6eTOwtJVusUUOLohRE_rD_y5_fI4NAR42--p16_6UthANcNTrcNr_6w3QTbxkiz1saXNdtxrWu_cj3LiZjZEaGfk-0rmyAAMrHcQWoheTL6PRvvPs6CFQtDJLk",
    "scope": "openid",
    "id_token": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImRlbW9jbGllbnQiLCJhenAiOiJkZW1vY2xpZW50IiwiaXNzIjoiaHR0cDpcL1wvYXV0aC1zZXJ2ZXI6ODA4MCIsImV4cCI6MTYzNTIzNjk4MywiaWF0IjoxNjM1MjM1MTgzLCJub25jZSI6IjZ3dmh6djJrd21mIiwianRpIjoiMmY0ZDhjZmUtZDA1Yi00YTk1LTkxMjItMDhiNTgyZjU1N2FkIn0.TpzMB7jsBsXLU8wiwc6zh8R507A_7ta1o5uZ99myOhzwcpv2uhGie2860FBYeyDJ15I2xandYktT-yCaAx-SbpyxXnhHH8ZhkpCrYwfMGDOapFQpfm1RfamQ10P2TpNNnfwfGSUOEOcGtzMzQpgZXdkO7flcRwEle8EmE-xxcpCUqCBpOrId75Y9C0OhmCPnY4JmZMt3jP27xBWemetl6Fo5jSMytR7RJwMT7xbXHhKl3CIM8QSyiiES8ARM_HTbTJHOsLqsQX5KXHnvXyxCP0SNNMV55fKnyTrr4XqgfHccinr3QSVw2wcWwhxNs3uUsJQTadgfZxcEmUb-tQrbFQ",
    "token_type": "Bearer",
    "expires_in": 299
}
```

### Test client credentials

This mode is used in Server-Server, without User participation.  

``` bash
curl --request POST '35.194.246.111/oauth2/token' \
--header 'Authorization: Basic ZGVtb2NsaWVudDoxMjM0NTY=' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=client_credentials'
```

OAuth server 回覆

``` bash
{
    "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkZW1vY2xpZW50IiwiYXVkIjoiZGVtb2NsaWVudCIsIm5iZiI6MTYzMzk1NzkzNCwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSJdLCJpc3MiOiJodHRwOlwvXC9hdXRoLXNlcnZlcjo4MDgwIiwiZXhwIjoxNjMzOTU4MjM0LCJpYXQiOjE2MzM5NTc5MzR9.ZKxy6Pz8Mx8AgU0hVsPo8jAwCk9jlgAx0sS_ao_cXLs5DnRU-VlzyrB9nUuTxUZJn4dbv9KPzP-ZnotBth2RJ6uhClMpJMq4hyNo_2Yu1LlE9O-tkn4b3UnKptpqroc34PWhg_SY-aa_7Y8M33kKOZeSdUxKYVQzr09-P0IVnIa1ENXUuGrxqCENW-sOMaobnW1AOqjvRPq4p5Ei2OiU6NJocxfYpaGXiLPizSt2LIAfRGYGt2U8PlfprnM9py7_QnW-K37-MhCiAtxuss69o4xVHZdheuheQaKFpBHiyi92nXhDKnZmC54ZBLAPQDN9MMSQBVeKO3JTnN3IhNbPGA",
    "scope": "openid",
    "token_type": "Bearer",
    "expires_in": 299
}
```

## Customized page

If you have a custom page requirement, please refer to the next steps.

add dependencies

``` groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
```

add controller

``` java
@Controller
public class LoginController {

  @GetMapping("/login")
  public String getLoginPage(Model model) {
    return "login";
  }
}
```

add page, there is not much customization here, only add a banner image to test.

``` html
<!DOCTYPE HTML>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please Log In</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link href="https://getbootstrap.com/docs/4.0/examples/signin/signin.css" rel="stylesheet"
        crossorigin="anonymous" />
    <style>

    </style>
</head>

<body>
    <div class="container">
        <div class="col-sm-10 col-lg-4 col-xl-4 mx-auto text-center">
            <img src="/images/banner.jpg" class="carousel-inner img-responsive img-rounded">
        </div>
        <form class="form-signin" method="post" th:action="@{/login}">
            <h2 class="form-signin-heading">Please Log In</h2>
            <div th:if="${param.error}" class="alert alert-danger" role="alert">wrong user name or password</div>
            <div th:if="${param.logout}" class="alert alert-success" role="alert">you are logged out</div>
            <p>
                <label for="username" class="sr-only">Username</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="輸入帳號" required
                    autofocus>
            </p>
            <p>
                <label for="password" class="sr-only">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="輸入密碼" required>
            </p>
            <button class="btn btn-lg btn-primary btn-block" type="submit">login</button>
        </form>
    </div>
</body>

</html>
```

DefaultSecurityConfig.java

``` java
@EnableWebSecurity
public class DefaultSecurityConfig {

  // @formatter:off
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/images/*")
        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        .permitAll()
        .and()
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());
    return http.build();
  }
  // @formatter:on
}
```

Then try to log in again, now we are using our own login page.
[Customized login page](https://imgur.com/RSu2hCf)

### Consent page

When you provide an external Client to access your user information, you will usually provide a consent page for users to confirm. Here is also how to customize the consent page.  

add AuthorizationConsentController.java

``` java
/** @author Daniel Garnier-Moiroux */
@Controller
@RequiredArgsConstructor
public class AuthorizationConsentController {
  private final RegisteredClientRepository registeredClientRepository;
  private final OAuth2AuthorizationConsentService authorizationConsentService;
  private final ScopeAssembler scopeAssembler;

  @GetMapping(value = "/oauth2/consent")
  public String consent(
      Principal principal,
      Model model,
      @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
      @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
      @RequestParam(OAuth2ParameterNames.STATE) String state) {

    // Remove scopes that were already approved
    Set<String> scopesToApprove = new HashSet<>();
    Set<String> previouslyApprovedScopes = new HashSet<>();
    RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
    OAuth2AuthorizationConsent currentAuthorizationConsent =
        this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());
    Set<String> authorizedScopes;
    if (currentAuthorizationConsent != null) {
      authorizedScopes = currentAuthorizationConsent.getScopes();
    } else {
      authorizedScopes = Collections.emptySet();
    }
    for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
      if (authorizedScopes.contains(requestedScope)) {
        previouslyApprovedScopes.add(requestedScope);
      } else {
        scopesToApprove.add(requestedScope);
      }
    }

    model.addAttribute("clientId", clientId);
    model.addAttribute("state", state);
    model.addAttribute("scopes", withDescription(scopesToApprove));
    model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
    model.addAttribute("principalName", principal.getName());

    return "consent";
  }

  private Set<ScopeWithDescription> withDescription(Set<String> scopes) {
    Set<ScopeWithDescription> scopeWithDescriptions = new HashSet<>();
    for (String scope : scopes) {

      scopeWithDescriptions.add(scopeAssembler.scopeToScopeWithDescription(scope));
    }
    return scopeWithDescriptions;
  }
}
```

add src/main/resources/templates/consent.html

``` html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
        integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <title>Custom consent page - Consent required</title>
    <style>
        body {
            background-color: aliceblue;
        }
    </style>
    <script>
        function cancelConsent() {
            document.consent_form.reset();
            document.consent_form.submit();
        }
    </script>
</head>

<body>
    <div class="container">
        <div class="col-sm-10 col-lg-4 col-xl-4 mx-auto text-center">
            <img src="/images/consent.jpg" class="carousel-inner img-responsive img-rounded">
        </div>
        <div class="row">
            <div class="col text-center">
                <p>
                    The application
                    <span class="font-weight-bold text-primary" th:text="${clientId}"></span>
                    wants to access your account
                    <span class="font-weight-bold" th:text="${principalName}"></span>
                </p>
            </div>
        </div>
        <div class="row pb-3">
            <div class="col text-center">
                <p>The following permissions are requested by the above app.<br />Please review
                    these and consent if you approve.</p>
            </div>
        </div>
        <div class="row">
            <div class="col text-center">
                <form name="consent_form" method="post" action="/oauth2/authorize">
                    <input type="hidden" name="client_id" th:value="${clientId}">
                    <input type="hidden" name="state" th:value="${state}">

                    <div th:each="scope: ${scopes}" class="form-group form-check py-1">
                        <input class="form-check-input" type="checkbox" name="scope" th:value="${scope.scope}"
                            th:id="${scope.scope}">
                        <label class="form-check-label font-weight-bold" th:for="${scope.scope}"
                            th:text="${scope.scope}"></label>
                        <p class="text-primary" th:text="${scope.description}"></p>
                    </div>

                    <p th:if="${not #lists.isEmpty(previouslyApprovedScopes)}">You have already granted the following
                        permissions to the above app:</p>
                    <div th:each="scope: ${previouslyApprovedScopes}" class="form-group form-check py-1">
                        <input class="form-check-input" type="checkbox" th:id="${scope.scope}" disabled checked>
                        <label class="form-check-label font-weight-bold" th:for="${scope.scope}"
                            th:text="${scope.scope}"></label>
                        <p class="text-primary" th:text="${scope.description}"></p>
                    </div>

                    <div class="form-group pt-3">
                        <button class="btn btn-primary btn-lg" type="submit" id="submit-consent">
                            Submit Consent
                        </button>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-link regular" type="button" id="cancel-consent"
                            onclick="cancelConsent();">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <div class="row pt-4">
            <div class="col text-center">
                <p>
                    <small>
                        Your consent to provide access is required.
                        <br />If you do not approve, click Cancel, in which case no information will be shared with the
                        app.
                    </small>
                </p>
            </div>
        </div>
    </div>
</body>

</html>
```

Adjust AuthorizationServerConfig.java

``` java
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

  private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
    authorizationServerConfigurer
        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));

    RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

    http.requestMatcher(endpointsMatcher)
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(authorizationServerConfigurer);
    return http.formLogin(Customizer.withDefaults()).build();
  }
}
```

Because openid is defined by default without the user's consent, the consent screen will appear when we add a profile to our Scope.

``` url
http://localhost:8080/oauth2/authorize?client_id=democlient&redirect_uri=https%3A%2F%2Foidcdebugger.com%2Fdebug&scope=openid%20profile&response_type=code&response_mode=form_post&nonce=6wvhzv2kwmf
```

After successful login
[consent page](https://imgur.com/uQd40aM)