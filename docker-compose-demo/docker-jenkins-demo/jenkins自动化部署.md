# jenkins自动化部署手册
## docker-compose stack部署需创建目录脚本
```shell script
# nginx 挂载目录
mkdir -p /home/nginx/
scp -r files/nginx/*  user@remote:/home/nginx/
# mysql 挂载目录 
ssh user@remote mkdir -p /home/mysql/data /home/mysql/conf/ /home/mysql/init 
scp -r files/mysql/conf/*  user@remote:/home/mysql/conf/
scp -r files/mysql/init/*  user@remote:/home/mysql/init/
# redis 挂载目录
ssh user@remote mkdir -p /home/redis/data
# 应用挂载目录
ssh user@remote mkdir -p /home/app/


```
## 相关文件
- Dockerfile
```dockerfile
FROM dushitaoyuan/alpine-oracle-java8:jdk
WORKDIR /springboot
COPY target/springboot-demo.jar springboot-demo.jar
#日志输出路径
VOLUME /home/logs
EXPOSE 8080
CMD ["nohup","java","-jar","/springboot/springboot-demo.jar","&"]
```
- docker镜像构建
```shell script

```
- jenkins pipeline 脚本
```groovy
node (){
    //拉取代码
   stage('Git Checkout') {
      checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'git@code.aliyun.com:mtr-sscp/sscp-antipass.git']]])
   }
   //代码编译
   stage('Maven Build') {
       sh 
        """
        """
   }
   stage('Build and Push Image') {
sh '''<br>TIME=$(date +%Y%m%d%H%M)<br>echo ${TIME} >TIME.txt
REPOSITORY=172.31.182.143/dev/sscp-antipass:${Tag}
cd target
cat >> Dockerfile <<EOF
# 以java:8为基础镜像
FROM java:8
MAINTAINER du.cn
# 告诉 Docker 服务端容器暴露的端口号
EXPOSE 8100
# 创建一个可以从本地主机或其他容器挂载的挂载点，一般用来存放数据库和需要保持的数据等。
VOLUME /tmp
# 格式为 ADD <src> <dest>。
# 该命令将复制指定的 <src> 到容器中的 <dest>。 其中 <src> 可以是Dockerfile所在目录的一个相对路径；也可以是一个 URL；还可以是一个 tar 文件（自动解压为目录）。
ADD sscp-antipass.jar /app.jar
# 从命令本身看，是为了执行一个touch命令，前面的add命令把jar复制过去，后面touch命令的作用是修改这个文件的访问时间和修改时间为当前时间。
RUN bash -c 'touch /app.jar'
# container启动时执行的命令，但是一个Dockerfile中只能有一条ENTRYPOINT命令，如果多条，则只执行最后一条
ENTRYPOINT ["java","-jar","/app.jar"]
EOF
docker build -t $REPOSITORY .
docker login -u admin -p Harbor%12345 172.31.182.143
docker push $REPOSITORY
'''
}
    stage('Deploy to Docker') {
        sh '''
        REPOSITORY=172.31.182.143/dev/sscp-antipass:${Tag}
        docker rm -f antipass |true
        docker image rm $REPOSITORY |true
        docker login -u admin -p Harbor%12345 172.31.182.143
        docker run -p 8100:8100 -v /home/sscp/sscp-antipass/dockerDeploy/logs:/logs -d --name antipass  $REPOSITORY
        '''
    }
}

```