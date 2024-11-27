CREATE TABLE solving_quiz
(
	id         bigint   NOT NULL AUTO_INCREMENT,
	member_id  bigint   NOT NULL,
	quiz_id    bigint   NOT NULL,
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	CONSTRAINT solving_quiz_member_id_fk FOREIGN KEY (member_id) REFERENCES member (id),
	CONSTRAINT solving_quiz_quiz_id_fk FOREIGN KEY (quiz_id) REFERENCES book_quiz (id)
) ENGINE=InnoDB
	DEFAULT CHARSET=utf8mb4
	COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE solving_quiz_sheet
(
	id              bigint       NOT NULL AUTO_INCREMENT,
	solving_quiz_id bigint       NOT NULL,
	question_id     bigint       NOT NULL,
	content         varchar(255) NOT NULL,
	created_at      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted         boolean      NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	CONSTRAINT solving_quiz_sheet_solving_quiz_id_fk FOREIGN KEY (solving_quiz_id) REFERENCES solving_quiz (id),
	CONSTRAINT solving_quiz_sheet_question_id_fk FOREIGN KEY (question_id) REFERENCES book_quiz_question (id)
) ENGINE=InnoDB
	DEFAULT CHARSET=utf8mb4
	COLLATE=utf8mb4_0900_ai_ci;