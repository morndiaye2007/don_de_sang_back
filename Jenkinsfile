pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        SPRING_PROFILES_ACTIVE = 'test'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code source...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compilation du projet...'
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Exécution des tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Création du package JAR...'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
      stage('Quality Analysis') {
          steps {
              echo 'Analyse du code avec SonarCloud...'
              withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                  bat """
                  mvn -B verify sonar:sonar ^
                  -Dsonar.projectKey=don_de_sang_back ^
                  -Dsonar.organization=morndiaye2007 ^
                  -Dsonar.host.url=https://sonarcloud.io ^
                  -Dsonar.login=%SONAR_TOKEN%
                  """
              }
          }
      }


        
        stage('Docker Build') {
            steps {
                echo 'Construction de l\'image Docker...'
                script {
                    def imageTag = "dondesang-api:${BUILD_NUMBER}"
                    bat "docker build -t ${imageTag} ."
                    bat "docker tag ${imageTag} dondesang-api:latest"
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Déploiement en environnement de staging...'
                bat '''
                    docker stop dondesang-api-staging || true
                    docker rm dondesang-api-staging || true
                    docker run -d --name dondesang-api-staging \
                        -p 8081:2000 \
                        -e SPRING_PROFILES_ACTIVE=staging \
                        dondesang-api:latest
                '''
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                echo 'Déploiement en production...'
                input message: 'Déployer en production?', ok: 'Déployer'
                bat '''
                    docker stop dondesang-api-prod || true
                    docker rm dondesang-api-prod || true
                    docker run -d --name dondesang-api-prod \
                        -p 8080:2000 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        dondesang-api:latest
                '''
            }
        }
    }
    
    post {
        always {
            echo 'Nettoyage des ressources...'
            bat 'docker system prune -f'
        }
        success {
            echo 'Pipeline exécuté avec succès!'
            emailext (
                subject: " Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Le build ${env.BUILD_NUMBER} s'est terminé avec succès.",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
        failure {
            echo 'Échec du pipeline!'
            emailext (
                subject: " Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Le build ${env.BUILD_NUMBER} a échoué. Vérifiez les logs.",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
    }
}