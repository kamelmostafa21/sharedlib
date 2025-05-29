// vars/docker_pipeline.groovy

def call() {
    pipeline {
        agent {
            label 'agent-1'
        }

        tools {
            jdk "java-8"
        }

        environment {
            DOCKER_USER = credentials('docker_user')
            DOCKER_PASS = credentials('docker_password')
        }

        stages {
            stage('Clean Workspace') {
                steps {
                    sh 'rm -rf java python'
                }
            }

            stage('Login to DockerHub') {
                steps {
                    sh """
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                    """
                }
            }

            stage('Clone Repositories') {
                steps {
                    sh '''
                        git clone -b master https://github.com/kamelmostafa21/java.git java
                        git clone -b main https://github.com/kamelmostafa21/python-sample-vscode-flask-tutorial.git python
                    '''
                }
            }

            stage('Build and Push Docker Images') {
                parallel {
                    stage('Java Image') {
                        steps {
                            dir('java') {
                                sh '''
                                    mvn clean package -DskipTests
                                    docker build -t kamelmostafa/java-app .
                                    docker push kamelmostafa/java-app:latest
                                '''
                            }
                        }
                    }

                    stage('Python Image') {
                        steps {
                            dir('python') {
                                sh '''
                                    docker build -t kamelmostafa/python-app .
                                    docker push kamelmostafa/python-app:latest
                                '''
                            }
                        }
                    }
                }
            }
        }
    }
}