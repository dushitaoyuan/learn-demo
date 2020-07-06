#  消息队列

## kafka

###  适用场景
海量消息堆积,日志等

### 简单介绍
- 并发消费  
并发消费,取决于topic 分区(Partition)数量,一个topic中消息只能consumer-group中某个消费者,不同consumer-group中的消费者可重复消费
- 顺序消费  
同一分区(Partition) 可以实现顺序,但不支持全局的顺序消费(场景较少),如果要实现某段消息的强制顺序消费,可在生产者消费投递时将一组消息投递到一个分区
- ack机制  
消息的ack机制,可实现消息的不丢失
- 重复消费  
业务方支持



## rocketmq
###  适用场景  
api丰富 支持延时消息,事务消息,顺序消费,消息过滤,文档详细
### (单机)安装 
```shell script
1.修改nameserver jvm大小
cd xxx/bin
vim runserver
set "JAVA_OPT=%JAVA_OPT% -server -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
2. 修改 broker jvm大小
vim runbroker
set "JAVA_OPT=%JAVA_OPT% -server -Xms1g -Xmx1g -Xmn512m"

3. 创建topic
-c 集群名称 查看配置
mqadmin updatetopic -n localhost:9876 -c DefaultCluster -t demotopic

4. list topic
 mqadmin  topicList -n localhost:9876
```
### 简单介绍
