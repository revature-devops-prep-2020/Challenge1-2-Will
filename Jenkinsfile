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
        /*
        stage('sonarqube') {
            agent {
                docker { image 'busybox' }
            }
            steps {
                sh 'echo sonarqube'
            }
        }
        stage('docker build') {
            agent {
                docker { image 'busybox' }
            }
            steps {
                sh 'echo docker build'
            }
        }
        stage('docker push') {
            agent {
                docker { image 'busybox' }
            }
            steps {
                sh 'echo docker push'
            }
        }
        stage('app deploy') {
            agent {
                docker { image 'busybox' }
            }
            steps {
                sh 'echo kube deploy'
            }
        }
            */
    }
}