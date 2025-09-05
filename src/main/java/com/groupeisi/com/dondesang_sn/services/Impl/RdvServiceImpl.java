package com.groupeisi.com.dondesang_sn.services.Impl;

//import com.groupeisi.com.dondesang_sn.entity.RdvEntity;
import com.groupeisi.com.dondesang_sn.mapper.RdvMapper;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import com.groupeisi.com.dondesang_sn.repository.CampagneRepository;
import com.groupeisi.com.dondesang_sn.repository.CentreCollecteRepository;
import com.groupeisi.com.dondesang_sn.repository.DonneurRepository;
import com.groupeisi.com.dondesang_sn.repository.RdvRepository;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutRdv;
import com.groupeisi.com.dondesang_sn.services.RdvService;
import com.groupeisi.com.dondesang_sn.services.SmsService;
import com.groupeisi.com.dondesang_sn.services.PushNotificationService;
import com.groupeisi.com.dondesang_sn.services.DonationEligibilityService;
import com.groupeisi.com.dondesang_sn.services.DonService;
import com.groupeisi.com.dondesang_sn.services.StockSangService;
import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final RdvMapper rdvMapper;
    private final CentreCollecteRepository centreCollecteRepository;
    private final CampagneRepository campagneRepository;
    private final DonneurRepository donneurRepository;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;
    private final DonService donService;
    private final StockSangService stockSangService;
    private final DonationEligibilityService donationEligibilityService;

    @Override
    public RdvDTO createRdv(RdvDTO rdvDTO) {
        var entity = rdvMapper.asEntity(rdvDTO);
        
        // Récupérer les entités complètes si les IDs sont fournis
        if (rdvDTO.getCampagneId() != null) {
            var campagne = campagneRepository.findById(rdvDTO.getCampagneId())
                .orElseThrow(() -> new RuntimeException("Campagne non trouvée avec l'ID: " + rdvDTO.getCampagneId()));
            entity.setCampagne(campagne);
        }
        
        if (rdvDTO.getCentreCollecteId() != null) {
            var centreCollecte = centreCollecteRepository.findById(rdvDTO.getCentreCollecteId())
                .orElseThrow(() -> new RuntimeException("Centre de collecte non trouvé avec l'ID: " + rdvDTO.getCentreCollecteId()));
            entity.setCentreCollecte(centreCollecte);
        }
        
        // Associer le donneur si l'ID est fourni
        if (rdvDTO.getDonneurId() != null) {
            var donneur = donneurRepository.findById(rdvDTO.getDonneurId())
                .orElseThrow(() -> new RuntimeException("Donneur non trouvé avec l'ID: " + rdvDTO.getDonneurId()));
            
            // Vérifier l'éligibilité pour un nouveau don
            if (!donationEligibilityService.isEligibleForDonation(rdvDTO.getDonneurId())) {
                var nextEligibleDate = donationEligibilityService.getNextEligibleDate(rdvDTO.getDonneurId());
                var waitingPeriod = donationEligibilityService.getWaitingPeriodInMonths(donneur.getSexe().name());
                throw new RuntimeException(String.format(
                    "Le donneur ne peut pas faire un nouveau don avant le %s (%d mois d'attente pour les %s)", 
                    new SimpleDateFormat("dd/MM/yyyy").format(nextEligibleDate),
                    waitingPeriod,
                    donneur.getSexe().name().toLowerCase() + "s"
                ));
            }
            
            entity.setDonneur(donneur);
        }
        
        var savedEntity = rdvRepository.save(entity);
        var savedDto = rdvMapper.asDto(savedEntity);
        
        // Envoyer SMS de confirmation si un donneur est associé
        if (savedEntity.getDonneur() != null) {
            try {
                var donneur = savedEntity.getDonneur();
                var centreNom = savedEntity.getCentreCollecte() != null ? 
                    savedEntity.getCentreCollecte().getNom() : "Centre non spécifié";
                
                // Formater la date et l'heure
                String dateFormatted = savedEntity.getDateRdv() != null ? 
                    new SimpleDateFormat("dd/MM/yyyy").format(savedEntity.getDateRdv()) : "Date non spécifiée";
                String heureFormatted = savedEntity.getHeureRdv() != null ?
                        savedEntity.getHeureRdv().toString() : "Heure non spécifiée";
                
                // Envoyer SMS si configuré
                smsService.sendConfirmationSms(
                    donneur.getTelephone(),
                    donneur.getPrenom() + " " + donneur.getNom(),
                    dateFormatted,
                    heureFormatted,
                    centreNom
                );
                
                // Envoyer notification push si le donneur a un token FCM
                if (donneur.getFcmToken() != null && !donneur.getFcmToken().trim().isEmpty()) {
                    pushNotificationService.sendAppointmentConfirmation(
                        donneur.getFcmToken(),
                        donneur.getPrenom() + " " + donneur.getNom(),
                        dateFormatted,
                        heureFormatted,
                        centreNom
                    );
                    log.info("Notification push de confirmation envoyée pour le RDV ID: {}", savedEntity.getId());
                } else {
                    log.info("Aucun token FCM disponible pour le donneur ID: {}", donneur.getId());
                }
                
                log.info("Notifications envoyées pour le RDV ID: {}", savedEntity.getId());
            } catch (Exception e) {
                log.error("Erreur lors de l'envoi du SMS de confirmation pour RDV ID: {}", savedEntity.getId(), e);
                // Ne pas faire échouer la création du RDV si l'SMS échoue
            }
        }
        
        return savedDto;
    }

    @Override
    public RdvDTO updateRdv(RdvDTO rdvDTO)  {
        // Récupérer l'entité existante pour comparer les statuts
        var existingEntity = rdvRepository.findById(rdvDTO.getId())
            .orElseThrow(() -> new RuntimeException("RDV non trouvé avec l'ID: " + rdvDTO.getId()));
        
        var entityUpdate = rdvMapper.asEntity(rdvDTO);
        var updatedEntity = rdvRepository.save(entityUpdate);
        var updatedDto = rdvMapper.asDto(updatedEntity);
        
        // Envoyer SMS si le statut a changé et qu'un donneur est associé
        if (updatedEntity.getDonneur() != null && 
            !Objects.equals(existingEntity.getStatutRdv(), updatedEntity.getStatutRdv())) {
            
            try {
                var donneur = updatedEntity.getDonneur();
                String dateFormatted = updatedEntity.getDateRdv() != null ? 
                    new SimpleDateFormat("dd/MM/yyyy").format(updatedEntity.getDateRdv()) : "Date non spécifiée";
                
                String statusText = updatedEntity.getStatutRdv() != null ? 
                    updatedEntity.getStatutRdv().name() : "INCONNU";
                
                // Envoyer SMS si configuré
                smsService.sendStatusUpdateSms(
                    donneur.getTelephone(),
                    donneur.getPrenom() + " " + donneur.getNom(),
                    statusText,
                    dateFormatted
                );
                
                // Envoyer notification push si le donneur a un token FCM
                if (donneur.getFcmToken() != null && !donneur.getFcmToken().trim().isEmpty()) {
                    pushNotificationService.sendAppointmentStatusUpdate(
                        donneur.getFcmToken(),
                        donneur.getPrenom() + " " + donneur.getNom(),
                        statusText,
                        dateFormatted
                    );
                    log.info("Notification push de statut envoyée pour le RDV ID: {} - Nouveau statut: {}", 
                        updatedEntity.getId(), statusText);
                } else {
                    log.info("Aucun token FCM disponible pour le donneur ID: {}", donneur.getId());
                }
                
                log.info("Notifications de statut envoyées pour le RDV ID: {} - Nouveau statut: {}", 
                    updatedEntity.getId(), statusText);
            } catch (Exception e) {
                log.error("Erreur lors de l'envoi du SMS de mise à jour pour RDV ID: {}", 
                    updatedEntity.getId(), e);
                // Ne pas faire échouer la mise à jour du RDV si l'SMS échoue
            }
        }
        
        return updatedDto;
    }

    @Override
    public void deleteRdv(Long id) {
        if(!rdvRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        rdvRepository.deleteById(id);
    }

    @Override
    public RdvDTO getRdv(Long id) {
        var entity = rdvRepository.findById(id);
        return rdvMapper.asDto(entity.get());
    }


    @Override
    public Page<RdvDTO> getAllRdvs(Map<String, String> searchParams, Pageable pageable) {
        var page = rdvRepository.findAll(pageable);
        return page.map(entity -> {
            var dto = rdvMapper.asDto(entity);
            
            // Debug: Log pour voir si donneur_id existe
            System.out.println("RDV ID: " + entity.getId() + ", Donneur: " + entity.getDonneur());
            
            // Si donneur_id existe mais pas de relation, charger manuellement
            if (dto.getDonneurId() != null) {
                try {
                    var donneur = donneurRepository.findById(dto.getDonneurId());
                    if (donneur.isPresent()) {
                        var donneurEntity = donneur.get();
                        var donneurDto = DonneurDTO.builder()
                            .id(donneurEntity.getId())
                            .nom(donneurEntity.getNom())
                            .prenom(donneurEntity.getPrenom())
                            .sexe(donneurEntity.getSexe())
                            .telephone(donneurEntity.getTelephone())
                            .build();
                        dto.setDonneur(donneurDto);
                    }
                } catch (Exception e) {
                    System.out.println("Erreur chargement donneur: " + e.getMessage());
                }
            }
            return dto;
        });
    }

    @Override
    public List<RdvDTO> getRdvsByDonneur(Long donneurId) {
        var rdvs = rdvRepository.findByDonneurId(donneurId);
        return rdvs.stream().map(rdvMapper::asDto).collect(Collectors.toList());
    }

    @Override
    public RdvDTO validerRdv(Long rdvId) {
        var rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV non trouvé"));
        
        // Vérifier l'éligibilité du donneur avant validation
        if (rdv.getDonneur() != null) {
            boolean isEligible = donationEligibilityService.isEligibleForDonation(rdv.getDonneur().getId());
            if (!isEligible) {
                var nextEligibleDate = donationEligibilityService.getNextEligibleDate(rdv.getDonneur().getId());
                var waitingPeriod = donationEligibilityService.getWaitingPeriodInMonths(rdv.getDonneur().getSexe().name());
                throw new RuntimeException(String.format(
                    "Le donneur n'est pas éligible pour un don. Période d'attente: %d mois (%s). Prochaine date éligible: %s",
                    waitingPeriod,
                    rdv.getDonneur().getSexe().name().equals("MASCULIN") ? "Homme" : "Femme",
                    nextEligibleDate
                ));
            }
        }
        
        // Mettre à jour le statut
        rdv.setStatutRdv(StatutRdv.VALIDEE);
        var rdvSauvegarde = rdvRepository.save(rdv);
        
        try {
            // Créer automatiquement un don
            var don = donService.createDonFromRdv(rdvId);
            log.info("Don créé automatiquement pour RDV {}: {}", rdvId, don.getId());
            
            // Incrémenter le stock de sang
            if (rdv.getDonneur() != null && rdv.getCampagne() != null) {
                stockSangService.incrementerStock(
                    rdv.getDonneur().getGroupeSanguin(),
                    1.0, // 1 poche par don
                    rdv.getCampagne().getCentreCollecte().getId()
                );
                log.info("Stock incrémenté pour donneur {} au centre {}", 
                    rdv.getDonneur().getId(), rdv.getCampagne().getCentreCollecte().getId());
            }
            
        } catch (Exception e) {
            log.error("Erreur lors de la création du don ou incrémentation stock pour RDV {}", rdvId, e);
            // Ne pas faire échouer la validation du RDV si la création du don échoue
        }
        
        // Envoyer notification SMS
        try {
            if (rdv.getDonneur() != null && rdv.getDonneur().getTelephone() != null) {
                String message = String.format("Votre rendez-vous de don de sang du %s a été validé. Merci pour votre générosité !",
                    rdv.getDateRdv());
                smsService.sendSms(rdv.getDonneur().getTelephone(), message);
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du SMS pour RDV {}", rdvId, e);
        }
        
        return rdvMapper.asDto(rdvSauvegarde);
    }

    @Override
    public RdvDTO refuserRdv(Long id) {
        var rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RDV non trouvé"));
        
        rdv.setStatutRdv(StatutRdv.REFUSEE);
        var savedEntity = rdvRepository.save(rdv);
        return rdvMapper.asDto(savedEntity);
    }
}
