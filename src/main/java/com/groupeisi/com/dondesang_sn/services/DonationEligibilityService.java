package com.groupeisi.com.dondesang_sn.services;

import java.util.Date;

public interface DonationEligibilityService {
    boolean isEligibleForDonation(Long donneurId);
    Date getNextEligibleDate(Long donneurId);
    int getWaitingPeriodInMonths(String sexe);
}
