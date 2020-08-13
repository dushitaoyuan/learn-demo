pipeline {
  agent any
  stages {
    stage('pull') {
      steps {
        git(url: 'https://github.com/dushitaoyuan/learn-demo.git', branch: 'master', credentialsId: 'deno_01', poll: true)
      }
    }

  }
}