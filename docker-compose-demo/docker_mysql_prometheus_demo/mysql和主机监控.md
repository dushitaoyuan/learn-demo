# mysql和主机监控
## 组件简介
- grafana
grafana 是一款采用 go 语言编写的开源应用，主要用于大规模指标数据的可视化展现，是网络架构和应用分析中最流行的时序数据展示工具，目前已经支持绝大部分常用的时序数据库
- Prometheus
一种时序性数据库
- xx exporter
监控数据采集点
node-exporter 主机监控数据采集程序
mysql-exporter mysql监控数据采集

## 安装
```shell
#安装 docker-compose
#启动 node-exporter
docker run -d \
--name node-exporter \
  --net="host" \
   --pid="host" \
   -v "/:/host:ro,rslave" \
   prom/node-exporter \
   --path.rootfs /host
# 创建网络
docker network connect mysql-monitor  node-exporter

docker-compose up -d
# 查看 
http://192.168.3.150:20002/
```
## 参考文档
grafana 图表商城:https://grafana.com/grafana/dashboards
https://www.cnblogs.com/myzony/p/10253986.html
https://www.jianshu.com/p/87e1ca5b84c9
https://www.jianshu.com/p/7e7e0d06709b
