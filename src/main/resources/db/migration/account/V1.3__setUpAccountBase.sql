alter table account
	alter column created_at set default (now());

alter table account
	add updated_at datetime default now() not null on update now();

alter table account
	add deleted TINYINT(1) default 0 not null;


rename table role to account_role;

alter table account_role
	add created_at datetime default now() not null;

alter table account_role
	add updated_at datetime default now() not null on update now();

alter table account_role
	add deleted TINYINT(1) default 0 not null;
