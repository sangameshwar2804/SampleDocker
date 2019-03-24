node{
    stage('SCM Checkout'){
      git 'https://github.com/KamalNaidu/demo'
    }
    stage('Compile-Package'){
      // get maven home path
      def mvnHome = tool name: 'maven-3', type: 'maven'
      sh "${mvnHome}/bin/mvn package"
     }
    stage('Build Dcker Image'){
          withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerHubPwd')]) {
            sh "docker login -u msitdevops -p ${dockerHubPwd}"
          }
          sh 'docker build -t msitdevops/myapp:1.0.0 .'
    }
    stage('Push Dcker Image'){
          withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerHubPwd')]) {
          sh "docker login -u msitdevops -p ${dockerHubPwd}"
          }
          sh 'docker push msitdevops/myapp:1.0.0'
    }
    stage('Run Container on Dev Server'){
          sh 'docker run -p 8089:8089 -d msitdevops/myapp:1.0.0'
    }
}
