package com.groupeisi.com.dondesang_sn.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${push.notifications.enabled:true}")
    private boolean pushNotificationsEnabled;

    @Value("${push.notifications.test.mode:false}")
    private boolean testMode;

    @PostConstruct
    public void initialize() {
        if (!pushNotificationsEnabled) {
            log.info("Notifications push désactivées - Firebase non initialisé");
            return;
        }

        if (testMode) {
            log.info("Mode test activé - Firebase non initialisé (notifications simulées)");
            return;
        }

        try {
            // Chercher le fichier de configuration Firebase
            InputStream serviceAccount = null;
            
            try {
                // Essayer de charger depuis le classpath
                serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();
                log.info("Fichier Firebase trouvé dans le classpath");
            } catch (Exception e) {
                log.warn("Fichier firebase-service-account.json non trouvé dans le classpath");
                
                // Essayer de charger depuis un chemin externe
                try {
                    serviceAccount = new FileInputStream("firebase-service-account.json");
                    log.info("Fichier Firebase trouvé dans le répertoire courant");
                } catch (Exception ex) {
                    log.warn("Fichier firebase-service-account.json non trouvé dans le répertoire courant");
                }
            }

            if (serviceAccount == null) {
                log.warn("Aucun fichier de configuration Firebase trouvé. Les notifications push ne fonctionneront pas.");
                log.info("Pour activer les notifications push :");
                log.info("1. Téléchargez le fichier de clé privée depuis la console Firebase");
                log.info("2. Renommez-le 'firebase-service-account.json'");
                log.info("3. Placez-le dans src/main/resources/ ou à la racine du projet");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase initialisé avec succès pour les notifications push");
            } else {
                log.info("Firebase déjà initialisé");
            }

        } catch (IOException e) {
            log.error("Erreur lors de l'initialisation de Firebase", e);
            log.warn("Les notifications push ne fonctionneront pas sans configuration Firebase valide");
        }
    }
}
