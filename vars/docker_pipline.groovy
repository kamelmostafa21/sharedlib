def call(){
    def dockerx = new org.iti.docker()
    node('agent-1') {
       

        tools {
            jdk "java-8"
        }

        environment {
            DOCKER_USER = credentials('docker_user')
            DOCKER_PASS = credentials('docker_password')
        }

        stages {
            stage('Login to DockerHub') {
                steps {
                    script {
                        dockerx.login(env.DOCKER_USER, env.DOCKER_PASS)
                    }
                }
            }

            stage('Clone Repositories') {
                steps {
                    script {
                        dockerx.gitClone('https://github.com/kamelmostafa21/java.git', 'master', 'java')
                        dockerx.gitClone('https://github.com/kamelmostafa21/python-sample-vscode-flask-tutorial.git', 'main', 'python')
                    }
                }
            }

            stage('Build and Push Docker Images') {
                parallel {
                    stage('Java Image') {
                        steps {
                            script {
                                dockerx.buildJava('kamelmostafa/java-app', 'latest')
                                dockerx.push('kamelmostafa/java-app', 'latest')
                            }
                        }
                    }

                    stage('Python Image') {
                        steps {
                            
                            script {
                                dockerx.buildPython('kamelmostafa/python-app', 'latest')
                                dockerx.push('kamelmostafa/python-app', 'latest')
                            }
                            
                        }
                    }
                }
            }
        }
    }
    }