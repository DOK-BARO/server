CREATE TABLE member
(
	id                bigint     NOT NULL AUTO_INCREMENT,
	certification_id  binary(16) NOT NULL,
	name              varchar(31)         DEFAULT NULL,
	nickname          varchar(31)         DEFAULT NULL,
	profile_image_url varchar(255)        DEFAULT NULL,
	created_at        datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at        datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted           boolean    NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY member_pk (certification_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


alter table account
	add constraint account_member_id_fk
		foreign key (member_id) references member (id)
			on delete cascade;

CREATE TABLE member_role
(
	id         bigint   NOT NULL AUTO_INCREMENT,
	member_id  bigint   NOT NULL,
	name       varchar(10)       DEFAULT NULL,
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY member_role_pk (member_id, name),
	CONSTRAINT member_role_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE member_email
(
	id         bigint      NOT NULL AUTO_INCREMENT,
	member_id  bigint      NOT NULL,
	address    varchar(31) NOT NULL,
	main       boolean     NOT NULL DEFAULT false,
	created_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY member_email_pk_2 (member_id, address),
	CONSTRAINT member_email_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
