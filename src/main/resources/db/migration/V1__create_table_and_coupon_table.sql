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
    weight      numeric,
    currency text
);

insert into product (id, description, price, width, height, length, weight, currency) values (1, 'A', 1000, 100, 30, 10, 3, 'BRL');
insert into product (id, description, price, width, height, length, weight, currency) values (2, 'B', 5000, 50, 50, 50, 22, 'BRL');
insert into product (id, description, price, width, height, length, weight, currency) values (3, 'C', 30, 10, 10, 10, 0.9, 'BRL');
insert into product (id, description, price, width, height, length, weight, currency) values (4, 'D', 30, -10, 10, 10, 0.9, 'BRL');
insert into product (id, description, price, width, height, length, weight, currency) values (5, 'A', 1000, 100, 30, 10, 3, 'USD');

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