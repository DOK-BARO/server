CREATE TABLE oauth2_account
(
	id         bigint       NOT NULL AUTO_INCREMENT,
	member_id  bigint       NOT NULL,
	social_id  varchar(127) NOT NULL,
	provider   varchar(31)  NOT NULL,
	created_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean      NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY account_pk (provider, social_id),
	UNIQUE KEY account_pk_2 (provider, member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


create table account_password
(
	id         bigint       NOT NULL AUTO_INCREMENT,
	password   varchar(255) NOT NULL,
	member_id  bigint       NOT NULL,
	created_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean      NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY account_password_pk (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
