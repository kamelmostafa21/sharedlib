package org.iti;

def login(USERNAME, PASSWORD){
    sh "docker login -u ${USERNAME} -p ${PASSWORD}"
}

def buildPython(IMAGE_NAME, IMAGE_TAG){
    dir('python'){
        sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
    }
}
    

def push(IMAGE_NAME, IMAGE_TAG){
    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
}

def buildJava(IMAGE_NAME, IMAGE_TAG){
    dir('java'){
        sh "mvn clean package install -Dmaven.test.skip=true"
        sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
    }
}

def gitClone(String repoUrl, String branch = 'main', String targetDir = '.') {
    sh "rm -rf ${targetDir}"
    sh "git clone --branch ${branch} ${repoUrl} ${targetDir}"
}