INSERT INTO resource_scope(id, resource, scope, description, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', 'oauth', 'openid', '身份識別', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');
INSERT INTO resource_scope(id, resource, scope, description, created_date, created_by, last_modified_date, last_modified_by)
VALUES('2', 'user', 'profile', '個人資訊', CURRENT_TIMESTAMP, '１', CURRENT_TIMESTAMP, '1');

INSERT INTO user_account
(id, user_name, user_password, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_date, created_by, last_modified_date, last_modified_by)
VALUES('1', 'admin', '$2a$10$oaeeokjBmMdYIRQBzz8cde2Z1CxPIE7kfStPI4WeKNwfItbYJxE/W', true, false, false, false, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

INSERT INTO user_group(id, code, name, created_date, created_by, last_modified_date, last_modified_by) 
VALUES ('1', 'ADMIN', '管理群組', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, '1');

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