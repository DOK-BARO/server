CREATE TABLE study_group
(
	id                bigint       NOT NULL AUTO_INCREMENT,
	name              varchar(127) NOT NULL,
	introduction      text                  DEFAULT NULL,
	profile_image_url varchar(255)          DEFAULT NULL,
	invite_code       varchar(63)  NOT NULL,
	created_at        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted           boolean      NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY study_group_pk (invite_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE study_group_member
(
	id             bigint      NOT NULL AUTO_INCREMENT,
	study_group_id bigint      NOT NULL,
	member_id      bigint      NOT NULL,
	member_role    varchar(20) NOT NULL,
	created_at     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted        boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY study_group_member_pk (study_group_id, member_id),
	CONSTRAINT study_group_member_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id),
	CONSTRAINT study_group_member_study_group_id_fk FOREIGN KEY (study_group_id) REFERENCES study_group (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;