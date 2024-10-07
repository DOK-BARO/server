CREATE TABLE book_quiz
(
	id                bigint       NOT NULL AUTO_INCREMENT,
	title             varchar(127) NOT NULL,
	description       blob,
	creator_id        bigint       NOT NULL,
	book_id           bigint       NOT NULL,
	time_limit_second int,
	view_scope        varchar(31)  NOT NULL,
	edit_scope        varchar(31)  NOT NULL,
	created_at        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted           tinyint(1)   NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	KEY               book_quiz___fk (creator_id),
	KEY               book_quiz_book_id_fk (book_id),
	CONSTRAINT book_quiz___fk FOREIGN KEY (creator_id) REFERENCES member (id),
	CONSTRAINT book_quiz_book_id_fk FOREIGN KEY (book_id) REFERENCES book (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE book_quiz_question
(
	id               bigint      NOT NULL AUTO_INCREMENT,
	question_content blob        NOT NULL,
	book_quiz_id     bigint      NOT NULL,
	question_type    varchar(31) NOT NULL,
	explanation      blob        NOT NULL,
	created_at       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted          tinyint(1)  NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	KEY              book_quiz_question_book_quiz_id_fk (book_quiz_id),
	CONSTRAINT book_quiz_question_book_quiz_id_fk FOREIGN KEY (book_quiz_id) REFERENCES book_quiz (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE book_quiz_select_option
(
	id                    bigint   NOT NULL AUTO_INCREMENT,
	content               blob     NOT NULL,
	seq                   int      NOT NULL,
	book_quiz_question_id bigint   NOT NULL,
	created_at            datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at            datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted               tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	UNIQUE KEY book_quiz_select_option_pk (book_quiz_question_id, seq),
	KEY                   book_quiz_select_option_book_quiz_question_id_fk (book_quiz_question_id),
	CONSTRAINT book_quiz_select_option_book_quiz_question_id_fk FOREIGN KEY (book_quiz_question_id) REFERENCES book_quiz_question (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE book_quiz_answer
(
	id                    bigint       NOT NULL AUTO_INCREMENT,
	content               varchar(255) NOT NULL,
	book_quiz_question_id bigint       NOT NULL,
	created_at            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted               tinyint(1)   NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	KEY                   book_quiz_answer___fk (book_quiz_question_id),
	CONSTRAINT book_quiz_answer___fk FOREIGN KEY (book_quiz_question_id) REFERENCES book_quiz_question (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE study_group_quiz
(
	id             bigint   NOT NULL AUTO_INCREMENT,
	study_group_id bigint   NOT NULL,
	book_quiz_id   bigint   NOT NULL,
	created_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted        tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (id),
	UNIQUE KEY study_group_quiz_pk (study_group_id, book_quiz_id),
	KEY            study_group_quiz___fk (book_quiz_id),
	CONSTRAINT study_group_quiz___fk FOREIGN KEY (book_quiz_id) REFERENCES book_quiz (id),
	CONSTRAINT study_group_quiz_study_group_id_fk FOREIGN KEY (study_group_id) REFERENCES study_group (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;