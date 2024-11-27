CREATE TABLE quiz_review_report
(
	id               bigint     NOT NULL AUTO_INCREMENT,
	report_member_id bigint     NOT NULL,
	quiz_review_id   bigint     NOT NULL,
	content          text       NOT NULL,
	used             boolean    NOT NULL DEFAULT false,
	created_at       datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at       datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted          boolean    NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	CONSTRAINT quiz_review_report_report_member_id_fk FOREIGN KEY (report_member_id) REFERENCES member (id),
	CONSTRAINT quiz_review_report_report_quiz_review_id_fk FOREIGN KEY (quiz_review_id) REFERENCES quiz_review (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
