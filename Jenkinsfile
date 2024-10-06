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
                sh 'docker rm $(docker ps -aq --filter "ancestor=maven")'
            }
        }

        stage('构建') {
            steps {
                withDockerContainer('maven') {
                    // docker run -v "$(pwd)":/usr/src/demo -v /var/mvn/:/root/.m2/repository -v /etc/maven/settings.xml:/root/.m2/settings.xml -w /usr/src/demo maven mvn clean package -T 1C -DskipTests
                     sh 'ls -al'
                     sh 'ls script -al'
                    sh 'mvn -s script/settings.xml -Dmaven.repo.local=/var/jenkins_home/repository clean package -T 2C -DskipTests'
                    sh 'ls ./target -al'
                    sh 'ls -al'   
                    sh 'ls target@tmp -al' 
                }
            }
        }

        //stage('运行') {
        //    steps {
        //        sh 'java -jar ./target/*.jar'
        //    }
        //}
        

        stage('部署') {
            steps {
                dir('./target') {
                    sh 'ls -al'
                    writeFile file: 'Dockerfile',
                            text: '''FROM joohit/jdk-17
        COPY *.jar /app/app.jar
        WORKDIR /app
        ENTRYPOINT ["java", "-jar", "app.jar"]'''
                    sh 'cat Dockerfile'
                    sh 'ls -al'
                    sh 'docker build -t jar-app:latest .'
                    sh 'docker rm -f jar-app || true' // 停止并删除已有容器
                    sh 'docker run -d -p 8899:8080 --name jar-app jar-app:latest' // 确保这里的端口一致
                }
            }
        }

    }
}
