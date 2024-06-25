create table role
(
	id         bigint auto_increment primary key,
	account_id bigint      not null references account (id),
	name       varchar(10) null
);

create index role__index
	on role (account_id);
