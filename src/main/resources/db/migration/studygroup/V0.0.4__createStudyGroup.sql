CREATE TABLE study_group
(
	id                bigint      NOT NULL AUTO_INCREMENT,
	name              varchar(50) NOT NULL,
	introduction      blob        NOT NULL,
	profile_image_url varchar(100)         DEFAULT NULL,
	created_at        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted           tinyint(1)  NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
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
	deleted        tinyint(1)  NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	UNIQUE KEY study_group_member_pk (study_group_id, member_id),
	KEY study_group_member_member_id_fk (member_id),
	CONSTRAINT study_group_member_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id),
	CONSTRAINT study_group_member_study_group_id_fk FOREIGN KEY (study_group_id) REFERENCES study_group (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;