CREATE TABLE email_authentication
(
	id            bigint      NOT NULL AUTO_INCREMENT,
	address       varchar(31) NOT NULL,
	code          varchar(16) NOT NULL,
	authenticated boolean     NOT NULL DEFAULT false,
	used          boolean     NOT NULL DEFAULT false,
	created_at    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted       boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

