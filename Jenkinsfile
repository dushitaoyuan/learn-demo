pipeline {
  agent any
  stages {
    stage('') {
      steps {
        sh '''cd $workspace

echo `pwd`'''
      }
    }

    stage('example') {
      steps {
        sh 'echo `pwd`'
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