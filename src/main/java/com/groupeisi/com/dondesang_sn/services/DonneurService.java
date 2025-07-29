package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface DonneurService {

  //  Page<DepartementDTO> getAllDepartement(Map<String, String> searchParams, Pageable pageable);
     DonneurDTO createDonneur(DonneurDTO donneurDTO);
    DonneurDTO updateDonneur(DonneurDTO donneurDTO);
    void deleteDonneur(Long id);
    DonneurDTO getDonneur(Long id);
     Page<DonneurDTO> getAllDonneurs(Map<String, String> searchParams, Pageable pageable);
}
