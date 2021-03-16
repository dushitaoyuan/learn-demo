# kafka简单示例

## 安装

### 资源地址
- kafka  
http://kafka.apache.org/documentation/#quickstart
- zookeeper  
https://zookeeper.apache.org/releases.html
- kafka消费监控  
https://cloud.tencent.com/developer/article/1667262#:~:text=Kafka%E4%B8%89%E7%A7%8D%E5%8F%AF%E8%A7%86%E5%8C%96%E7%9B%91%E6%8E%A7%E7%AE%A1%E7%90%86%E5%B7%A5%E5%85%B7Monitor%2FManager%2FEagle%201%20Kafka%20Monitor%20%E4%B8%8B%E8%BD%BD%E9%93%BE%E6%8E%A5%EF%BC%9Ahttps%3A%2F%2F%20...%202,Kafka%20Manager%202.%20...%203%20Kafka%20Eagle
```shell script
# kafka 查询消费数量
./kafka-consumer-groups.sh --describe --bootstrap-server localhost:9092 --group 组名称 |grep topic名称|awk '{sum1 += $3} {sum2 += $4} {sum3 += $5}  END {print  "已消费数量",sum1,"总数",sum2,"未消费数量",sum3}'

# shell向kafka发送消息

echo "消息内容" | bin/kafka-console-producer.sh  --broker-list localhost:9092 --sync --topic topic名称


cat 文件 | bin/kafka-console-producer.sh  --broker-list localhost:9092 --sync --topic topic名称


# 创建topic

bin/kafka-topics.sh --create --zookeeper localhost:2181/kafka --partitions 1 --replication-factor 1 --topic topic名称

# 关闭,启动

bin/kafka-server-stop.sh

./kafka-server-start.sh -daemon ../config/server.properties


#查看topic
 ./bin/kafka-topics.sh --list --zookeeper localhost:2181
```


## kafka stream
### 例子topic复制
- 代码参见:
com.taoyuanx.demo.stream.StreamCopy 
```shell script
# 创建topic
kafka-topics.sh --create --zookeeper localhost:2181 --topic myStreamIn --partitions 1 --replication-factor 1
kafka-topics.sh --create --zookeeper localhost:2181 --topic myStreamIn --partitions 1 --replication-factor 1

#生产myStreamIn消息
kafka-console-producer.sh --topic myStreamIn --broker-list localhost:9092

#消费myStreamOut消息
kafka-console-consumer.sh --topic myStreamOut --bootstrap-server localhost:9092 --from-beginning

```
### 例子wordcount
- 代码参见:
com.taoyuanx.demo.stream.WordCountStream 

```shell script

# 创建wordcount-input
kafka-topics.sh --create --zookeeper localhost:2181 --topic wordcount-input --partitions 1 --replication-factor 1
# 创建wordcount-output
kafka-topics.sh --create --zookeeper localhost:2181 --topic wordcount-output --partitions 1 --replication-factor 1
# 创建生产者
kafka-console-producer.sh --topic wordcount-input --broker-list localhost:9092
# 创建消费者，需要打印出key(--property print.key=true)
kafka-console-consumer.sh --topic wordcount-output --bootstrap-server localhost:9092 --from-beginning --property print.key=true

```


### 例子求和
- 代码参见:
com.taoyuanx.demo.stream.SumStream   

```shell script

# 创建suminput
kafka-topics.sh --create --zookeeper localhost:2181 --topic suminput  --partitions 1 --replication-factor 1
# 创建生产者
kafka-console-producer.sh --topic suminput --broker-list localhost:9092
# 创建sumout
kafka-topics.sh --create --zookeeper localhost:2181 --topic sumout  --partitions 1 --replication-factor 1
# 创建消费者
kafka-console-consumer.sh --topic sumout --bootstrap-server localhost:9092 --from-beginning

```


### 例子window wordcount

每隔2秒钟输出一次过去5秒内windowWordCountInput里的wordcount  结果写入windowWordCountOutput 时间有交叉 
- 代码参见:
com.taoyuanx.demo.stream.WindowStream     
每隔5秒钟输出一次过去5秒内windowWordCountInput里的wordcount 结果写入windowWordCountOutput 时间无交叉  
com.taoyuanx.demo.stream.WindowStream2 

windowWordCountInput 15秒内的wordcount，结果写入windowWordCountOutput 
com.taoyuanx.demo.stream.WindowStream3

```shell script
# 创建windowWordCountInput
kafka-topics.sh --create --zookeeper localhost:2181 --topic windowWordCountInput --partitions 1 --replication-factor 1
# 创建windowWordCountOutput
kafka-topics.sh --create --zookeeper localhost:2181 --topic windowWordCountOutput --partitions 1 --replication-factor 1
# 创建生产者
kafka-console-producer.sh --topic windowWordCountInput --broker-list localhost:9092
# 创建消费者，需要打印出key(--property print.key=true)
kafka-console-consumer.sh --topic windowWordCountOutput --bootstrap-server localhost:9092 --from-beginning --property print.key=true


```
### 例子 页面pv统计

- 数据构造与数据格式解析
com.taoyuanx.demo.stream.data.producer.PageViewDataProducer    
- stream 数据处理  
com.taoyuanx.demo.stream.MyPageViewCountStream  
- 时间戳抽取  
com.taoyuanx.demo.stream.MyJsonTimestampExtractor  



```shell script
# 创建pageView
kafka-topics.sh --create --zookeeper localhost:2181 --topic pageView --partitions 1 --replication-factor 1
# 创建appCount-output/pageCount-output
kafka-topics.sh --create --zookeeper localhost:2181 --topic appCount-output --partitions 1 --replication-factor 1
kafka-topics.sh --create --zookeeper localhost:2181 --topic pageCount-output --partitions 1 --replication-factor 1


# 查看输出
kafka-console-consumer.sh --topic appCount-output --bootstrap-server localhost:9092 --from-beginning --property print.key=true

kafka-console-consumer.bat --topic pageCount-output --bootstrap-server localhost:9092 --from-beginning --property print.key=true


```


##例子 复杂pv统计(官方例子)


- 数据构造  
com.taoyuanx.demo.stream.data.producer.PageViewMoreDataProducer

- stream 数据处理  
com.taoyuanx.demo.stream.data.PageViewTypedDemo 

```shell script
# 创建pageView
kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-pageview-input --partitions 1 --replication-factor 1
kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-userprofile-input --partitions 1 --replication-factor 1
kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-pageviewstats-typed-output --partitions 1 --replication-factor 1

# 查看输出
kafka-console-consumer.sh --topic streams-pageviewstats-typed-output --bootstrap-server localhost:9092 --from-beginning --property print.key=true


```