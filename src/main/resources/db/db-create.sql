CREATE TABLE users
(
    id        int8         not null,
    username  varchar(255) not null unique,
    password  varchar(255) not null,
    full_name varchar(255),
    email     varchar(255),
    bio       varchar(255),
    reg_date  timestamp,
    primary key (id)
);

create table user_authorities
(
    user_id     int8 not null,
    authorities varchar(255)
);

create table tours
(
    id          int8         not null,
    name        varchar(255) not null unique,
    type        varchar(255),
    price       int,
    group_size  int,
    hotel       varchar(255),
    description varchar(255),
    is_hot      boolean
);

drop table user_authorities;
drop table users;
