create table user_account
(
    id bigserial not null
        constraint user_account_pk
            primary key,
    name    varchar                                                  not null,
    surname varchar                                                  not null,
    password varchar                                                 not null,
    type    boolean                                                  not null,
    avatar  bytea
);

alter table user_account
    owner to postgres;

create table category
(
    id            bigserial not null,
    name varchar   not null
);
alter table category
    add constraint category_pk
        primary key (id);

create table subcategory
(
    id               bigserial not null
        constraint subcategory_pk
            primary key,
    name varchar   not null,
    category_id  bigint not null
        constraint subcategory_category_category_id_fk
            references category (id)
);

create table product
(
    id          bigserial not null
        constraint product_pk
            primary key,
    name        varchar   not null,
    description varchar   not null,
    photo       bytea     not null,
    subcategory_id bigint not null,
    constraint product_subcategory_id_fk foreign key (subcategory_id)
    references subcategory
);

create table products_sellers
(
    id_product bigint not null
        constraint products_sellers_product_id_fk
            references product,
    id_seller  bigint not null
        constraint products_sellers_user_account_id_fk
            references user_account,
    price decimal not null,
    quantity bigint not null
);