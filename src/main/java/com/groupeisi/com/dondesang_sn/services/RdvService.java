package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RdvService {

    RdvDTO createRdv(RdvDTO rdvDTO);

    RdvDTO updateRdv(RdvDTO rdvDTO);

    void deleteRdv(Long id);

    RdvDTO getRdv(Long id);

    Page<RdvDTO> getAllRdvs(Map<String, String> searchParams, Pageable pageable);

    List<RdvDTO> getRdvsByDonneur(Long donneurId);
    
    RdvDTO validerRdv(Long id);
    
    RdvDTO refuserRdv(Long id);

}