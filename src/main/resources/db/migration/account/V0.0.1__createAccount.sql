CREATE TABLE account
(
	id         bigint      NOT NULL AUTO_INCREMENT,
	social_id  varchar(50) NOT NULL,
	provider   varchar(10) NOT NULL,
	created_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE account_role
(
	id         bigint   NOT NULL AUTO_INCREMENT,
	account_id bigint   NOT NULL,
	name       varchar(10)       DEFAULT NULL,
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY account_role_pk (account_id, name),
	CONSTRAINT account_role_account_id_fk FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;