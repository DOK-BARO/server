CREATE TABLE member
(
	id                bigint       NOT NULL AUTO_INCREMENT,
	certification_id  binary(16)   NOT NULL,
	nickname          varchar(127) NOT NULL,
	email             varchar(63)  NOT NULL,
	profile_image_url varchar(255)          DEFAULT NULL,
	created_at        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted           boolean      NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY member_pk (certification_id),
	UNIQUE KEY member_pk_2 (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


alter table oauth2_account
	add constraint oauth2_account_member_id_fk
		foreign key (member_id) references member (id)
			on delete cascade;

alter table account_password
	add constraint account_password_id_fk
		foreign key (member_id) references member (id)
			on delete cascade;

CREATE TABLE member_role
(
	id         bigint   NOT NULL AUTO_INCREMENT,
	member_id  bigint   NOT NULL,
	name       varchar(15)       DEFAULT NULL,
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY member_role_pk (member_id, name),
	CONSTRAINT member_role_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
