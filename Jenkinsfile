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
            agent {
                docker {
                    image 'docker'
                    args '--net=host'
                }
            }
            steps {
                sh "docker build -t hippy96/samplewebapp:${currentBuild.number} ."
                sh "docker tag hippy96/samplewebapp:${currentBuild.number} hippy96/samplewebapp:latest"
            }
        }
        stage('docker push') {
            agent {
                docker {
                    image 'docker'
                    args '--net=host'
                }
            }

            steps {
                withDockerRegistry([credentialsId: 'creds-dockerhub', url: '']) {
                    sh "docker push hippy96/samplewebapp:${currentBuild.number}"
                    sh 'docker push hippy96/samplewebapp:latest'
                }
            }
        }
        stage('app deploy') {
                agent {
                    docker {
                        image 'alpine/k8s'
                        args '--net=host'
                    }
                }
            steps {
                withKubeConfig([credentialsId: 'creds-kubernetes', serverUrl: 'https://E4002E13B45A1FBD72B244572F837574.gr7.us-west-1.eks.amazonaws.com']) {
                    sh 'kubectl apply -f deployment.yaml -n test'
                    sh 'kubectl apply -f deployment.yaml -n prod'
                    sh 'kubectl rollout restart deployment/sample-deployment -n test'
                    sh 'kubectl rollout restart deployment/sample-deployment -n prod'
                }
            }  //ADD IN SLACK AFTER THIS GETS TO WORKING
            post {
                failure {
                    slackSend(color: 'danger', message: "Kubernetes deployment failed .")
                }
            }
        }
    }
    post {
        success {
            slackSend(color: 'good', message: "Application has been built and tested and is now deployed.")
        }
    }
}