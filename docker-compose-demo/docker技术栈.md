# docker-swarm小集群实践
## 集群划分
- ip 划分
  192.168.3.150 docker-swarm1  
  192.168.3.152 docker-swarm2  
  192.168.3.153 docker-node1   
  192.168.3.154 docker-node2   
  192.168.3.155 docker-registry   

## docker相关操作
```shell
# 设置host 
hostnamectl set-hostname docker-swarm1

# 关闭防火墙
service firewalld stop
# 修改ip
vi /etc/sysconfig/network-scripts/ifcfg-ens33
# 开机启动
systemctl enable docker
vi /etc/docker/daemon.json

{
"registry-mirrors": [
"https://2lqq34jg.mirror.aliyuncs.com",
"https://pee6w651.mirror.aliyuncs.com",
"https://registry.docker-cn.com",
"http://hub-mirror.c.163.com"
],
"dns": ["8.8.8.8","8.8.4.4"],
"insecure-registries":["myregistry.com:5000"]
}
systemctl restart docker
# swarm init
docker swarm init --advertise-addr=192.168.3.150


Swarm initialized: current node (sfv7z5mxbnm8qhdfb83nkqgsd) is now a manager.

To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-148kt4dlearo64goo8aqkr593gqdws6b0crms5wb3o5u96ostm-f0ldzllqcybvwkwwijsvfgey6 192.168.3.150:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.
# 开放端口
#docker 集群通信
firewall-cmd --zone=public --add-port=2377/tcp --permanent
firewall-cmd --zone=public --add-port=5000/tcp --permanent
#docker 集群通信
firewall-cmd --zone=public --add-port=7946/tcp --permanent
firewall-cmd --reload

#加入swarm集群
docker swarm join --token SWMTKN-1-148kt4dlearo64goo8aqkr593gqdws6b0crms5wb3o5u96ostm-f0ldzllqcybvwkwwijsvfgey6 docker-swarm1:2377
# 退出集群
docker swarm leave
# 查看node
docker node ls

# docker registry 安装
官方地址:https://docs.docker.com/registry/configuration/
docker pull registry

vi /etc/docker/daemon.json
{
"insecure-registries":["docker-registry"]
}
sudo systemctl daemon-reload
sudo systemctl restart docker

# docker service  相关命令
#创建service
docker service create --replicas 3 -p 80:80 --name nginx nginx:1.13.7-alpine
# 删除 service
docker service rm nginx
#查看service
docker service ls
docekr service ps xxx
# 扩容/缩容 service
docker service scale nginx=2

# 删除 service
docker service rm nginx
# 查看 service 日志
docker service logs nginx




# 创建 docker overlay网络
docker network create -d overlay --attachable my_net
docker network ls

#滚动更新服务
docker service update --image new_image --update-delay 5s --update-parallelism 3 nginx
#设置并行更新的副本数目
--update-parallelism
#指定滚动更新的间隔时间
--update-delay            



# docker swarm 负载
docker network create -d overlay my_net

vip 模式
docker service create --replicas 3 -p 80:80 --network my_net  --name nginx nginx:1.13.7-alpine 


dnsrr 模式 不能暴露端口
docker service create --replicas 3  --network my_net --endpoint-mode dnsrr --name nginx nginx:1.13.7-alpine 

docker service update \
  --publish-add published=8080,target=80 \
  nginx2

修改 index.html
docker cp 62eee52b9580:/usr/share/nginx/html/index.html ./index.html
vi 
add <h1>docker-nodexxx</h1>

docker cp index.html 62eee52b9580:/usr/share/nginx/html/
docker exec -it 94b073fdb50e /bin/bash

查看 虚拟service ip 并对比 networkId 查找到对应的虚拟ip
docker service inspect -f '{{json .Endpoint.VirtualIPs}}' nginx
[{"NetworkID":"c3cwo0uh7o7ddmyzw8g306nme","Addr":"10.0.0.26/24"},{"NetworkID":"jgdbwvy8ptuns3v87fhvlnqkj","Addr":"10.0.1.2/24"}]
# swarm 相关文档
https://docs.docker.com/engine/swarm/services/



```

##  docker-machine 安装
- 安装
https://github.com/docker/machine/releases
参考博客:
https://blog.csdn.net/fly_leopard/article/details/93481469

```shell
# 创建 hyber-v虚拟机
my-switch
# 创建 docker-machine
docker-machine.exe create -d hyperv --hyperv-virtual-switch "my-switch" docker-swarm1

docker-machine create --driver virtualbox  --virtualbox-memory "512" docker-swarm1


# 查看docker-machine
docker-machine ls

```



## docker-compose 

docker-compose 单节点多容器编排

###  docker-compose 安装
- 官方手册

  https://docs.docker.com/compose/install/

  https://www.bbsmax.com/A/mo5kNrW2Jw/
  
  ```shell
  sudo curl -L "https://github.com/docker/compose/releases/download/1.25.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  # 添加可执行权限
  sudo chmod +x /usr/local/bin/docker-compose
  # 将文件copy到 /usr/bin/目录下
  sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
  # 查看版本
  docker-compose --version
  ```

### docker-compose语法

```yml
version: "3"  # 指定docker-compose语法版本
services:    # 从以下定义服务配置列表
  server_name:   # 可将server_name替换为自定义的名字，如mysql/php都可以
    container_name: container_name  # 指定实例化后的容器名，可将container_name替换为自定义名
    image: xxx:latest # 指定使用的镜像名及标签
    build:  # 如果没有现成的镜像，需要自己构建使用这个选项
      context: /xxx/xxx/Dockerfile  # 指定构建镜像文件的路径
      dockerfile: ....     # 指定Dockerfile文件名，上一条指定，这一条就不要了
    ports:
      - "00:00"  # 容器内的映射端口，本地端口:容器内端口
      - "00:00"  # 可指定多个
    volumes:
      - test1:/xx/xx  # 这里使用managed volume的方法，将容器内的目录映射到物理机，方便管理
      - test2:/xx/xx  # 前者是volumes目录下的名字，后者是容器内目录
      - test3:/xx/xx  # 在文件的最后还要使用volumes指定这几个tests
    volumes_from:  # 指定卷容器
       - volume_container_name  # 卷容器名
    restarts: always  # 设置无论遇到什么错，重启容器
    depends_on:       # 用来解决依赖关系，如这个服务的启动，必须在哪个服务启动之后
      - server_name   # 这个是名字其他服务在这个文件中的server_name
      - server_name1  # 按照先后顺序启动
    links:  # 与depend_on相对应，上面控制容器启动，这个控制容器连接
      - mysql  # 值可以是- 服务名，比较复杂，可以在该服务中使用links中mysql代替这个mysql的ip
    networks: # 加入指定的网络，与之前的添加网卡名类似
      - my_net  # bridge类型的网卡名
      - myapp_net # 如果没有网卡会被创建,建议使用时先创建号，在指定
    environment: # 定义变量，类似dockerfile中的ENV
      - TZ=Asia/Shanghai  # 设置时区
      变量值: 变量名   # 这些变量将会被直接写到镜像中的/etc/profile
    command: [                        #使用 command 可以覆盖容器启动后默认执行的命令
            '--character-set-server=utf8mb4',            #设置数据库表的数据集
            '--collation-server=utf8mb4_unicode_ci',    #设置数据库表的数据集
            '--default-time-zone=+8:00'                    #设置mysql数据库的 时区问题！！！！ 而不是设置容器的时区问题！！！！
    ]
  server_name2:  # 开始第二个容器
    server_name:
      stdin_open: true # 类似于docker run -d
      tty: true  # 类似于docker run -t
volumes:   # 以上每个服务中挂载映射的目录都在这里写入一次,也叫作声明volume
  test1:
  test2:
  test3:
networks:  # 如果要指定ip网段，还是创建好在使用即可，声明networks
  my_net:
    driver: bridge  # 指定网卡类型
  myapp_net:
    driver: bridge
```

### docker-compose 基本命令
**单机容器编排**

``` shell
#启动
docker-compose up -d
# 查看使用docker-compose.yml启动的容器
docker-compose ps

# 进入容器
docker-compose run  容器名 /bin/sh
# 启动docker-compose.yml中的容器
docker-compose start
#停止docker-compose.yml中的容器
docker-compose stop
# 删除使用docker-compose.yml运行的容器(需要先stop)
docker-compose rm
# 伸缩
 docker-compose scale web1=5
 


```
### docker stack 基本命令
**docker 原生多机容器编排**
不支持build指令

- 参考地址:  
https://blog.csdn.net/huangjun0210/article/details/86502021
```shell

 # deploy
 创建网络
 docker network create netname -d overlay --attachable flask_net 
 docker build -t myregistry.com:5000/flask_demo .
 docker push myregistry.com:5000/flask_demo .
 docker stack deploy -c docker-stack.yml myflask
 
 docker stack deploy myflask -c=docker-stack.yml

#部署新的堆栈或更新现有堆栈
docker stack deploy	
#列出现有堆栈
docker stack ls	
#列出堆栈中的任务
docker stack ps	
#删除一个或多个堆栈
docker stack rm	
# 列出堆栈中的服务
docker stack services	
```
## 实操问题
- 网络问题
  跨主机通信 overlay(自带) flannel(第三方)
  如网络不通,查看操作系统内核是否为4.x版本
  升级内核版本为4.x以上

  参考博客:https://www.cnblogs.com/xzkzzz/p/9627658.html

  https://blog.csdn.net/u013948858/article/details/83115388

   关闭防火墙  
   service firewalld stop

  禁止开机自启动: 
  systemctl stop firewalld.service

- docker stack 部署失败问题 invalid bind mount

  检查yml 挂载的磁盘路径在swarm node是否存在

  