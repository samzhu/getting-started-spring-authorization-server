INSERT INTO "scope"(id, resource, "scope", description) VALUES
('1', 'oauth', 'openid', 'Identification'),
('2', 'user', 'profile', 'Personal information');

INSERT INTO user_account(id, user_name, user_password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
('1', 'admin', '$2a$10$oaeeokjBmMdYIRQBzz8cde2Z1CxPIE7kfStPI4WeKNwfItbYJxE/W', true, false, false, false);

INSERT INTO user_group(id, "name", code) VALUES
('1', 'ADMIN', 'Manage groups');

INSERT INTO user_group_member(id, user_group_id, user_id) VALUES
('1', '1', '1');

INSERT INTO user_group_scope(id, user_group_id, scope_id) VALUES
('1', '1', '1'), ('2', '1', '2');

INSERT INTO oauth2_client (id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name) VALUES
('1', 'democlient', CURRENT_TIMESTAMP, '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', NULL, 'demo');

INSERT INTO oauth2_client_authentication_method(id, oauth2_client_id, authentication_method) VALUES
('1', '1', 'client_secret_post'), ('2', '1', 'client_secret_basic');

INSERT INTO oauth2_client_grant_type(id, oauth2_client_id, grant_type) VALUES
('1', '1', ''), ('2', '1', 'refresh_token'), ('3', '1', 'client_credentials');

INSERT INTO oauth2_client_redirect_uri(id, oauth2_client_id, redirect_uri) VALUES
('1', '1', 'https://oidcdebugger.com/debug');

INSERT INTO oauth2_client_scope(id, oauth2_client_id, scope_id) VALUES
('1', '1', '1'), ('2', '1', '2');

INSERT INTO oauth2_client_setting(id, oauth2_client_id, property_type, property_name, property_value) VALUES
('1', '1', 'client', 'require-proof-key', 'false'),
('2', '1', 'client', 'require-authorization-consent', 'true'),
('3', '1', 'token', 'id-token-signature-algorithm', 'RS256'),
('4', '1', 'token', 'access-token-time-to-live', '300'),
('5', '1', 'token', 'refresh-token-time-to-live', '3600'),
('6', '1', 'token', 'reuse-refresh-tokens', 'true');

