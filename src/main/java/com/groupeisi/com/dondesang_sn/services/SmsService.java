package com.groupeisi.com.dondesang_sn.services;

public interface SmsService {
    void sendConfirmationSms(String phoneNumber, String donorName, String appointmentDate, String appointmentTime, String centerName);
    void sendStatusUpdateSms(String phoneNumber, String donorName, String status, String appointmentDate);
    void sendSms(String phoneNumber, String message);
}
