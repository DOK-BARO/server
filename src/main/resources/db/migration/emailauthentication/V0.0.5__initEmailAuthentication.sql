CREATE TABLE email_authentication
(
	id            bigint      NOT NULL AUTO_INCREMENT,
	address       varchar(31) NOT NULL,
	code          varchar(16) NOT NULL,
	authenticated tinyint(1)  NOT NULL DEFAULT '0',
	used          tinyint(1)  NOT NULL DEFAULT '0',
	created_at    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted       tinyint(1)  NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

