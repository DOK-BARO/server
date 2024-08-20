alter table book
	add created_at datetime default now() not null;

alter table book
	add updated_at datetime default now() not null on update now();

alter table book
	add deleted TINYINT(1) default 0 not null;

alter table book_author
	add created_at datetime default now() not null;

alter table book_author
	add updated_at datetime default now() not null on update now();

alter table book_author
	add deleted TINYINT(1) default 0 not null;

alter table book_category
	add created_at datetime default now() not null;

alter table book_category
	add updated_at datetime default now() not null on update now();

alter table book_category
	add deleted TINYINT(1) default 0 not null;

alter table book_category_group
	add created_at datetime default now() not null;

alter table book_category_group
	add updated_at datetime default now() not null on update now();

alter table book_category_group
	add deleted TINYINT(1) default 0 not null;
