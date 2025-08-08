package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.DemandeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface DemandeService {
    DemandeDTO createDemande(DemandeDTO demandeDTO);
    DemandeDTO updateDemande(DemandeDTO demandeDTO);
    void deleteDemande(Long id);
    DemandeDTO getDemande(Long id);
    Page<DemandeDTO> getAllDemandes(Map<String, String> searchParams, Pageable pageable);
}
