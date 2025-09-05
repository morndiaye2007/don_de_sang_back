package com.groupeisi.com.dondesang_sn.services;

public interface PushNotificationService {
    void sendAppointmentConfirmation(String fcmToken, String donorName, String appointmentDate, String appointmentTime, String centerName);
    void sendAppointmentStatusUpdate(String fcmToken, String donorName, String status, String appointmentDate);
    void sendGeneralNotification(String fcmToken, String title, String body);
}
