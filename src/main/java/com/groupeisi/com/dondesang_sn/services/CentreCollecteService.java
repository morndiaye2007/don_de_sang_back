package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.CentreCollecteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CentreCollecteService {

    CentreCollecteDTO createCentreCollecte(CentreCollecteDTO centreCollecteDTO);
    CentreCollecteDTO updateCentreCollecte(CentreCollecteDTO centreCollecteDTO);
    void deleteCentreCollecte(Long id);
    CentreCollecteDTO getCentreCollecte(Long id);
    Page<CentreCollecteDTO> getAllCentreCollectes(Map<String, String> searchParams, Pageable pageable);
}
