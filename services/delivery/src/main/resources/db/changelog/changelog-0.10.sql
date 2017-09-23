--liquibase formatted SQL

--changeset gavin:1
DROP TABLE IF EXISTS `carrier`;
CREATE TABLE `carrier` (
  `id`   CHAR(3)     NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `carrier_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `delivery`;
CREATE TABLE `delivery` (
  `id`              CHAR(36)     NOT NULL,
  `order_id`        CHAR(36)     NOT NULL,
  `consignee`       VARCHAR(45)  NOT NULL,
  `address`         VARCHAR(100) NOT NULL,
  `phone_number`    VARCHAR(20)  NOT NULL,
  `carrier_id`      CHAR(3)               DEFAULT NULL,
  `tracking_number` VARCHAR(15)           DEFAULT NULL,
  `status`          VARCHAR(20)  NOT NULL,
  `version`         BIGINT(20)            DEFAULT NULL,
  `created_time`    TIMESTAMP    NOT NULL DEFAULT current_timestamp,
  `modified_time`   TIMESTAMP    NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `delivery_id_uindex` (`id`),
  KEY `delivery_carrier_id_fk` (`carrier_id`),
  CONSTRAINT `delivery_carrier_id_fk` FOREIGN KEY (`carrier_id`) REFERENCES `carrier` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

--changeset gavin:2
INSERT INTO carrier (id, NAME ) VALUES ('001', 'ems');
INSERT INTO carrier (id, name) VALUES ('002', 'ups');