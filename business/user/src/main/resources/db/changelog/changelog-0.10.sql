--liquibase formatted SQL

--changeset gavin:1
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`            CHAR(36)     NOT NULL,
  `login_name`    VARCHAR(45)  NOT NULL,
  `password`      VARCHAR(100) NOT NULL,
  `nick_name`     VARCHAR(45)  NOT NULL,
  `email`         VARCHAR(45)  NOT NULL,
  `mobile_number` VARCHAR(45)       DEFAULT NULL,
  `status`        VARCHAR(20)  NOT NULL,
  `grade`         TINYINT(2)        DEFAULT 0,
  `version`       BIGINT(20)        DEFAULT NULL,
  `created_time`  TIMESTAMP    NULL DEFAULT current_timestamp,
  `modified_time` TIMESTAMP    NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_uindex` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority` (
  `id`        CHAR(36)    NOT NULL,
  `user_id`   CHAR(36)    NOT NULL,
  `authority` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_authority_id_uindex` (`id`),
  KEY `user_authority_user_id_fk` (`user_id`),
  CONSTRAINT `user_authority_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

--changeset gavin:2
INSERT INTO schema_user.user (id, login_name, PASSWORD, nick_name, email, mobile_number, STATUS, grade, version, created_time, modified_time)
VALUES ('806f0ac6-a3e4-42a1-8dfc-1a3e56002881', 'gavin', '$2a$10$lZtZ84C7opaUODCAdYzhwuNOuGqpSVjZLiM/gcZiAqEDDI/Vfq/Vu',
'gavin-guo', 'gavin.guo@msn.com', '13621670031', 'ENABLED', 1, 1,
'2016-11-03 07:25:03', '2016-11-03 07:25:03');

INSERT INTO schema_user.user_authority (id, user_id, authority)
VALUES ('bca21a1c-2afe-4305-875d-ee5ec5ac395f', '806f0ac6-a3e4-42a1-8dfc-1a3e56002881', 'AUTHORITY_SUPER');