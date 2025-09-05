package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.enums.StatusDon;
import com.groupeisi.com.dondesang_sn.repository.DonRepository;
import com.groupeisi.com.dondesang_sn.repository.DonneurRepository;
import com.groupeisi.com.dondesang_sn.services.DonationEligibilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationEligibilityServiceImpl implements DonationEligibilityService {

    private final DonRepository donRepository;
    private final DonneurRepository donneurRepository;

    @Override
    public boolean isEligibleForDonation(Long donneurId) {
        try {
            var donneur = donneurRepository.findById(donneurId)
                .orElseThrow(() -> new RuntimeException("Donneur non trouvé"));

            // Récupérer le dernier don effectué du donneur
            var dons = donRepository.findByDonneurId(donneurId);
            var lastDonation = dons.stream()
                .filter(don -> don.getStatutDon() == StatusDon.EFFECTUE || don.getStatutDon() == StatusDon.EFFECTUE)
                .max((d1, d2) -> d1.getDateDon().compareTo(d2.getDateDon()));

            if (lastDonation.isEmpty()) {
                // Aucun don précédent, éligible
                return true;
            }

            var lastDonDate = lastDonation.get().getDateDon();
            var waitingPeriod = getWaitingPeriodInMonths(donneur.getSexe().name());
            var nextEligibleDate = addMonthsToDate(lastDonDate, waitingPeriod);

            return new Date().after(nextEligibleDate);

        } catch (Exception e) {
            log.error("Erreur lors de la vérification d'éligibilité pour donneur {}", donneurId, e);
            return false;
        }
    }

    @Override
    public Date getNextEligibleDate(Long donneurId) {
        try {
            var donneur = donneurRepository.findById(donneurId)
                .orElseThrow(() -> new RuntimeException("Donneur non trouvé"));

            var dons = donRepository.findByDonneurId(donneurId);
            var lastDonation = dons.stream()
                .filter(don -> don.getStatutDon() == StatusDon.EFFECTUE || don.getStatutDon() == StatusDon.EFFECTUE)
                .max((d1, d2) -> d1.getDateDon().compareTo(d2.getDateDon()));

            if (lastDonation.isEmpty()) {
                // Aucun don précédent, éligible maintenant
                return new Date();
            }

            var lastDonDate = lastDonation.get().getDateDon();
            var waitingPeriod = getWaitingPeriodInMonths(donneur.getSexe().name());
            return addMonthsToDate(lastDonDate, waitingPeriod);

        } catch (Exception e) {
            log.error("Erreur lors du calcul de la prochaine date éligible pour donneur {}", donneurId, e);
            return new Date(); // Par défaut, éligible maintenant
        }
    }

    @Override
    public int getWaitingPeriodInMonths(String sexe) {
        switch (sexe.toUpperCase()) {
            case "MASCULIN":
                return 3; // 3 mois pour les hommes
            case "FEMININ":
                return 4; // 4 mois pour les femmes
            default:
                return 4; // Par défaut, période la plus longue
        }
    }

    private Date addMonthsToDate(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
}
