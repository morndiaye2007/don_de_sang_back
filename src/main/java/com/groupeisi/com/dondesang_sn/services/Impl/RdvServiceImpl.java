package com.groupeisi.com.dondesang_sn.services.Impl;

//import com.groupeisi.com.dondesang_sn.entity.QRdvEntity;
import com.groupeisi.com.dondesang_sn.entity.RdvEntity;
import com.groupeisi.com.dondesang_sn.mapper.RdvMapper;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import com.groupeisi.com.dondesang_sn.repository.CampagneRepository;
import com.groupeisi.com.dondesang_sn.repository.CentreCollecteRepository;
import com.groupeisi.com.dondesang_sn.repository.DonneurRepository;
import com.groupeisi.com.dondesang_sn.repository.RdvRepository;
import com.groupeisi.com.dondesang_sn.services.RdvService;
import com.groupeisi.com.dondesang_sn.services.SmsService;
import com.groupeisi.com.dondesang_sn.services.DonationEligibilityService;
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
                    savedEntity.getHeureRdv() : "Heure non spécifiée";
                
                smsService.sendConfirmationSms(
                    donneur.getTelephone(),
                    donneur.getPrenom() + " " + donneur.getNom(),
                    dateFormatted,
                    heureFormatted,
                    centreNom
                );
                
                log.info("SMS de confirmation envoyé pour le RDV ID: {}", savedEntity.getId());
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
                
                smsService.sendStatusUpdateSms(
                    donneur.getTelephone(),
                    donneur.getPrenom() + " " + donneur.getNom(),
                    statusText,
                    dateFormatted
                );
                
                log.info("SMS de mise à jour statut envoyé pour le RDV ID: {} - Nouveau statut: {}", 
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
}
