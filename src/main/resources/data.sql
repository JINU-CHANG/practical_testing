insert into product(product_number, type, selling_status, name, price)
values ('001', 'BOTTLE', 'SELLING', '아메리카노', 4000),
       ('002', 'BAKERY', 'HOLD', '카페라떼', 4500),
       ('003', 'HANDMADE', 'STOP_SELLING', '크루아상', 3500);

insert into stock(product_number, quantity)
values('001',2),
      ('002',2)
