-- ----------------------------
-- Table structure for scope
-- ----------------------------
CREATE TABLE scope (
	id varchar(10) NOT NULL,
	resource varchar(50) NOT NULL,
    scope varchar(50) NOT NULL,
    description varchar(50) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table scope
-- ----------------------------
ALTER TABLE scope ADD CONSTRAINT pk_scope_id PRIMARY KEY(id);
ALTER TABLE scope ADD CONSTRAINT uk_scope_resource_scope UNIQUE (resource, scope);
-- ----------------------------
-- Comment for table scope
-- ----------------------------
COMMENT ON TABLE scope IS 'resource scope';
COMMENT ON COLUMN scope.id IS 'unique value';
COMMENT ON COLUMN scope.resource IS 'resources name';
COMMENT ON COLUMN scope.scope IS 'scope code';

-- ----------------------------
-- Table structure for user_account
-- ----------------------------
CREATE TABLE user_account (
	id varchar(36) NOT NULL,
	user_account varchar(30) NOT NULL,
	user_password varchar(60) NOT NULL,
	enabled boolean NOT NULL,
	account_non_expired boolean NOT NULL,
	account_non_locked boolean NOT NULL,
	credentials_non_expired boolean NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_account
-- ----------------------------
ALTER TABLE user_account ADD CONSTRAINT pk_user_account_id PRIMARY KEY(id);
ALTER TABLE user_account ADD CONSTRAINT uk_user_account_user_account UNIQUE (user_account);
-- ----------------------------
-- Comment for table user_account
-- ----------------------------
COMMENT ON TABLE user_account IS 'Account information';
COMMENT ON COLUMN user_account.user_account IS 'user account';
COMMENT ON COLUMN user_account.user_password IS 'user password';
COMMENT ON COLUMN user_account.enabled IS 'enable';
COMMENT ON COLUMN user_account.account_non_expired IS 'expired';
COMMENT ON COLUMN user_account.account_non_locked IS 'account locked';
COMMENT ON COLUMN user_account.credentials_non_expired IS 'certificate has expired';

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
CREATE TABLE user_group (
	id varchar(10) NOT NULL,
	"name" varchar(50) NOT NULL,
	code varchar(20) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_group
-- ----------------------------
ALTER TABLE user_group ADD CONSTRAINT pk_user_group_id PRIMARY KEY(id);
ALTER TABLE user_group ADD CONSTRAINT uk_user_group_code UNIQUE (code);
-- ----------------------------
-- Comment for table user_group
-- ----------------------------
COMMENT ON TABLE user_group IS 'User Group';
COMMENT ON COLUMN user_group.code IS 'Group code';
COMMENT ON COLUMN user_group."name" IS 'Group name';

-- ----------------------------
-- Table structure for user_group_member
-- ----------------------------
CREATE TABLE user_group_member (
	id varchar(10) NOT NULL,
	user_group_id varchar(10) NOT NULL,
	user_id varchar(10) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE user_group_member ADD CONSTRAINT pk_user_group_member_id PRIMARY KEY(id);
ALTER TABLE user_group_member ADD CONSTRAINT uk_user_group_member_group_user UNIQUE (user_group_id, user_id);
ALTER TABLE user_group_member ADD CONSTRAINT fk_user_group_member_group FOREIGN KEY (user_group_id) REFERENCES user_group(id);
ALTER TABLE user_group_member ADD CONSTRAINT fk_user_group_member_user FOREIGN KEY (user_id) REFERENCES scope(id);
-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------
COMMENT ON TABLE user_group_member IS 'User group corresponding data';
COMMENT ON COLUMN user_group_member.user_group_id IS 'Group ID';
COMMENT ON COLUMN user_group_member.user_id IS 'Account ID';

-- ----------------------------
-- Table structure for user_group_scope
-- ----------------------------
CREATE TABLE user_group_scope (
	id varchar(10) NOT NULL,
	user_group_id varchar(10) NOT NULL,
	scope_id varchar(10) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE user_group_scope ADD CONSTRAINT pk_user_group_scope_id PRIMARY KEY(id);
ALTER TABLE user_group_scope ADD CONSTRAINT uk_user_group_scope_group_scope UNIQUE (user_group_id, scope_id);
ALTER TABLE user_group_scope ADD CONSTRAINT fk_user_group_scope_group FOREIGN KEY (user_group_id) REFERENCES user_group(id);
ALTER TABLE user_group_scope ADD CONSTRAINT fk_user_group_scope_scope FOREIGN KEY (scope_id) REFERENCES user_group(id);
-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------
COMMENT ON TABLE user_group_scope IS 'Group permissions data';
COMMENT ON COLUMN user_group_scope.user_group_id IS 'Group ID';
COMMENT ON COLUMN user_group_scope.scope_id IS 'Scope ID';

-- ----------------------------
-- Table structure for oauth2_client
-- ----------------------------
CREATE TABLE oauth2_client (
	id varchar(10) NOT NULL,
	client_id varchar(100) NOT NULL,
	client_id_issued_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	client_secret varchar(200) DEFAULT NULL,
	client_secret_expires_at timestamp DEFAULT NULL,
	client_name varchar(200) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table oauth2_client
-- ----------------------------
ALTER TABLE oauth2_client ADD CONSTRAINT pk_oauth2_client_id PRIMARY KEY(id);
ALTER TABLE oauth2_client ADD CONSTRAINT uk_oauth2_client_client_id UNIQUE (client_id);
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
-- Table structure for oauth2_client_authentication_method
-- ----------------------------
CREATE TABLE oauth2_client_authentication_method (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL,
	authentication_method varchar(20) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table oauth2_client_authentication_method
-- ----------------------------
ALTER TABLE oauth2_client_authentication_method ADD CONSTRAINT pk_oauth2_client_authentication_method_id PRIMARY KEY(id);
ALTER TABLE oauth2_client_authentication_method ADD CONSTRAINT uk_oauth2_client_authentication_method_client_method UNIQUE (oauth2_client_id, authentication_method);
ALTER TABLE oauth2_client_authentication_method ADD CONSTRAINT fk_oauth2_client_authentication_method FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);
-- ----------------------------
-- Comment for table oauth2_client_authentication_method
-- ----------------------------
COMMENT ON TABLE oauth2_client_authentication_method IS 'oauth client verification method';
COMMENT ON COLUMN oauth2_client_authentication_method.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_authentication_method.authentication_method IS 'verification method';

-- ----------------------------
-- Table structure for oauth2_client_grant_type
-- ----------------------------
CREATE TABLE oauth2_client_grant_type (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL,
	grant_type varchar(20) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table oauth2_client_grant_type
-- ----------------------------
ALTER TABLE oauth2_client_grant_type ADD CONSTRAINT pk_oauth2_client_grant_type_id PRIMARY KEY(id);
ALTER TABLE oauth2_client_grant_type ADD CONSTRAINT uk_oauth2_client_grant_type_client_id_grant_type UNIQUE (oauth2_client_id, grant_type);
ALTER TABLE oauth2_client_grant_type ADD CONSTRAINT fk_oauth2_client_grant_type_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);
-- ----------------------------
-- Comment for table oauth2_client_grant_type
-- ----------------------------
COMMENT ON TABLE oauth2_client_grant_type IS 'oauth client authorization method';
COMMENT ON COLUMN oauth2_client_grant_type.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_grant_type.grant_type IS 'Authorization method';

-- ----------------------------
-- Table structure for oauth2_client_redirect_uri
-- ----------------------------
CREATE TABLE oauth2_client_redirect_uri (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL,
	redirect_uri varchar(50) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table oauth2_client_redirect_uri
-- ----------------------------
ALTER TABLE oauth2_client_redirect_uri ADD CONSTRAINT pk_oauth2_client_redirect_uri_id PRIMARY KEY(id);
ALTER TABLE oauth2_client_redirect_uri ADD CONSTRAINT uk_oauth2_client_redirect_uri_client_uri UNIQUE (oauth2_client_id, redirect_uri);
ALTER TABLE oauth2_client_redirect_uri ADD CONSTRAINT fk_oauth2_client_redirect_uri_client FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);
-- ----------------------------
-- Comment for table oauth2_client_redirect_uri
-- ----------------------------
COMMENT ON TABLE oauth2_client_redirect_uri IS 'oauth client redirection URL';
COMMENT ON COLUMN oauth2_client_redirect_uri.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_redirect_uri.redirect_uri IS 'Redirection URL';

-- ----------------------------
-- Table structure for user_group_scope
-- ----------------------------
CREATE TABLE oauth2_client_setting (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL,
	property_type varchar(20) NOT NULL,
	property_name varchar(50) NOT NULL,
	property_value varchar(100) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE oauth2_client_setting ADD CONSTRAINT pk_oauth2_client_setting_id PRIMARY KEY(id);
ALTER TABLE oauth2_client_setting ADD CONSTRAINT uk_oauth2_client_setting_client_type_name UNIQUE (oauth2_client_id, property_type, property_name);
ALTER TABLE oauth2_client_setting ADD CONSTRAINT fk_oauth2_client_setting_client FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);
-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------
COMMENT ON TABLE oauth2_client_setting IS 'client config';
COMMENT ON COLUMN oauth2_client_setting.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_setting.property_type IS 'Type of setting';
COMMENT ON COLUMN oauth2_client_setting.property_name IS 'Attribute name';
COMMENT ON COLUMN oauth2_client_setting.property_value IS 'Attribute value';

-- ----------------------------
-- Table structure for oauth2_client_scope
-- ----------------------------
CREATE TABLE oauth2_client_scope (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL,
	scope_id varchar(10) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE oauth2_client_scope ADD CONSTRAINT pk_oauth2_client_scope_id PRIMARY KEY(id);
ALTER TABLE oauth2_client_scope ADD CONSTRAINT uk_oauth2_client_scope_client_scope UNIQUE (oauth2_client_id, scope_id);
ALTER TABLE oauth2_client_scope ADD CONSTRAINT fk_oauth2_client_scope_client FOREIGN KEY (oauth2_client_id) REFERENCES oauth2_client(id);
ALTER TABLE oauth2_client_scope ADD CONSTRAINT fk_oauth2_client_scope_scope FOREIGN KEY (scope_id) REFERENCES scope(id);
-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------
COMMENT ON TABLE oauth2_client_scope IS 'client scope';
COMMENT ON COLUMN oauth2_client_scope.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN oauth2_client_scope.scope_id IS 'scope ID';



-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
CREATE TABLE oauth2_authorization (
	id varchar(100) NOT NULL,
	registered_client_id varchar(100) NOT NULL,
	principal_name varchar(200) NOT NULL,
	authorization_grant_type varchar(100) NOT NULL,
	"attributes" varchar(4000) NULL,
	state varchar(500) NULL ,
	authorization_code_value text DEFAULT NULL,
	authorization_code_issued_at timestamp NULL,
	authorization_code_expires_at timestamp NULL,
	authorization_code_metadata varchar(2000) NULL,
	access_token_value text NULL,
	access_token_issued_at timestamp NULL,
	access_token_expires_at timestamp NULL,
	access_token_metadata varchar(2000) NULL,
	access_token_type varchar(100) NULL,
	access_token_scopes varchar(1000) NULL,
	oidc_id_token_value text NULL,
	oidc_id_token_issued_at timestamp NULL,
	oidc_id_token_expires_at timestamp NULL,
	oidc_id_token_metadata varchar(2000) NULL,
	refresh_token_value text NULL,
	refresh_token_issued_at timestamp NULL,
	refresh_token_expires_at timestamp NULL,
	refresh_token_metadata varchar(2000) NULL
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE oauth2_authorization ADD CONSTRAINT pk_oauth2_authorization PRIMARY KEY(id);
-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------

-- ----------------------------
-- Table structure for user_group_scope
-- ----------------------------
CREATE TABLE oauth2_authorization_consent (
	registered_client_id varchar(100) NOT NULL,
	principal_name varchar(200) NOT NULL,
	authorities varchar(1000) NOT NULL
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE oauth2_authorization_consent ADD CONSTRAINT pk_oauth2_authorization_consent PRIMARY KEY(registered_client_id, principal_name);
-- ----------------------------
-- Comment for table user_group_member
-- ----------------------------
