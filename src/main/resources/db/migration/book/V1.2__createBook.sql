create table book
(
	id           bigint auto_increment
		primary key,
	isbn         char(14)     not null,
	title        varchar(70)  not null,
	publisher    varchar(30)  not null,
	published_at date         not null,
	price        int          not null,
	description  blob         null,
	image_url    varchar(100) null,
	constraint isbn
		unique (isbn)
);

create table book_author
(
	id      bigint auto_increment
		primary key,
	book_id bigint      not null,
	name    varchar(50) not null,
	constraint book_author_book_id_fk
		foreign key (book_id) references book (id)
);

create table book_category
(
	id           bigint      not null
		primary key,
	english_name varchar(50) not null,
	korean_name  varchar(50) not null,
	parent_id    bigint      null,
	constraint book_category_book_category_id_fk
		foreign key (parent_id) references book_category (id)
			on delete set null
);

create table book_category_group
(
	id               bigint auto_increment
		primary key,
	book_id          bigint not null,
	book_category_id bigint not null,
	constraint book_category
		unique (book_id, book_category_id),
	constraint book_category_group_book_category_id_fk
		foreign key (book_category_id) references book_category (id),
	constraint book_category_of_book___fk
		foreign key (book_id) references book (id)
);

