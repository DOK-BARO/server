create table account
(
	id         BIGINT auto_increment primary key,
	social_id  VARCHAR(50) not null,
	provider   VARCHAR(10) not null,
	created_at DATETIME    not null
);

create index account_social_id_index
	on account (social_id);