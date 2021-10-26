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
