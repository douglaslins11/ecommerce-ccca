drop table IF EXISTS coupon;
drop table IF EXISTS product;

create table product
(
    id  integer,
    description text,
    price       numeric
);

insert into product (id, description, price)
values (1, 'A', 1000);
insert into product (id, description, price)
values (2, 'B', 5000);
insert into product (id, description, price)
values (3, 'C', 30);

create table coupon (
    code       text,
    percentage numeric
);

insert into coupon (code, percentage)
values ('VALE20', 20);