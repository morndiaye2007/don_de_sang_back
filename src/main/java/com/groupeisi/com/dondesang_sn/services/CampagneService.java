package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CampagneService {
    CampagneDTO createCampagne(CampagneDTO campagneDTO);
    CampagneDTO updateCampagne(CampagneDTO campagneDTO);
    void deleteCampagne(Long id);
    CampagneDTO getCampagne(Long id);
    Page<CampagneDTO> getAllCampagnes(Map<String, String> searchParams, Pageable pageable);
}
