package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.MedecinDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface MedecinService {
    MedecinDTO createMedecin(MedecinDTO medecinDTO);
    MedecinDTO updateMedecin(MedecinDTO medecinDTO);
    void deleteMedecin(Long id);
    MedecinDTO getMedecin(Long id);
    Page<MedecinDTO> getAllMedecins(Map<String, String> searchParams, Pageable pageable);
    Response<Object> authenticateMedecin(String email, String motDePasse);
}
