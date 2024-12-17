CREATE TABLE quiz_question_report
(
	id               bigint   NOT NULL AUTO_INCREMENT,
	report_member_id bigint   NOT NULL,
	quiz_question_id bigint   NOT NULL,
	content          text     NOT NULL,
	created_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted          boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	CONSTRAINT quiz_question_report_report_member_id_fk FOREIGN KEY (report_member_id) REFERENCES member (id),
	CONSTRAINT quiz_question_report_quiz_question_id_fk FOREIGN KEY (quiz_question_id) REFERENCES book_quiz_question (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
