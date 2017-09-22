--liquibase formatted SQL

--changeset gavin:1
DROP TABLE IF EXISTS `point`;
CREATE TABLE `point` (
  `id`                CHAR(36)       NOT NULL,
  `user_id`           CHAR(36)       NOT NULL,
  `amount`            DECIMAL(10, 0) NOT NULL,
  `lock_for_order_id` CHAR(36)                DEFAULT NULL,
  `expire_date`       CHAR(10)       NOT NULL,
  `version`           BIGINT(20)              DEFAULT NULL,
  `created_time`      TIMESTAMP      NOT NULL DEFAULT current_timestamp,
  `modified_time`     TIMESTAMP      NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `point_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `point_history`;
CREATE TABLE `point_history` (
  `id`           CHAR(36)       NOT NULL,
  `user_id`      CHAR(36)       NOT NULL,
  `order_id`     CHAR(36)                DEFAULT NULL,
  `amount`       DECIMAL(10, 0) NOT NULL,
  `action`       VARCHAR(20)    NOT NULL,
  `version`      BIGINT(20)              DEFAULT NULL,
  `created_time` TIMESTAMP      NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `point_history_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;