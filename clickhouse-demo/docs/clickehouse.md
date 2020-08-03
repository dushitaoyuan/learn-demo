# clclickhouse
## 简介
1. 真正的面向列的DBMS

2. 数据高效压缩

3. 磁盘存储的数据

4. 多核并行处理

5. 在多个服务器上分布式处理

6. SQL语法支持

7. 向量化引擎

8. 实时数据更新

9. 索引

10. 适合在线查询

11. 支持近似预估计算

12. 支持嵌套的数据结构,支持数组作为数据类型

13. 支持限制查询复杂性以及配额

14. 复制数据复制和对数据完整性的支持

## 安装
```shell script
# docker 镜像地址:https://hub.docker.com/r/yandex/clickhouse-server
docker pull yandex/clickhouse-server

# 启动命令
docker run -d -p  8123:8123 -p 9000:9000 --name clickhouse-server --ulimit nofile=262144:262144 --volume=/home/clickhouse/clickhouse_database:/var/lib/clickhouse yandex/clickhouse-server:20.3.15.133 

#进入docker镜像
docker exec -it clickhouse-server /bin/bash

clickhouse-client 
show databases

```

## 基本用法
```shell script

CREATE DATABASE IF NOT EXISTS test
use test
create table if not exists test.tb_test
(
    id Int64,
    datetime DateTime,
    content Nullable(String),
    value Nullable(Float64),
    date Date
)
engine = MergeTree                  --使用mergeTree引擎，ch主要引擎
partition by toYYYYMM(datetime)     --按照datetime这个字段的月进行分区
order by id                         --按照id进行排序
TTL datetime + INTERVAL 3 DAY ;     --三天过期





--修改表中数据过期时间，到期后数据会在merge时被删除
ALTER TABLE test.tb_test
MODIFY TTL datetime + INTERVAL 1 DAY;


--查询
select * from tb_test order by id;

--删除分区，可用于定时任务删除旧数据
alter table tb_test drop partition '202005';

--插入数据
insert into tb_test values (5, '2020-02-29 12:38:37', 'abcde', 12.553, '2020-04-25');

--修改数据，不推荐使用
alter table tb_test update content = 'hello click' where id=52;

--删除数据，不推荐使用
alter table tb_test delete WHERE id=56;



--自动求和聚合表
CREATE TABLE IF NOT EXISTS tb_stat
(
    regionId String,    --门店id
    groupId String,     --统计组id
    in int,             --进客流
    out int,            --出客流
    statDate DateTime   --统计时间
)
ENGINE = SummingMergeTree
partition by (toYYYYMM(statDate), regionId)
ORDER BY (toStartOfHour(statDate), regionId, groupId);

insert into tb_stat values ('1232364', '111',  32, 2,  '2020-03-25 12:56:00');
insert into tb_stat values ('1232364', '111',  34, 44, '2020-03-25 12:21:00');
insert into tb_stat values ('1232364', '111',  54, 12, '2020-03-25 12:20:00');
insert into tb_stat values ('1232364', '222',  45, 11, '2020-03-25 12:13:00');
insert into tb_stat values ('1232364', '222',  32, 33, '2020-03-25 12:44:00');
insert into tb_stat values ('1232364', '222',  12, 23, '2020-03-25 12:22:00');
insert into tb_stat values ('1232364', '333',  54, 54, '2020-03-25 12:11:00');
insert into tb_stat values ('1232364', '333',  22, 74, '2020-03-25 12:55:00');
insert into tb_stat values ('1232364', '333',  12, 15, '2020-03-25 12:34:00');


select toStartOfHour(statDate), regionId, groupId, sum(in), sum(out)
from tb_stat group by toStartOfHour(statDate), regionId, groupId;



执行:
clickhouse-client --query "create database test"
clickhouse-client --query "create table if not exists test.tb_test
(
    id Int64,
    datetime DateTime,
    content Nullable(String),
    value Nullable(Float64),
    date Date
)
engine = MergeTree                  
partition by toYYYYMM(datetime)   
order by id                       
TTL datetime + INTERVAL 3 DAY "


clickhouse-client --query "create table if not exists test.tb_test_ref
(
    id Int64,
    tb_test_id Int64, 
    datetime DateTime,
    status Int8 
)
engine = MergeTree                  
partition by toYYYYMM(datetime)   
order by id                       
TTL datetime + INTERVAL 3 DAY "

```

## 参考资料
-  官方文档  
https://clickhouse.tech/docs/en/engines/table-engines/  
