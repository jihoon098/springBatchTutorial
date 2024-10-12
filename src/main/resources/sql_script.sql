-- 주문 테이블 생성
CREATE TABLE study.orders (
    id INT NOT NULL AUTO_INCREMENT
    , order_item VARCHAR(50)
    , price INT
    , order_date DATE
    , PRIMARY KEY (id)
);

-- 정산 테이블 생성
CREATE TABLE study.accounts (
    id INT NOT NULL AUTO_INCREMENT
    , order_item VARCHAR(50)
    , price INT
    , order_date DATE
    , account_date DATE
    , PRIMARY KEY (id)
);

INSERT INTO study.orders(order_item, price, order_date) values ('카카오 선물', 15000, '2022-03-01');
INSERT INTO study.orders(order_item, price, order_date) values ('배달 주문', 18000, '2022-03-01');
INSERT INTO study.orders(order_item, price, order_date) values ('교보문고', 14000, '2022-03-02');
INSERT INTO study.orders(order_item, price, order_date) values ('아이스크림', 4000, '2022-03-03');
INSERT INTO study.orders(order_item, price, order_date) values ('치킨', 22000, '2022-03-04');
INSERT INTO study.orders(order_item, price, order_date) values ('커피', 3500, '2022-03-04');
INSERT INTO study.orders(order_item, price, order_date) values ('교보문고', 17000, '2022-03-05');
INSERT INTO study.orders(order_item, price, order_date) values ('햄버거', 12500, '2022-03-06');

SELECT *
FROM study.orders;

SELECT *
FROM study.accounts;