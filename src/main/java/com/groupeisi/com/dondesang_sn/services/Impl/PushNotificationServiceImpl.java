package com.groupeisi.com.dondesang_sn.services.Impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.groupeisi.com.dondesang_sn.services.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {

    @Value("${push.notifications.enabled:true}")
    private boolean pushNotificationsEnabled;

    @Value("${push.notifications.test.mode:false}")
    private boolean testMode;

    @Override
    public void sendAppointmentConfirmation(String fcmToken, String donorName, String appointmentDate, String appointmentTime, String centerName) {
        if (!pushNotificationsEnabled) {
            log.info("Notifications push désactivées - notification de confirmation non envoyée à {}", donorName);
            return;
        }

        if (testMode) {
            log.info("MODE TEST - Notification push de confirmation simulée pour {} :\nTitre: Rendez-vous confirmé\nMessage: Votre RDV du {} à {} au centre {} est confirmé", 
                donorName, appointmentDate, appointmentTime, centerName);
            return;
        }

        try {
            String title = "🩸 Rendez-vous confirmé";
            String body = String.format("Bonjour %s, votre RDV du %s à %s au centre %s est confirmé", 
                donorName, appointmentDate, appointmentTime, centerName);

            Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putData("type", "appointment_confirmation")
                .putData("donor_name", donorName)
                .putData("appointment_date", appointmentDate)
                .putData("appointment_time", appointmentTime)
                .putData("center_name", centerName)
                .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Notification push de confirmation envoyée avec succès. Message ID: {}", response);

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de la notification push de confirmation", e);
        }
    }

    @Override
    public void sendAppointmentStatusUpdate(String fcmToken, String donorName, String status, String appointmentDate) {
        if (!pushNotificationsEnabled) {
            log.info("Notifications push désactivées - notification de statut non envoyée à {}", donorName);
            return;
        }

        if (testMode) {
            log.info("MODE TEST - Notification push de statut simulée pour {} :\nTitre: Statut RDV mis à jour\nMessage: Votre RDV du {} est maintenant {}", 
                donorName, appointmentDate, status);
            return;
        }

        try {
            String title = "📋 Statut de rendez-vous mis à jour";
            String statusMessage = getStatusMessage(status);
            String body = String.format("Bonjour %s, %s pour votre rendez-vous du %s", 
                donorName, statusMessage, appointmentDate);

            Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putData("type", "appointment_status_update")
                .putData("donor_name", donorName)
                .putData("status", status)
                .putData("appointment_date", appointmentDate)
                .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Notification push de statut envoyée avec succès. Message ID: {}", response);

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de la notification push de statut", e);
        }
    }

    @Override
    public void sendGeneralNotification(String fcmToken, String title, String body) {
        if (!pushNotificationsEnabled) {
            log.info("Notifications push désactivées - notification générale non envoyée");
            return;
        }

        if (testMode) {
            log.info("MODE TEST - Notification push générale simulée :\nTitre: {}\nMessage: {}", title, body);
            return;
        }

        try {
            Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putData("type", "general")
                .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Notification push générale envoyée avec succès. Message ID: {}", response);

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de la notification push générale", e);
        }
    }

    private String getStatusMessage(String status) {
        switch (status.toUpperCase()) {
            case "VALIDEE":
                return "✅ votre rendez-vous a été validé";
            case "REFUSEE":
                return "❌ votre rendez-vous a été refusé. Veuillez reprendre contact avec nous";
            case "EN_ATTENTE":
                return "⏳ votre rendez-vous est en attente de validation";
            case "ANNULEE":
                return "🚫 votre rendez-vous a été annulé";
            default:
                return "📋 le statut de votre rendez-vous a été mis à jour : " + status;
        }
    }
}
