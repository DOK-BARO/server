CREATE TABLE terms_of_service_detail
(
	id                  bigint   NOT NULL AUTO_INCREMENT,
	terms_of_service_id bigint   NOT NULL,
	content             blob     NOT NULL,
	created_at          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted             boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY terms_of_service_detail_pk (terms_of_service_id)
) ENGINE=InnoDB
	DEFAULT CHARSET=utf8mb4
	COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE agree_terms_of_service
(
	id                  bigint   NOT NULL AUTO_INCREMENT,
	terms_of_service_id bigint   NOT NULL,
	member_id           bigint   NOT NULL,
	created_at          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted             boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY terms_of_service_detail_pk (member_id,terms_of_service_id),
	CONSTRAINT terms_of_service_detail_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE=InnoDB
	DEFAULT CHARSET=utf8mb4
	COLLATE=utf8mb4_0900_ai_ci;
