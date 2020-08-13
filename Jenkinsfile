pipeline {
  agent any
  stages {
    stage('pullcode') {
      steps {
        git(url: 'https://github.com/dushitaoyuan/learn-demo.git', branch: 'master', poll: true)
      }
    }

    stage('project_build') {
      steps {
        sh '''cd docker-compose-demo/docker-jenkins-demo

mvn clean package '''
      }
    }

    stage('docker_build') {
      steps {
        sh '''sh dos2unix files/shell/*sh

node_ip_arr="192.168.3.150 192.168.3.153"

for node_ip in $node_ip_arr
do 
 ssh root@$node_ip  "mkdir -p /home/deploy/shell"
 scp files/shell/*.sh /home/deploy/shell/
 ssh root@$node_ip  "cd /home/deploy/shell;sh docker-volume.sh create"
done

sh files/shell/docker-shell.sh project_build
sh files/shell/docker-shell.sh docker_build
sh files/shell/docker-shell.sh docker_deploy


echo "docker deploy success"

'''
      }
    }

  }
}