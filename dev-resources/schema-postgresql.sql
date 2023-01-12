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
ALTER TABLE scope ADD CONSTRAINT pk_id PRIMARY KEY(id);
ALTER TABLE scope ADD CONSTRAINT uk_resource_scope UNIQUE (resource, scope);
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
ALTER TABLE user_account ADD CONSTRAINT pk_id PRIMARY KEY(id);
ALTER TABLE user_account ADD CONSTRAINT uk_user_account UNIQUE (user_account);
-- ----------------------------
-- Comment for table user_account
-- ----------------------------
COMMENT ON TABLE user_account IS 'Account information';
COMMENT ON COLUMN user_account.user_name IS 'user account';
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
	code varchar(20) NOT NULL,
	"name" varchar(50) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- ----------------------------
-- Constraint for table user_group
-- ----------------------------
ALTER TABLE user_group ADD CONSTRAINT pk_id PRIMARY KEY(id);
ALTER TABLE user_group ADD CONSTRAINT uk_code UNIQUE (code);
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
	user_id varchar(10) NOT NULL,
	user_group_id varchar(10) NOT NULL,
	created_by varchar(30) NOT NULL,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified_by varchar(30) NOT NULL,
	last_modified_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP

	CONSTRAINT fk_user_group_member_user_group_id FOREIGN KEY (user_group_id) REFERENCES public.user_group(id),
	CONSTRAINT fk_user_group_member_user_id FOREIGN KEY (user_id) REFERENCES public.user_account(id)
);
-- ----------------------------
-- Constraint for table user_group_member
-- ----------------------------
ALTER TABLE user_group_member ADD CONSTRAINT pk_id PRIMARY KEY(id);
ALTER TABLE user_group_member ADD CONSTRAINT uk_user_id_user_group_id UNIQUE (user_id, user_group_id);



COMMENT ON TABLE public.user_group_member IS 'User group corresponding data';
-- Column comments

COMMENT ON COLUMN public.user_group_member.user_id IS 'Account ID';
COMMENT ON COLUMN public.user_group_member.user_group_id IS 'Group ID';








