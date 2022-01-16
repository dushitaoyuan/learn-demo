/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.7.23-log : Database - demo
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `demo`;

/*Data for the table `account` */

insert  into `account`(`id`,`username`,`phone`,`email`,`password`,`status`,`bind_user`,`type`) values 
(1207237777444442112,NULL,'13717898407',NULL,'$2a$10$wPEbYwJFY.zL/GM63r7XwOHM1ofWPfTyqap6rvJwMUqdQiH8II4Na',1,NULL,2);

/*Data for the table `log` */

insert  into `log`(`id`,`code`,`msg`,`msg_context`,`createtime`) values 
(1211458750452469760,101,'测试日志001e33fb25a39a84e8c8dbc482f08ff84dd','{\"userId\":\"111111\"}','2019-12-30 09:27:29'),
(1211459979165437952,101,'测试日志00157e7dd2db8004a2cab635f9f443ad144','{\"userId\":\"111111\"}','2019-12-30 09:32:22'),
(1211488180390465536,101,'测试日志001f7554bc4485c460f9500636dd285fd87','{\"userId\":\"111111\"}','2019-12-30 11:24:25'),
(1211488418127810560,101,'测试日志00112d4c2cdaffa42fda6b6470b49cd5d05','{\"userId\":\"111111\"}','2019-12-30 11:25:22'),
(1211488505381916672,101,'测试日志00101dd31ddf2244df2b12b913f447a2e8b','{\"userId\":\"111111\"}','2019-12-30 11:25:43'),
(1211535106263617536,101,'测试日志001f80f3a8d73d5479d83a601c3c95ba6e7','{\"userId\":\"111111\"}','2019-12-30 14:30:53'),
(1211559449051926528,101,'测试日志0012a358157156945f181f34bccc0184fac','{\"userId\":\"111111\"}','2019-12-30 16:07:37'),
(1211559604098568192,101,'测试日志0012cfdae7dc25c442d86e4fe6972e8130d','{\"userId\":\"111111\"}','2019-12-30 16:08:14'),
(1211559536389918720,101,'测试日志001287439877ae949009f7085861da33fd0','{\"userId\":\"111111\"}','2019-12-30 16:07:58'),
(1211560015186497536,101,'测试日志001e2985c0d4aca42fca8e230d0eee58a96','{\"userId\":\"111111\"}','2019-12-30 16:09:52'),
(1211562183608111104,101,'测试日志0012220aea52eb84267bca960f494a7f01a','{\"userId\":\"111111\"}','2019-12-30 16:18:29'),
(1211570390418198528,101,'测试日志001fd97a0078689428ea6853f82aab133d1','{\"userId\":\"111111\"}','2019-12-30 16:51:06'),
(1211570429232287744,101,'测试日志00132dbd25d2df64441a37e9b567f2dfaf8','{\"userId\":\"111111\"}','2019-12-30 16:51:15'),
(1211571069278883840,101,'测试日志001232713c9035e4293a442d3c4487509dd','{\"userId\":\"111111\"}','2019-12-30 16:53:47'),
(1211572254178152448,101,'测试日志001bfe9a812648c444597e5860a612cc987','{\"userId\":\"111111\"}','2019-12-30 16:58:30'),
(1211573035103031296,101,'测试日志00135f9aa09634e429e9e250277b4a032eb','{\"userId\":\"111111\"}','2019-12-30 17:01:36'),
(1211583341384568832,101,'测试日志001f83c4b62318f49a7aba0d2ce46b872b9','{\"userId\":\"111111\"}','2019-12-30 17:42:33'),
(1211583353267032064,101,'测试日志00148510d8a56f24dcd8709bd8fca342df8','{\"userId\":\"111111\"}','2019-12-30 17:42:36');

/*Data for the table `open_account` */

insert  into `open_account`(`id`,`open_id`,`open_secret`,`status`,`desc`,`white_ip`,`end_time`,`create_time`) values 
(1,'1','11111',1,'测试','*','2020-12-26 13:18:26','2019-12-26 13:18:16');

/*Data for the table `simple_user` */

/*Data for the table `user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
