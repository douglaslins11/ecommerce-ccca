drop table IF EXISTS coupon;
drop table IF EXISTS product;

create table product
(
    id          integer,
    description text,
    price       numeric,
    width       numeric,
    height      numeric,
    length      numeric,
    weight      numeric
);

insert into product (id, description, price, width, height, length, weight) values (1, 'A', 1000, 100, 30, 10, 3);
insert into product (id, description, price, width, height, length, weight) values (2, 'B', 5000, 50, 50, 50, 22);
insert into product (id, description, price, width, height, length, weight) values (3, 'C', 30, 10, 10, 10, 0.9);
insert into product (id, description, price, width, height, length, weight) values (4, 'D', 30, -10, 10, 10, 0.9);
insert into product (id, description, price, width, height, length, weight) values (5, 'A', 1000, 100, 30, 10, 3);

create table coupon
(
    code        text,
    percentage  numeric,
    expire_date timestamp
);

insert into coupon (code, percentage, expire_date)
values ('VALE20', 20, '2023-10-01T10:00:00');
insert into coupon (code, percentage, expire_date)
values ('VALE10', 10, '2022-10-01T10:00:00');