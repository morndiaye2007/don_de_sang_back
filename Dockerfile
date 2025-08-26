# Dockerfile pour l'API DonDeSang Spring Boot
FROM openjdk:21-jdk-slim

# Métadonnées
LABEL maintainer="DonDeSang Team"
LABEL description="API Spring Boot pour le système de don de sang"

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=2000

# Créer un utilisateur non-root pour la sécurité
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Répertoire de travail
WORKDIR /app

# Copier le JAR de l'application
COPY target/*.jar app.jar

# Changer le propriétaire du fichier
RUN chown appuser:appuser app.jar

# Basculer vers l'utilisateur non-root
USER appuser

# Exposer le port
EXPOSE 2000

# Point de santé pour Docker
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:2000/api/actuator/health || exit 1

# Commande de démarrage
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
