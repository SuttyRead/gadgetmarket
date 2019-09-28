DROP TABLE if exists user;

create table if not exists user
(
    id         bigint auto_increment primary key,
    birthday   date         null,
    email      varchar(255) null unique,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    password   varchar(255) null,
    role       varchar(255) null,
    username   varchar(255) null unique
);