insert into category(id, name) values (1, 'Sample category')

insert into retailer(id, name) values (1, 'Sample retailer')

insert into product(id, active, title) values (1, 'TRUE', 'Sample product 1')
insert into product(id, active, title) values (2, 'TRUE', 'Sample product 2')
insert into product(id, active, title) values (3, 'FALSE', 'Sample inactive product 3')

insert into retail_offer(id, price, product_id, retailer_id) values (1, 100, 2, 1)
insert into retail_offer(id, price, product_id, retailer_id) values (2, 200, 2, 1)

insert into product_category(product_id, category_id) values (1, 1)


