CREATE DATABASE /*!32312 IF NOT EXISTS*/`money_db_a` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `money_db_a`;

/*Table structure for table `user_account` */

DROP TABLE IF EXISTS `user_account`;

CREATE TABLE `user_account` (
  `id` bigint(20) DEFAULT NULL,
  `username` varchar(128) DEFAULT NULL COMMENT '账户名',
  `money` double DEFAULT NULL COMMENT '账户余额',
  `createtime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_account` */

insert  into `user_account`(`id`,`username`,`money`,`createtime`) values

(1,'dushitaoyuan-a',100,'2020-07-07 13:11:33');




CREATE DATABASE /*!32312 IF NOT EXISTS*/`money_db_b` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `money_db_b`;

/*Table structure for table `user_account` */

DROP TABLE IF EXISTS `user_account`;

CREATE TABLE `user_account` (
  `id` bigint(20) DEFAULT NULL,
  `username` varchar(128) DEFAULT NULL COMMENT '账户名',
  `money` double DEFAULT NULL COMMENT '账户余额',
  `createtime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_account` */

insert  into `user_account`(`id`,`username`,`money`,`createtime`) values

(2,'dushitaoyuan-b',100,'2020-07-07 13:11:53');