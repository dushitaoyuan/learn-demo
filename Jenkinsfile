pipeline {
  agent any
  stages {
    stage('prepare') {
      steps {
        git(url: 'https://github.com/dushitaoyuan/learn-demo.git', branch: 'master', credentialsId: 'demo_01', poll: true)
      }
    }

    stage('example') {
      steps {
        sh '''echo $workspace

echo `pwd`

echo $image


ls
'''
      }
    }

  }
  environment {
    app_name = 'name'
    app_version = 'v1'
    app_env = 'prod'
    image = '$app_name_$app_version'
    workspace = 'docker-compose-demo/docker-jenkins-demo'
  }
}