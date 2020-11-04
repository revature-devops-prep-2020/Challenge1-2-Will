pipeline {
    agent none
    stages {
        stage('build') {
            agent {
                docker {
                    image 'maven'
                    args '--net=host'
                }
            }
            steps {
                sh 'mvn clean package'
            }
        }
        stage('sonarqube') {
            agent {
                docker {
                    image 'maven'
                    args '--net=host'
                }
            }
             steps {
                 withSonarQubeEnv('sonarcloud') {
                    sh "mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar"
                    sleep(10)
                 }       
            }
        }
        stage('sonarqube gatekeeper') {
             steps {
                 timeout(time: 5, unit: 'MINUTES') {
                     waitForQualityGate abortPipeline: false
                 }
             }
         }
         stage('docker build') {
            steps {
                sh "docker build -t hippy96/samplewebapp:${currentBuild.number} ."
                sh "docker tag hippy96/samplewebapp:${currentBuild.number} hippy96/samplewebapp:latest"
            }
        }
        stage('docker push') {
            steps {
                withDockerRegistry([credentialsId: 'creds-dockerhub', url: '']) {
                    sh "docker push hippy96/samplewebapp:${currentBuild.number}"
                    sh 'docker push hippy96/samplewebapp:latest'
                }
            }
        }
        /*
        stage('app deploy') {
            steps {
                withKubeConfig([credentialsId: 'kube-creds', serverUrl: 'https://kubernetes.docker.internal:6443']) {
                    sh 'kubectl apply -f kubernetes.yml'
                    sh 'kubectl rollout restart deployment/sample-spring-boot'
                }
            }
        }*/
    }
}