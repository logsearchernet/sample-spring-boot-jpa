-- password is password
INSERT INTO sample_users(email,password,enabled) VALUES ('admin@a.com','$2a$10$rC8AaDh/2sjM1YaRd1eZl.uaAibD4vp2cK65TzCqUus3Y28e8JBCO', 1);
INSERT INTO sample_users(email,password,enabled) VALUES ('user@a.com','$2a$10$rC8AaDh/2sjM1YaRd1eZl.uaAibD4vp2cK65TzCqUus3Y28e8JBCO', 1);
 
INSERT INTO sample_user_roles (email, role) VALUES ('admin@a.com', 'ROLE_ADMIN');
INSERT INTO sample_user_roles (email, role) VALUES ('user@a.com', 'ROLE_USER');