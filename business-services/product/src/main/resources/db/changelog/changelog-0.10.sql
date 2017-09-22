--liquibase formatted SQL

--changeset gavin:1
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id`        CHAR(6)     NOT NULL,
  `name`      VARCHAR(45) NOT NULL,
  `parent_id` CHAR(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id`            CHAR(36)     NOT NULL,
  `name`          VARCHAR(100) NOT NULL,
  `category_id`   CHAR(6)      NOT NULL,
  `price`         FLOAT        NOT NULL,
  `stocks`        INT(10)      NOT NULL,
  `comment`       VARCHAR(200)          DEFAULT NULL,
  `version`       BIGINT(20)            DEFAULT NULL,
  `created_time`  TIMESTAMP    NOT NULL DEFAULT current_timestamp,
  `modified_time` TIMESTAMP    NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (`id`),
  KEY `category_id_fk_idx` (`category_id`),
  CONSTRAINT `product_category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `point_reward_plan`;
CREATE TABLE `point_reward_plan` (
  `id`           CHAR(36)  NOT NULL,
  `product_id`   CHAR(36)  NOT NULL,
  `ratio`        FLOAT     NOT NULL,
  `start_date`   CHAR(10)  NOT NULL,
  `end_date`     CHAR(10)  NOT NULL,
  `version`      BIGINT(20)         DEFAULT NULL,
  `created_time` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `point_reward_plan_id_uindex` (`id`),
  KEY `point_reward_plan_product_id_fk` (`product_id`),
  CONSTRAINT `point_reward_plan_product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `product_reservation`;
CREATE TABLE `product_reservation` (
  `id`           CHAR(36)  NOT NULL,
  `product_id`   CHAR(36)  NOT NULL,
  `order_id`     CHAR(36)  NOT NULL,
  `quantity`     INT(10)   NOT NULL,
  `version`      BIGINT(20)         DEFAULT NULL,
  `created_time` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_reservation_id_uindex` (`id`),
  KEY `product_reservation_product_id_fk` (`product_id`),
  CONSTRAINT `product_reservation_product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

--changeset gavin:2
INSERT INTO schema_product.category (id, NAME, parent_id) VALUES ('a00001', '衣服', NULL );
INSERT INTO schema_product.category (id, name, parent_id) VALUES ('a00002', '鞋子', NULL);

INSERT INTO schema_product.category (id, name, parent_id) VALUES ('b01001', '外套', 'a00001');
INSERT INTO schema_product.category (id, name, parent_id) VALUES ('b01002', '衬衫', 'a00001');
INSERT INTO schema_product.category (id, name, parent_id) VALUES ('b01003', 't恤', 'a00001');

INSERT INTO schema_product.category (id, name, parent_id) VALUES ('b02001', '皮鞋', 'a00002');
INSERT INTO schema_product.category (id, name, parent_id) VALUES ('b02002', '凉鞋', 'a00002');
INSERT INTO schema_product.category (id, name, parent_id) VALUES ('b02003', '运动鞋', 'a00002');

INSERT INTO schema_product.product (id, name, category_id, price, stocks, comment, version, created_time, modified_time)
VALUES
  ('0e5b6fbd-8b6d-43e1-9c7b-ed4c00f319a9', '夏季防晒衣男士夹克薄外套', 'b01001', 149,
   1000, '70% wool/20% nylon/10% cashmere', 1, '2016-10-30 02:08:22',
   '2016-11-13 04:33:06');
INSERT INTO schema_product.product (id, name, category_id, price, stocks, comment, version, created_time, modified_time)
VALUES
  ('181c105f-1fa3-44c7-9793-090a19a6c897', '日系休闲情侣款修身牛仔夹克', 'b01001', 60.5, 1000,
   '', 1, '2016-10-30 01:56:52',
   '2016-10-30 01:56:52');
INSERT INTO schema_product.product (id, name, category_id, price, stocks, comment, version, created_time, modified_time)
VALUES
  ('545524a6-c3ec-47f2-9423-f4ee2f9959f0', '韩版休闲男士免烫衬衫', 'b01002', 53.9, 1000,
   'textile and synthetic', 1, '2016-10-30 02:09:56',
   '2016-11-12 06:42:39');
INSERT INTO schema_product.product (id, name, category_id, price, stocks, comment, version, created_time, modified_time)
VALUES
  ('668f3eac-b7cc-4a66-9012-48cb09ea0fea', '爱登堡新贵男士短袖衬衫', 'b01002', 42, 1000,
   'soft, durable man-made upper', 1,
   '2016-10-30 01:18:05', '2016-11-12 06:42:37');
INSERT INTO schema_product.product (id, name, category_id, price, stocks, comment, version, created_time, modified_time)
VALUES
  ('7395260f-41a7-44a1-bfc8-ae1c23f8afdc', '夏季新款短袖丝光棉t恤', 'b01003', 9, 2000,
   '', 1,
   '2016-10-30 07:33:03', '2016-11-12 06:42:34');
INSERT INTO schema_product.product (id, name, category_id, price, stocks, comment, version, created_time, modified_time)
VALUES
  ('cb4d3cff-df04-4558-b22a-2f8941711121', '时尚百搭色修身男士短袖polo衫', 'b01003', 6,
   2000, 'solid knit glove with three-finger touchscreen conductivity', 1,
   '2016-10-30 07:37:31', '2016-10-30 08:32:03');
