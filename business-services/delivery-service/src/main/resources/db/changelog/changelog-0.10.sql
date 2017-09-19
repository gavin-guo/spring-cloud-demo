--liquibase formatted sql

--changeset gavin:1
drop table if exists `carrier`;
create table `carrier` (
  `id`   char(3)     not null,
  `name` varchar(45) not null,
  primary key (`id`),
  unique key `carrier_id_uindex` (`id`)
)
  engine = innodb
  default charset = utf8;

drop table if exists `delivery`;
create table `delivery` (
  `id`              char(36)     not null,
  `order_id`        char(36)     not null,
  `consignee`       varchar(45)  not null,
  `address`         varchar(100) not null,
  `phone_number`    varchar(20)  not null,
  `carrier_id`      char(3)               default null,
  `tracking_number` varchar(15)           default null,
  `status`          varchar(20)  not null,
  `version`         bigint(20)            default null,
  `created_time`    timestamp    not null default current_timestamp,
  `modified_time`   timestamp    not null default current_timestamp on update current_timestamp,
  primary key (`id`),
  unique key `delivery_id_uindex` (`id`),
  key `delivery_carrier_id_fk` (`carrier_id`),
  constraint `delivery_carrier_id_fk` foreign key (`carrier_id`) references `carrier` (`id`)
)
  engine = innodb
  default charset = utf8;

--changeset gavin:2
insert into carrier (id, name) values ('001', 'ems');
insert into carrier (id, name) values ('002', 'ups');