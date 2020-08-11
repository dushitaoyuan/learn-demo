create database if not exists test;
use test;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `username`    varchar(50) NOT NULL COMMENT '用户名',
    `age`         int(11)   DEFAULT NULL COMMENT '年龄',
    `sex`         int(11)   DEFAULT NULL COMMENT '性别',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

INSERT INTO `user` VALUES ('1', 'dushitaoyuan', 1, 1, '2020-04-10 01:22:07.000');