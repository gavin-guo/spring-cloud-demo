--liquibase formatted SQL

--changeset gavin:1
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id`            CHAR(36)    NOT NULL,
  `user_id`       CHAR(36)    NOT NULL,
  `status`        VARCHAR(20) NOT NULL,
  `total_amount`  DECIMAL(10, 0)       DEFAULT NULL,
  `reward_points` DECIMAL(10, 0)       DEFAULT NULL,
  `consignee`     VARCHAR(45)          DEFAULT NULL,
  `address`       VARCHAR(100)         DEFAULT NULL,
  `phone_number`  VARCHAR(20)          DEFAULT NULL,
  `version`       BIGINT(20)           DEFAULT NULL,
  `created_time`  TIMESTAMP   NOT NULL DEFAULT current_timestamp,
  `modified_time` TIMESTAMP   NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id`         CHAR(36) NOT NULL,
  `product_id` CHAR(36) NOT NULL,
  `order_id`   CHAR(36) NOT NULL,
  `quantity`   INT(10)  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_id_uindex` (`id`),
  KEY `item_order_id_fk` (`order_id`),
  CONSTRAINT `item_order_id_fk` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;