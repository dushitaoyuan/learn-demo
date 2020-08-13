#!/bin/bash
regist_repo="myregistry.com:5000"
app_version="v1"
app_env="prod"
app_name="docker-jenkins-demo"
image_tag="$app_env"
image_name="$regist_repo/demo/$app_name_$app_version:$image_tag"
# 项目构建
function project_build() {
    cd ../../
    mvn clean package
}
# docker 镜像构建
function docker_build() {
    cd ../../
    docker build -t $image_name .
    # docker harbor 登录
    #docker login -u admin -p Harbor12345 $regist_repo
    docker push  $image_name
    #删除本地镜像
    docker rmi $image_name

}
# docker stack 部署
function docker_deploy() {
    #创建overlay网络 手动创建(建议)或 docker-compose 自动创建
    docker network create netname -d overlay --attachable java_net
    cd ../../
    docker stack deploy $app_name -c=docker-stack.yml
}


