create table user_account
(
    id bigserial not null
        constraint user_account_pk
            primary key,
    name    varchar                                                  not null,
    surname varchar                                                  not null,
    type    boolean                                                  not null,
    avatar  bytea
);

alter table user_account
    owner to postgres;

create table user_password
(
    id bigserial not null
        constraint user_password_pk
            primary key,
    password varchar                                                                not null,
    id_user  bigint                                                                 not null
        constraint user_password_user_account_id_fk
            references user_account
);

alter table user_password
    owner to postgres;

create table product
(
    id          bigserial not null
        constraint product_pk
            primary key,
    name        varchar   not null,
    price       integer   not null,
    description varchar   not null,
    photo       bytea     not null
);

create table products_sellers
(
    id_product bigint not null
        constraint products_sellers_product_id_fk
            references product,
    id_seller  bigint not null
        constraint products_sellers_user_account_id_fk
            references user_account
);
