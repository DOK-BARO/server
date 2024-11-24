CREATE TABLE book
(
	id           bigint      NOT NULL AUTO_INCREMENT,
	isbn         char(14)    NOT NULL,
	title        varchar(70) NOT NULL,
	publisher    varchar(30) NOT NULL,
	published_at date        NOT NULL,
	price        int         NOT NULL,
	description  blob,
	image_url    varchar(100)         DEFAULT NULL,
	created_at   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted      boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY isbn (isbn)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE book_author
(
	id         bigint      NOT NULL AUTO_INCREMENT,
	book_id    bigint      NOT NULL,
	name       varchar(50) NOT NULL,
	created_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted    boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	CONSTRAINT book_author_book_id_fk FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE book_category
(
	id           bigint      NOT NULL AUTO_INCREMENT,
	english_name varchar(50) NOT NULL,
	korean_name  varchar(50) NOT NULL,
	parent_id    bigint               DEFAULT NULL,
	created_at   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted      boolean     NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	CONSTRAINT book_category_book_category_id_fk FOREIGN KEY (parent_id) REFERENCES book_category (id) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO book_category(id, english_name, korean_name) VALUES (1, 'ROOT', 'ROOT');

CREATE TABLE book_category_group
(
	id               bigint   NOT NULL AUTO_INCREMENT,
	book_id          bigint   NOT NULL,
	book_category_id bigint   NOT NULL,
	created_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	deleted          boolean  NOT NULL DEFAULT false,
	PRIMARY KEY (id),
	UNIQUE KEY book_category (book_id, book_category_id),
	CONSTRAINT book_category_group_book_category_id_fk FOREIGN KEY (book_category_id) REFERENCES book_category (id),
	CONSTRAINT book_category_of_book___fk FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
