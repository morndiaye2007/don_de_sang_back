package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.LivraisonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface LivraisonService {
    LivraisonDTO createLivraison(LivraisonDTO livraisonDTO);
    LivraisonDTO createLivraisonFromDemande(Long demandeId);
    LivraisonDTO updateLivraison(LivraisonDTO livraisonDTO);
    void deleteLivraison(Long id);
    LivraisonDTO getLivraison(Long id);
    Page<LivraisonDTO> getAllLivraisons(Map<String, String> searchParams, Pageable pageable);
    List<LivraisonDTO> getLivraisonsByDemande(Long demandeId);
    List<LivraisonDTO> getLivraisonsByHopital(Long hopitalId);
}
