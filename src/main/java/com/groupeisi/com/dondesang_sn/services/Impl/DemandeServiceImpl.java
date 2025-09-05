package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import com.groupeisi.com.dondesang_sn.entity.HopitalEntity;
import com.groupeisi.com.dondesang_sn.entity.MedecinEntity;
import com.groupeisi.com.dondesang_sn.entity.QDemandeEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutDemande;
import com.groupeisi.com.dondesang_sn.mapper.DemandeMapper;
import com.groupeisi.com.dondesang_sn.models.DemandeDTO;
import com.groupeisi.com.dondesang_sn.repository.DemandeRepository;
import com.groupeisi.com.dondesang_sn.repository.HopitalRepository;
import com.groupeisi.com.dondesang_sn.repository.MedecinRepository;
import com.groupeisi.com.dondesang_sn.services.DemandeService;
import com.groupeisi.com.dondesang_sn.services.LivraisonService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
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

@Service
@Transactional
@RequiredArgsConstructor
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository demandeRepository;
    private final DemandeMapper demandeMapper;
    private final MedecinRepository medecinRepository;
    private final HopitalRepository hopitalRepository;
    private final LivraisonService livraisonService;

    @Override
    public DemandeDTO createDemande(DemandeDTO demandeDTO) {
        var entity = demandeMapper.asEntity(demandeDTO);
        var entitySave = demandeRepository.save(entity);
        return demandeMapper.asDto(entitySave);    }

    @Override
    public DemandeDTO updateDemande(DemandeDTO demandeDTO) {
        var entityUpdate = demandeMapper.asEntity(demandeDTO);
        var updatedEntity = demandeRepository.save(entityUpdate);
        return demandeMapper.asDto(updatedEntity);    }

    @Override
    public void deleteDemande(Long id) {
        if(!demandeRepository.existsById(id)) {
            throw new RuntimeException("Contrat not found");
        }
        demandeRepository.deleteById(id);
    }

    @Override
    public DemandeDTO getDemande(Long id) {
        var entity = demandeRepository.findById(id);
        return demandeMapper.asDto(entity.get());    }

    @Override
    public Page<DemandeDTO> getAllDemandes(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return demandeRepository.findAll(booleanBuilder, pageable)
                .map(demandeMapper::asDto);
    }

    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QDemandeEntity.demandeEntity;
            if (searchParams.containsKey("groupeSanguin"))
                booleanBuilder.and(qEntity.groupeSanguin.stringValue().equalsIgnoreCase(searchParams.get("groupeSanguin")));

            if (searchParams.containsKey("statutDemande"))
                booleanBuilder.and(qEntity.statutDemande.stringValue().equalsIgnoreCase(searchParams.get("statutDemande")));


            if (searchParams == null || searchParams.isEmpty()) return;
            if (searchParams.containsKey("dateDemande")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dateDemande"));
                    booleanBuilder.and(qEntity.dateDemande.loe(date)); // less or equal

                } catch (ParseException e) {
                    throw new RuntimeException("Format de date_fin invalide (attendu: yyyy-MM-dd)",e);
                }
            }


        }
    }

    @Override
    public DemandeDTO createDemandeByMedecin(DemandeDTO demandeDTO) {
        var medecin = medecinRepository.findById(demandeDTO.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        
        var entity = demandeMapper.asEntity(demandeDTO);
        entity.setMedecin(medecin);
        entity.setHopital(medecin.getHopital());
        entity.setStatutDemande(StatutDemande.EN_ATTENTE);
        entity.setDateDemande(new Date());
        
        var entitySave = demandeRepository.save(entity);
        return demandeMapper.asDto(entitySave);
    }

    @Override
    public List<DemandeDTO> getDemandesByMedecin(Long medecinId) {
        var medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        
        return demandeRepository.findByMedecinId(medecinId)
                .stream()
                .map(demandeMapper::asDto)
                .toList();
    }

    @Override
    public DemandeDTO validerDemande(Long id) {
        var demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        demande.setStatutDemande(StatutDemande.VALIDEE);
        var savedEntity = demandeRepository.save(demande);
        
        // Créer automatiquement une livraison lors de la validation
        try {
            livraisonService.createLivraisonFromDemande(id);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer la validation
            System.err.println("Erreur lors de la création de la livraison: " + e.getMessage());
        }
        
        return demandeMapper.asDto(savedEntity);
    }

    @Override
    public DemandeDTO refuserDemande(Long id) {
        var demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        demande.setStatutDemande(StatutDemande.ANNULEE);
        var savedEntity = demandeRepository.save(demande);
        return demandeMapper.asDto(savedEntity);
    }

    @Override
    public Page<DemandeDTO> getHistoriqueDemandes(String hopitalId, String medecinId, String statutDemande, String dateDebut, String dateFin, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        var qEntity = QDemandeEntity.demandeEntity;
        
        // Filtres
        if (hopitalId != null && !hopitalId.isEmpty()) {
            booleanBuilder.and(qEntity.hopital.id.eq(Long.parseLong(hopitalId)));
        }
        
        if (medecinId != null && !medecinId.isEmpty()) {
            booleanBuilder.and(qEntity.medecin.id.eq(Long.parseLong(medecinId)));
        }
        
        if (statutDemande != null && !statutDemande.isEmpty()) {
            booleanBuilder.and(qEntity.statutDemande.stringValue().equalsIgnoreCase(statutDemande));
        }
        
        if (dateDebut != null && !dateDebut.isEmpty()) {
            try {
                Date debut = new SimpleDateFormat("yyyy-MM-dd").parse(dateDebut);
                booleanBuilder.and(qEntity.dateDemande.goe(debut));
            } catch (ParseException e) {
                throw new RuntimeException("Format de date_debut invalide (attendu: yyyy-MM-dd)", e);
            }
        }
        
        if (dateFin != null && !dateFin.isEmpty()) {
            try {
                Date fin = new SimpleDateFormat("yyyy-MM-dd").parse(dateFin);
                booleanBuilder.and(qEntity.dateDemande.loe(fin));
            } catch (ParseException e) {
                throw new RuntimeException("Format de date_fin invalide (attendu: yyyy-MM-dd)", e);
            }
        }
        
        return demandeRepository.findAll(booleanBuilder, pageable)
                .map(demandeMapper::asDto);
    }

    @Override
    public Map<String, Object> getStatistiquesDemandes() {
        var totalDemandes = demandeRepository.count();
        var demandesEnAttente = demandeRepository.countByStatutDemande(StatutDemande.EN_ATTENTE);
        var demandesValidees = demandeRepository.countByStatutDemande(StatutDemande.VALIDEE);
        var demandesAnnulees = demandeRepository.countByStatutDemande(StatutDemande.ANNULEE);
        
        var totalPoches = demandeRepository.sumNombreDepocheByStatut(StatutDemande.VALIDEE);
        
        return Map.of(
            "totalDemandes", totalDemandes,
            "demandesEnAttente", demandesEnAttente,
            "demandesValidees", demandesValidees,
            "demandesAnnulees", demandesAnnulees,
            "totalPochesValidees", totalPoches != null ? totalPoches : 0,
            "tauxValidation", totalDemandes > 0 ? (demandesValidees * 100.0 / totalDemandes) : 0
        );
    }

}
