--liquibase formatted SQL

--changeset gavin:1
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id`            CHAR(36)       NOT NULL,
  `user_id`       CHAR(36)       NOT NULL,
  `order_id`      CHAR(36)                DEFAULT NULL,
  `amount`        DECIMAL(10, 0) NOT NULL,
  `status`        VARCHAR(20)    NOT NULL,
  `version`       BIGINT(20)              DEFAULT NULL,
  `created_time`  TIMESTAMP      NOT NULL DEFAULT current_timestamp,
  `modified_time` TIMESTAMP      NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `payment_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;