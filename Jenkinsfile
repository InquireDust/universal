pipeline {
    agent any

    stages {
        stage('构建') {
            steps {
                withDockerContainer('maven') {
                    // 使用 Maven 进行多线程构建
                    sh 'mvn clean package -T 1C' // 使用与 CPU 核心数相同的线程数
                }
            }
        }

        stage('制品') {
            steps {
                // 假设 JAR 文件在 target 目录下
                dir('target') {
                    sh 'ls -al'
                    // 这里可以将 JAR 文件打包成 tar.gz，如果需要的话
                    sh 'tar -zcvf app.tar.gz demo-0.0.1-SNAPSHOT.jar'
                    archiveArtifacts artifacts: 'app.tar.gz',
                                     allowEmptyArchive: true,
                                     fingerprint: true,
                                     onlyIfSuccessful: true
                    sh 'ls -al'
                }
            }
        }

        stage('部署') {
            steps {
                // 假设将 JAR 文件部署到 Docker 容器中
                dir('target') {
                    sh 'ls -al'
                    writeFile file: 'Dockerfile',
                              text: '''FROM openjdk:11-jre
COPY demo-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]'''
                    sh 'cat Dockerfile'
                    sh 'docker build -t my-app:latest .'
                    sh 'docker rm -f app || true' // 添加 || true 以避免错误
                    sh 'docker run -d -p 6789:8888 --name app my-app:latest'
                }
            }
        }
    }
}