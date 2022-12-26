node {
    def WORKSPACE = "/var/lib/jenkins/workspace/zona-delivery-server"
    def dockerImageTag = "zona-delivery-server-${env.BUILD_NUMBER}"

    try{
    
         stage('Clone Git Repository') {         
            git url: 'https://github.com/theprogmatheus/zona-delivery-server.git',
                credentialsId: 'Git',
                branch: 'master'
         }
         
          stage('Build Docker Image') {
                 dockerImage = docker.build("zona-delivery-server:${env.BUILD_NUMBER}")
                 echo "DockerImage: ${dockerImage}"
          }
         
          stage('Deploy Docker Image'){
                  echo "Docker Image Tag Name: ${dockerImageTag}"
                  sh "docker stop zona-delivery-server || true && docker rm zona-delivery-server || true"
                  sh "docker run --name zona-delivery-server -d -p 127.0.0.1:8085:8085 --env-file /var/env/zona-delivery-server.env zona-delivery-server:${env.BUILD_NUMBER}"
          }
          
    }catch(e){
        throw e
    }finally{
//         notifyBuild(currentBuild.result)
    }
}