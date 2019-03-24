node{
    stage('SCM Checkout'){
      git 'https://github.com/sangameshwar2804/SampleDocker'
    }
    stage('Compile-Package'){
      // get maven home path
      def mvnHome = tool name: 'maven-3', type: 'maven'
      sh "${mvnHome}/bin/mvn package"
     }
    stage('Build Dcker Image'){
          withCredentials([string(credentialsId: 'docker-pwd', variable: 'docker-pswd')]) {
            sh "docker login -u aryavip -p ${docker-pswd}"
          }
          sh 'docker build -t arvayip/myapp:1.0.0 .'
    }
    stage('Push Dcker Image'){
          withCredentials([string(credentialsId: 'docker-pwd', variable: 'docker-pswd')]) {
          sh "docker login -u aryavip -p ${docker-pswd}"
          }
          sh 'docker push aryavip/myapp:1.0.0'
    }
    stage('Run Container on Dev Server'){
          sh 'docker run -p 8089:8089 -d aryavip/myapp:1.0.0'
    }
}
