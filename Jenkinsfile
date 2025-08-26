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
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Exécution des tests...'
                sh 'mvn test'
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')], sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Création du package JAR...'
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Quality Analysis') {
            steps {
                echo 'Analyse de la qualité du code...'
                script {
                    try {
                        sh 'mvn sonar:sonar'
                    } catch (Exception e) {
                        echo 'SonarQube analysis failed, continuing...'
                    }
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                echo 'Construction de l\'image Docker...'
                script {
                    def imageTag = "dondesang-api:${BUILD_NUMBER}"
                    sh "docker build -t ${imageTag} ."
                    sh "docker tag ${imageTag} dondesang-api:latest"
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Déploiement en environnement de staging...'
                sh '''
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
                sh '''
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
            sh 'docker system prune -f'
        }
        success {
            echo 'Pipeline exécuté avec succès!'
            emailext (
                subject: "✅ Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Le build ${env.BUILD_NUMBER} s'est terminé avec succès.",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
        failure {
            echo 'Échec du pipeline!'
            emailext (
                subject: "❌ Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Le build ${env.BUILD_NUMBER} a échoué. Vérifiez les logs.",
                to: "${env.CHANGE_AUTHOR_EMAIL}"
            )
        }
    }
}