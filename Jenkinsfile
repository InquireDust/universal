pipeline {
    agent any

    stages {
        stage('检查') {
            steps {
                echo '开始检查'
                sh 'docker run maven mvn -v' // 使用与 CPU 核心数相同的线程数
                echo '开始构建'
                sh 'mkdir -p /var/jenkins_home/repository'
                sh 'ls -l'
            }
        }

        stage('构建') {
            steps {
                withDockerContainer('maven') {
                    // docker run -v "$(pwd)":/usr/src/demo -v /var/mvn/:/root/.m2/repository -v /etc/maven/settings.xml:/root/.m2/settings.xml -w /usr/src/demo maven mvn clean package -T 1C -DskipTests

                    sh 'mvn -s script/settings.xml -Dmaven.repo.local=/var/jenkins_home/repository clean package -T 1C -DskipTests'
                    sh 'ls ./target -al'
                    sh 'ls -al'   
                    sh 'ls target@tmp -al' 
                }
            }
        }

        stage('部署') {
            steps {
                // 假设将 JAR 文件部署到 Docker 容器中
                dir('./target') {
                    sh 'ls -al'
                    writeFile file: 'Dockerfile',
                              text: '''FROM openjdk:11-jre
COPY *.* /*
ENTRYPOINT ["java", "-jar", "/*.jar"]'''
                    sh 'cat Dockerfile'
                    sh 'ls -al'
                    sh 'docker build -t jar-app:latest .'
                    sh 'docker rm -f jar-app || true' // 停止并删除已有容器
                    sh 'docker run -d -p 8899:6789 --name jar-app jar-app:latest'
                }
            }
        }
    }
}
