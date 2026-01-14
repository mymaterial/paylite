pipeline {
    agent any

    environment {
        APP_DIR  = "/opt/paylite"
        JAR_NAME = "paylite.jar"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/mymaterial/paylite.git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                pkill -f paylite.jar || true
                cp target/paylite-app-*.jar ${APP_DIR}/${JAR_NAME}
                java -jar ${APP_DIR}/${JAR_NAME} \
                  > ${APP_DIR}/paylite.log 2>&1 &
                '''
            }
        }
    }
}

