#!/bin/bash -il
workspace="docker-compose-demo/docker-jenkins-demo"
#docker镜像信息
docker_regist_repo="myregistry.com:5000"
app_version="v1"
app_env="prod"
app_name="docker-jenkins-demo"
image_tag="$app_env"
image_name="$docker_regist_repo/$app_name_$app_version:$image_tag"
#docker 集群ip
docker_node_ip="192.168.30.150 192.168.30.153"


cd $workspace

# 打包
mvn -U clean package -Dmaven.test.skip=true

#docker-build

docker build -t $image_name .
# docker harbor 登录
#docker login -u admin -p Harbor12345 ${docker_regist_repo}
docker push  $image_name
#删除本地镜像
docker rmi $image_name

# docker deploy prepare

su root
dos2unix files/shell/*sh

for node_ip in ${docker_node_ip}
do
sudo -s ssh root@$node_ip  "mkdir -p /home/deploy/shell"
sudo -s scp files/shell/docker-volume.sh root@$node_ip:/home/deploy/shell/
sudo -s ssh root@$node_ip  "cd /home/deploy/shell;sh docker-volume.sh create"
sudo -s scp -r files/mysql/* root@$node_ip:/home/mysql
sudo -s scp -r files/nginx/* root@$node_ip:/home/nginx
done


# docker deploy

docker stack rm $app_name
docker stack deploy $app_name -c=docker-stack.yml
docker stack ps $app_name






# 注意 设置 jdk,maven docker 可执行


## 将jenkins 加入docker组
#gpasswd -a jenkins docker
#newgrp docker

# 支持 sudo  vim /etc/sudoers
# jenkins ALL = NOPASSWD: ALL
