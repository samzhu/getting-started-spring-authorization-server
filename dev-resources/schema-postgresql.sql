-- ----------------------------
-- Table structure for permission
-- ----------------------------
CREATE TABLE permission (
	id varchar(10) NOT NULL,
	resource varchar(50) NOT NULL,
    permission varchar(50) NOT NULL,
    description varchar(50) NOT NULL,
	created_by varchar(60) NOT NULL,
	created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(60) NOT NULL,
	last_modified_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------
-- Constraint for table permission
-- ----------------------------
ALTER TABLE permission ADD CONSTRAINT permission_pk PRIMARY KEY(id);
ALTER TABLE permission ADD CONSTRAINT permission_uk_resource_permission UNIQUE (resource, permission);

-- ----------------------------
-- Comment for table permission
-- ----------------------------
COMMENT ON TABLE permission IS 'resource permissions';
COMMENT ON COLUMN permission.id IS 'unique value';
COMMENT ON COLUMN permission.resource IS 'resources name';
COMMENT ON COLUMN permission.permission IS 'permission code';
COMMENT ON COLUMN permission.permission IS 'describe';




-- Drop table

-- DROP TABLE public.oauth2_authorization;

CREATE TABLE public.oauth2_authorization (
	id varchar(100) NOT NULL,
	registered_client_id varchar(100) NOT NULL,
	principal_name varchar(200) NOT NULL,
	authorization_grant_type varchar(100) NOT NULL,
	"attributes" varchar(4000) NULL DEFAULT 'NULL::character varying'::character varying,
	state varchar(500) NULL DEFAULT 'NULL::character varying'::character varying,
	authorization_code_value text NULL,
	authorization_code_issued_at timestamp NULL,
	authorization_code_expires_at timestamp NULL,
	authorization_code_metadata varchar(2000) NULL DEFAULT 'NULL::character varying'::character varying,
	access_token_value text NULL,
	access_token_issued_at timestamp NULL,
	access_token_expires_at timestamp NULL,
	access_token_metadata varchar(2000) NULL DEFAULT 'NULL::character varying'::character varying,
	access_token_type varchar(100) NULL DEFAULT 'NULL::character varying'::character varying,
	access_token_scopes varchar(1000) NULL DEFAULT 'NULL::character varying'::character varying,
	oidc_id_token_value text NULL,
	oidc_id_token_issued_at timestamp NULL,
	oidc_id_token_expires_at timestamp NULL,
	oidc_id_token_metadata varchar(2000) NULL DEFAULT 'NULL::character varying'::character varying,
	refresh_token_value text NULL,
	refresh_token_issued_at timestamp NULL,
	refresh_token_expires_at timestamp NULL,
	refresh_token_metadata varchar(2000) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_authorization PRIMARY KEY (id)
);


-- public.oauth2_authorization_consent definition

-- Drop table

-- DROP TABLE public.oauth2_authorization_consent;

CREATE TABLE public.oauth2_authorization_consent (
	registered_client_id varchar(100) NOT NULL,
	principal_name varchar(200) NOT NULL,
	authorities varchar(1000) NOT NULL,
	CONSTRAINT pk_oauth2_authorization_consent PRIMARY KEY (registered_client_id, principal_name)
);


-- public.oauth2_client definition

-- Drop table

-- DROP TABLE public.oauth2_client;

CREATE TABLE public.oauth2_client (
	id varchar(10) NOT NULL,
	client_id varchar(100) NOT NULL, -- Client account
	client_id_issued_at timestamp NOT NULL DEFAULT now(), -- issuing time
	client_secret varchar(200) NULL DEFAULT 'NULL::character varying'::character varying, -- secret
	client_secret_expires_at timestamp NULL, -- Secret expiration time
	client_name varchar(200) NOT NULL, -- name
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_client PRIMARY KEY (id),
	CONSTRAINT uk_oauth2_client UNIQUE (client_id)
);
COMMENT ON TABLE public.oauth2_client IS 'OAuth Client';

-- Column comments

COMMENT ON COLUMN public.oauth2_client.client_id IS 'Client account';
COMMENT ON COLUMN public.oauth2_client.client_id_issued_at IS 'issuing time';
COMMENT ON COLUMN public.oauth2_client.client_secret IS 'secret';
COMMENT ON COLUMN public.oauth2_client.client_secret_expires_at IS 'Secret expiration time';
COMMENT ON COLUMN public.oauth2_client.client_name IS 'name';


-- public.resource_scope definition

-- Drop table

-- DROP TABLE public.resource_scope;

CREATE TABLE public.resource_scope (
	id varchar(10) NOT NULL,
	resource varchar(50) NOT NULL, -- resources name
	"scope" varchar(50) NOT NULL, -- access scope
	description varchar(50) NOT NULL, -- description
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_resource_scope PRIMARY KEY (id),
	CONSTRAINT uk_resource_scope_resource_scope UNIQUE (resource, scope),
	CONSTRAINT uk_resource_scope_scope UNIQUE (scope)
);
COMMENT ON TABLE public.resource_scope IS 'Access authority information';

-- Column comments

COMMENT ON COLUMN public.resource_scope.resource IS 'resources name';
COMMENT ON COLUMN public.resource_scope."scope" IS 'access scope';
COMMENT ON COLUMN public.resource_scope.description IS 'description';


-- public.user_account definition

-- Drop table

-- DROP TABLE public.user_account;

CREATE TABLE public.user_account (
	id varchar(36) NOT NULL,
	user_name varchar(30) NOT NULL, -- user account
	user_password varchar(60) NOT NULL, -- user password
	enabled bool NOT NULL, -- enable
	account_non_expired bool NOT NULL, -- expired
	account_non_locked bool NOT NULL, -- account locked
	credentials_non_expired bool NOT NULL, -- certificate has expired
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NOT NULL,
	last_modified_by varchar(36) NOT NULL,
	CONSTRAINT pk_user_account PRIMARY KEY (id),
	CONSTRAINT uk_user_account UNIQUE (user_name)
);
COMMENT ON TABLE public.user_account IS 'Account information';

-- Column comments

COMMENT ON COLUMN public.user_account.user_name IS 'user account';
COMMENT ON COLUMN public.user_account.user_password IS 'user password';
COMMENT ON COLUMN public.user_account.enabled IS 'enable';
COMMENT ON COLUMN public.user_account.account_non_expired IS 'expired';
COMMENT ON COLUMN public.user_account.account_non_locked IS 'account locked';
COMMENT ON COLUMN public.user_account.credentials_non_expired IS 'certificate has expired';


-- public.user_group definition

-- Drop table

-- DROP TABLE public.user_group;

CREATE TABLE public.user_group (
	id varchar(10) NOT NULL,
	code varchar(20) NOT NULL, -- Group code
	"name" varchar(50) NOT NULL, -- Group name
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_user_group PRIMARY KEY (id),
	CONSTRAINT uk_user_group UNIQUE (name)
);
COMMENT ON TABLE public.user_group IS 'Group data';

-- Column comments

COMMENT ON COLUMN public.user_group.code IS 'Group code';
COMMENT ON COLUMN public.user_group."name" IS 'Group name';


-- public.oauth2_client_authentication_method definition

-- Drop table

-- DROP TABLE public.oauth2_client_authentication_method;

CREATE TABLE public.oauth2_client_authentication_method (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL, -- OAuth Client ID
	authentication_method varchar(20) NOT NULL, -- verification method
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_client_authentication_method PRIMARY KEY (id),
	CONSTRAINT uk_oauth2_client_authentication_method UNIQUE (oauth2_client_id, authentication_method),
	CONSTRAINT fk_oauth2_client_authentication_method_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES public.oauth2_client(id)
);
COMMENT ON TABLE public.oauth2_client_authentication_method IS 'oauth client verification method';

-- Column comments

COMMENT ON COLUMN public.oauth2_client_authentication_method.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN public.oauth2_client_authentication_method.authentication_method IS 'verification method';


-- public.oauth2_client_grant_type definition

-- Drop table

-- DROP TABLE public.oauth2_client_grant_type;

CREATE TABLE public.oauth2_client_grant_type (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL, -- OAuth Client ID
	grant_type varchar(20) NOT NULL, -- Authorization method
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_client_grant_type PRIMARY KEY (id),
	CONSTRAINT uk_oauth2_client_grant_type UNIQUE (oauth2_client_id, grant_type),
	CONSTRAINT fk_oauth2_client_grant_type_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES public.oauth2_client(id)
);
COMMENT ON TABLE public.oauth2_client_grant_type IS 'oauth client authorization method';

-- Column comments

COMMENT ON COLUMN public.oauth2_client_grant_type.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN public.oauth2_client_grant_type.grant_type IS 'Authorization method';


-- public.oauth2_client_redirect_uri definition

-- Drop table

-- DROP TABLE public.oauth2_client_redirect_uri;

CREATE TABLE public.oauth2_client_redirect_uri (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL, -- OAuth Client ID
	redirect_uri varchar(50) NOT NULL, -- Redirection URL
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_client_redirect_uri PRIMARY KEY (id),
	CONSTRAINT uk_oauth2_client_redirect_uri UNIQUE (oauth2_client_id, redirect_uri),
	CONSTRAINT fk_oauth2_client_redirect_uri_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES public.oauth2_client(id)
);
COMMENT ON TABLE public.oauth2_client_redirect_uri IS 'oauth client redirection URL';

-- Column comments

COMMENT ON COLUMN public.oauth2_client_redirect_uri.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN public.oauth2_client_redirect_uri.redirect_uri IS 'Redirection URL';


-- public.oauth2_client_scope definition

-- Drop table

-- DROP TABLE public.oauth2_client_scope;

CREATE TABLE public.oauth2_client_scope (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL, -- OAuth Client ID
	resource_scope_id varchar(10) NOT NULL, -- scope ID
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_client_scope PRIMARY KEY (id),
	CONSTRAINT uk_oauth2_client_scope UNIQUE (oauth2_client_id, resource_scope_id),
	CONSTRAINT fk_oauth2_client_scope_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES public.oauth2_client(id),
	CONSTRAINT fk_oauth2_client_scope_resource_scope_id FOREIGN KEY (resource_scope_id) REFERENCES public.resource_scope(id)
);
COMMENT ON TABLE public.oauth2_client_scope IS 'client scope';

-- Column comments

COMMENT ON COLUMN public.oauth2_client_scope.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN public.oauth2_client_scope.resource_scope_id IS 'scope ID';


-- public.oauth2_client_setting definition

-- Drop table

-- DROP TABLE public.oauth2_client_setting;

CREATE TABLE public.oauth2_client_setting (
	id varchar(10) NOT NULL,
	oauth2_client_id varchar(10) NOT NULL, -- OAuth Client ID
	property_type varchar(20) NOT NULL, -- Type of setting
	property_name varchar(50) NOT NULL, -- Attribute name
	property_value varchar(100) NOT NULL, -- Attribute value
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_oauth2_client_setting PRIMARY KEY (id),
	CONSTRAINT uk_oauth2_client_setting UNIQUE (oauth2_client_id, property_type, property_name),
	CONSTRAINT fk_oauth2_client_setting_oauth2_client_id FOREIGN KEY (oauth2_client_id) REFERENCES public.oauth2_client(id)
);
COMMENT ON TABLE public.oauth2_client_setting IS 'client config';

-- Column comments

COMMENT ON COLUMN public.oauth2_client_setting.oauth2_client_id IS 'OAuth Client ID';
COMMENT ON COLUMN public.oauth2_client_setting.property_type IS 'Type of setting';
COMMENT ON COLUMN public.oauth2_client_setting.property_name IS 'Attribute name';
COMMENT ON COLUMN public.oauth2_client_setting.property_value IS 'Attribute value';


-- public.user_group_member definition

-- Drop table

-- DROP TABLE public.user_group_member;

CREATE TABLE public.user_group_member (
	id varchar(10) NOT NULL,
	user_id varchar(10) NOT NULL, -- Account ID
	user_group_id varchar(10) NOT NULL, -- Group ID
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_user_group_member PRIMARY KEY (id),
	CONSTRAINT uk_user_group_member UNIQUE (user_id, user_group_id),
	CONSTRAINT fk_user_group_member_user_group_id FOREIGN KEY (user_group_id) REFERENCES public.user_group(id),
	CONSTRAINT fk_user_group_member_user_id FOREIGN KEY (user_id) REFERENCES public.user_account(id)
);
COMMENT ON TABLE public.user_group_member IS 'User group corresponding data';

-- Column comments

COMMENT ON COLUMN public.user_group_member.user_id IS 'Account ID';
COMMENT ON COLUMN public.user_group_member.user_group_id IS 'Group ID';


-- public.user_group_scope definition

-- Drop table

-- DROP TABLE public.user_group_scope;

CREATE TABLE public.user_group_scope (
	id varchar(10) NOT NULL,
	user_group_id varchar(10) NOT NULL, -- Group ID
	resource_scope_id varchar(10) NOT NULL, -- Resource scope ID
	created_date timestamp NOT NULL,
	created_by varchar(36) NOT NULL,
	last_modified_date timestamp NULL,
	last_modified_by varchar(36) NULL DEFAULT 'NULL::character varying'::character varying,
	CONSTRAINT pk_user_group_scope PRIMARY KEY (id),
	CONSTRAINT uk_user_group_scope UNIQUE (user_group_id, resource_scope_id),
	CONSTRAINT fk_user_group_scope_resource_scope_id FOREIGN KEY (resource_scope_id) REFERENCES public.resource_scope(id),
	CONSTRAINT fk_user_group_scope_user_group_id FOREIGN KEY (user_group_id) REFERENCES public.user_group(id)
);
COMMENT ON TABLE public.user_group_scope IS 'Group permissions data';

-- Column comments

COMMENT ON COLUMN public.user_group_scope.user_group_id IS 'Group ID';
COMMENT ON COLUMN public.user_group_scope.resource_scope_id IS 'Resource scope ID';