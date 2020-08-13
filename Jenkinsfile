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
        sh '''echo ${project_path}

echo `pwd`

echo ${image}


ls
'''
      }
    }

  }
  environment {
    app_name = 'name'
    app_version = 'v1'
    app_env = 'prod'
    image = '${app_name}_${app_version}'
    project_path = 'docker-compose-demo/docker-jenkins-demo'
  }
}
