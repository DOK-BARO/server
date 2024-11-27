CREATE TABLE notification
(
	id                   bigint      NOT NULL AUTO_INCREMENT,
	content              blob        NOT NULL,
	notification_trigger varchar(63) NOT NULL,
	linked_id            bigint               DEFAULT NULL,
	image_url            varchar(255)         DEFAULT NULL,
	created_at           datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at           datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted              boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE notification_visibility
(
	id              bigint     NOT NULL AUTO_INCREMENT,
	notification_id bigint     NOT NULL,
	member_id       bigint     NOT NULL,
	checked         boolean    NOT NULL DEFAULT false,
	disabled        boolean    NOT NULL DEFAULT false,
	created_at      datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at      datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted         boolean    NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY notification_visibility_pk (member_id, notification_id),
	CONSTRAINT notification_visibility___fk FOREIGN KEY (notification_id) REFERENCES notification (id),
	CONSTRAINT notification_visibility_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;