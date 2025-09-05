package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.DemandeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DemandeService {
    DemandeDTO createDemande(DemandeDTO demandeDTO);
    DemandeDTO createDemandeByMedecin(DemandeDTO demandeDTO);
    DemandeDTO updateDemande(DemandeDTO demandeDTO);
    void deleteDemande(Long id);
    DemandeDTO getDemande(Long id);
    Page<DemandeDTO> getAllDemandes(Map<String, String> searchParams, Pageable pageable);
    List<DemandeDTO> getDemandesByMedecin(Long medecinId);
    DemandeDTO validerDemande(Long id);
    DemandeDTO refuserDemande(Long id);
    Page<DemandeDTO> getHistoriqueDemandes(String hopitalId, String medecinId, String statutDemande, String dateDebut, String dateFin, Pageable pageable);
    Map<String, Object> getStatistiquesDemandes();
}
