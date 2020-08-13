pipeline {
    agent any
    // 参数设置
      environment {
       repoUrl = 'https://github.com/dushitaoyuan/learn-demo.git'
        repoBranch = 'master'
        workspace ='docker-compose-demo/docker-jenkins-demo'
       // docker镜像信息
        docker_regist_repo='myregistry.com:5000'
        app_version='v1'
        app_env='prod'
        app_name='docker-jenkins-demo'
        image_tag="prod"
        image_name='${docker_regist_repo}/demo/${app_name}_${app_version}:${image_tag}'
       //docker 集群ip
        docker_node_ip="192.168.3.150 192.168.3.153"
       // 认证ID
        gitCredentialsId = 'demo_01'
  }
           // 正常构建流程
           stage("Preparation"){
               // 拉取需要构建的代码
               git(url: '${repoUrl}', branch: '${repoBranch}', credentialsId: '${gitCredentialsId}', poll: true)
           }
           stage('project_build') {
               // maven构建生成二进制包
               dir("${workspace}"){
                 sh "mvn -U clean package -Dmaven.test.skip=true"
               }
           }
           stage('DockerBuild'){
             dir("${workspace}"){
               // docker生成镜像并push到远程仓库中
               sh '''
                     docker build -t ${image_name} .
                     # docker harbor 登录
                     #docker login -u admin -p Harbor12345 ${docker_regist_repo}
                     docker push  ${image_name}
                     #删除本地镜像
                     docker rmi ${image_name}
               '''
             }
           }
           stage('DockerStackDeployPrepare'){
              //docker stack 部署准备
             dir("${workspace}"){
               sh '''
                   dos2unix files/shell/*sh
                   for node_ip in ${docker_node_ip}
                   do
                    ssh root@$node_ip/  "mkdir -p /home/deploy/shell"
                    scp files/shell/docker-volume.sh root@$node_ip:/home/deploy/shell/
                    ssh root@$node_ip  "cd /home/deploy/shell;sh docker-volume.sh create"
                    scp -r files/mysql/* root@$node_ip:/home/mysql
                    scp -r files/nginx/* root@$node_ip:/home/nginx
                   done
               '''
             }
           }
           stage('DockerStackDeploy'){
               //docker stack 部署
             dir("${workspace}"){
               sh "docker stack deploy ${app_name} -c=docker-stack.yml"
             }
           }


}
