create table if not exists items
(
    id            uuid primary key,
    name          varchar(255) not null,
    credit_card    varchar(255)
);

create table if not exists parking_spaces
(
    id serial primary key,
    name varchar(255) not null,
    status varchar(255) not null
);
