CREATE TABLE quiz_review
(
	id               bigint   NOT NULL AUTO_INCREMENT,
	star_rating      int      NOT NULL,
	difficulty_level int      NOT NULL,
	comment          blob,
	member_id        bigint   NOT NULL,
	quiz_id          bigint   NOT NULL,
	created_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted          tinyint  NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	CONSTRAINT quiz_review_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id),
	CONSTRAINT quiz_review_quiz_id_fk FOREIGN KEY (quiz_id) REFERENCES book_quiz (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;