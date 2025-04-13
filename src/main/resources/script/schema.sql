CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity int NOT NULL,
    price int NOT NULL,
    status int default 0,
    created_by int NOT NULL,
    modified_by int default NULL
);


INSERT INTO orders (created_by, product_id, quantity, price, status) VALUES
(1, 1, 5, 10.0, 1),
(2, 4, 7, 12.50, 1);