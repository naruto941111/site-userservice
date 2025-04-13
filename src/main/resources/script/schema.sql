CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    token VARCHAR(1000) NULL,
    status int default 0
);


--for admin user password is admin123
--for test user password is test123
INSERT INTO users (username, password, email, status) VALUES
('admin123', '$2a$10$QaO0YFRXs8bqivtIaYskEe/Ghoc1HiS7I2M7fd9HBQGM0KjpT3pf2', 'admin@dummy.com', 1),
('test123', '$2a$10$tdE/YzrTpvZNsxdw/Iw6p.xGiMxNV.3NgiUmGIdj0q3KDtATBUYPO', 'test@dummy.com', 1);

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