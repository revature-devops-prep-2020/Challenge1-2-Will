def repoName = "hippy96/samplewebapp"  //The repo the app will be pushed to
def kubeMaster = "https://3BE82A4FE03053098484F4C2AA933DB5.gr7.us-east-2.eks.amazonaws.com"  //cluster server URL
def kubectlImg = "reblank/kubectl_agent" //The docker img repo that has the kubectl CLI

pipeline {
    agent none
    stages {
        stage('build') {
            agent {
                docker {
                    image 'maven'
                    args '-v $HOME/.m2:/root/.m2 --net=host'  //cache downloaded dependencies to speed up subsequent builds
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
                sh "docker build -t ${repoName}:${currentBuild.number} ."
                sh "docker tag ${repoName}:${currentBuild.number} ${repoName}:latest"
            }
        }
        stage('Scan image') {
            agent {
                docker {
                    image 'trivy'
                    args '--net=host'
                }
            }
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh "trivy image --exit-code 1 ${repoName}:${currentBuild.number}"
                }
            }
            post {
                failure {
                    slackSend(color: 'warning', message: "This project has vulnerabilities in its image.")
                }
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
                    sh "docker push ${repoName}:${currentBuild.number}"
                    sh "docker push ${repoName}:latest"
                }
            }
        }
        stage('app deploy') {
                agent {
                    docker {
                        image "${kubectlImg}"
                        args '--net=host'
                    }
                }
            steps {
                withKubeConfig([credentialsId: 'creds-kubernetes', serverUrl: "${kubeMaster}"]) {
                    sh 'kubectl apply -f deployment.yaml -n test'
                    sh 'kubectl apply -f deployment.yaml -n prod'
                    sh 'kubectl rollout restart deployment/sample-deployment -n test'
                    sh 'kubectl rollout restart deployment/sample-deployment -n prod'  //The deployment name should be parameterized
                }
            }
            post {
                failure {
                    slackSend(color: 'danger', message: "Kubernetes deployment failed for app ${currentBuild.displayName}.")
                }
            }
        }
    }
    post {
        success {
            slackSend(color: 'good', message: "Application ${currentBuild.displayName} has been built and tested and is now deployed.")
        }
    }
}