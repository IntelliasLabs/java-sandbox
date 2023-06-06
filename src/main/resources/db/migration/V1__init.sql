create table if not exists items
(
    id            uuid primary key,
    name          varchar(255) not null,
    credit_card    varchar(255)
);

