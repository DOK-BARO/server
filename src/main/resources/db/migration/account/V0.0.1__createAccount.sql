CREATE TABLE account
(
	id         bigint      NOT NULL AUTO_INCREMENT,
	member_id  bigint      NOT NULL,
	social_id  varchar(50) NOT NULL,
	provider   varchar(10) NOT NULL,
	created_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id),
    UNIQUE KEY `account_pk` (`provider`,`social_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;