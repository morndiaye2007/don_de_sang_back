package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.phone.number:}")
    private String fromPhoneNumber;

    @Value("${sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${sms.test.mode:false}")
    private boolean testMode;

    private boolean twilioInitialized = false;

    private void initializeTwilio() {
        if (!twilioInitialized && smsEnabled && accountSid != null && !accountSid.isEmpty() && authToken != null && !authToken.isEmpty()) {
            try {
                Twilio.init(accountSid, authToken);
                twilioInitialized = true;
                log.info("Twilio SMS service initialized successfully");
            } catch (Exception e) {
                log.error("Failed to initialize Twilio SMS service", e);
            }
        }
    }

    @Override
    public void sendConfirmationSms(String phoneNumber, String donorName, String appointmentDate, String appointmentTime, String centerName) {
        if (!smsEnabled) {
            log.info("SMS disabled - would send confirmation to {}: RDV confirmé pour {} le {} à {} au centre {}", 
                phoneNumber, donorName, appointmentDate, appointmentTime, centerName);
            return;
        }

        // Mode test : simuler l'envoi sans utiliser Twilio
        if (testMode) {
            String messageBody = String.format(
                "Bonjour %s,\n\nVotre rendez-vous de don de sang est confirmé :\n" +
                "📅 Date : %s\n" +
                "🕐 Heure : %s\n" +
                "🏥 Centre : %s\n\n" +
                "Merci pour votre générosité !\n" +
                "Don de Sang Sénégal",
                donorName, appointmentDate, appointmentTime, centerName
            );
            
            log.info("MODE TEST - SMS de confirmation simulé envoyé à {} :\n{}", phoneNumber, messageBody);
            return;
        }

        initializeTwilio();
        
        if (!twilioInitialized) {
            log.warn("Twilio not initialized - SMS not sent to {}", phoneNumber);
            return;
        }

        try {
            String messageBody = String.format(
                "Bonjour %s,\n\nVotre rendez-vous de don de sang est confirmé :\n" +
                "📅 Date : %s\n" +
                "🕐 Heure : %s\n" +
                "🏥 Centre : %s\n\n" +
                "Merci pour votre générosité !\n" +
                "Don de Sang Sénégal",
                donorName, appointmentDate, appointmentTime, centerName
            );

            // Formater le numéro de téléphone pour le Sénégal
            String formattedPhone = formatSenegalPhoneNumber(phoneNumber);

            Message message = Message.creator(
                new PhoneNumber(formattedPhone),
                new PhoneNumber(fromPhoneNumber),
                messageBody
            ).create();

            log.info("SMS confirmation sent successfully to {} with SID: {}", formattedPhone, message.getSid());
        } catch (Exception e) {
            log.error("Failed to send confirmation SMS to {}", phoneNumber, e);
        }
    }

    @Override
    public void sendStatusUpdateSms(String phoneNumber, String donorName, String status, String appointmentDate) {
        if (!smsEnabled) {
            log.info("SMS disabled - would send status update to {}: RDV {} pour {} le {}", 
                phoneNumber, status, donorName, appointmentDate);
            return;
        }

        // Mode test : simuler l'envoi sans utiliser Twilio
        if (testMode) {
            String statusMessage = getStatusMessage(status);
            String messageBody = String.format(
                "Bonjour %s,\n\n%s pour votre rendez-vous du %s.\n\n" +
                "Don de Sang Sénégal",
                donorName, statusMessage, appointmentDate
            );
            
            log.info("MODE TEST - SMS de mise à jour de statut simulé envoyé à {} :\n{}", phoneNumber, messageBody);
            return;
        }

        initializeTwilio();
        
        if (!twilioInitialized) {
            log.warn("Twilio not initialized - SMS not sent to {}", phoneNumber);
            return;
        }

        try {
            String statusMessage = getStatusMessage(status);
            String messageBody = String.format(
                "Bonjour %s,\n\n%s pour votre rendez-vous du %s.\n\n" +
                "Don de Sang Sénégal",
                donorName, statusMessage, appointmentDate
            );

            String formattedPhone = formatSenegalPhoneNumber(phoneNumber);

            Message message = Message.creator(
                new PhoneNumber(formattedPhone),
                new PhoneNumber(fromPhoneNumber),
                messageBody
            ).create();

            log.info("SMS status update sent successfully to {} with SID: {}", formattedPhone, message.getSid());
        } catch (Exception e) {
            log.error("Failed to send status update SMS to {}", phoneNumber, e);
        }
    }

    private String getStatusMessage(String status) {
        switch (status.toUpperCase()) {
            case "VALIDEE":
                return "✅ Votre rendez-vous a été validé";
            case "REFUSEE":
                return "❌ Votre rendez-vous a été refusé. Veuillez reprendre contact avec nous";
            default:
                return "📋 Statut de votre rendez-vous mis à jour : " + status;
        }
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        if (!smsEnabled) {
            log.info("SMS disabled - would send to {}: {}", phoneNumber, message);
            return;
        }

        // Mode test : simuler l'envoi sans utiliser Twilio
        if (testMode) {
            log.info("MODE TEST - SMS simulé envoyé à {} :\n{}", phoneNumber, message);
            return;
        }

        initializeTwilio();
        
        if (!twilioInitialized) {
            log.warn("Twilio not initialized - SMS not sent to {}", phoneNumber);
            return;
        }

        try {
            String formattedPhone = formatSenegalPhoneNumber(phoneNumber);

            Message twilioMessage = Message.creator(
                new PhoneNumber(formattedPhone),
                new PhoneNumber(fromPhoneNumber),
                message
            ).create();

            log.info("SMS sent successfully to {} with SID: {}", formattedPhone, twilioMessage.getSid());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}", phoneNumber, e);
        }
    }

    private String formatSenegalPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de téléphone requis");
        }

        // Nettoyer le numéro
        String cleaned = phoneNumber.replaceAll("[^0-9]", "");

        // Si le numéro commence par 221 (code pays Sénégal), l'utiliser tel quel
        if (cleaned.startsWith("221")) {
            return "+" + cleaned;
        }

        // Si le numéro commence par 7 (numéros mobiles sénégalais), ajouter le code pays
        if (cleaned.startsWith("7") && cleaned.length() == 9) {
            return "+221" + cleaned;
        }

        // Si le numéro commence par 0, le remplacer par le code pays
        if (cleaned.startsWith("0") && cleaned.length() == 10) {
            return "+221" + cleaned.substring(1);
        }

        // Par défaut, ajouter le code pays sénégalais
        return "+221" + cleaned;
    }
}
