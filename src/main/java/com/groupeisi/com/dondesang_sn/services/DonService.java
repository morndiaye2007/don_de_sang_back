package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.DonDTO;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DonService {
    DonDTO createDon(DonDTO donDTO);
    DonDTO createDonFromRdv(Long rdvId);
    DonDTO updateDon(DonDTO donDTO);
    void deleteDon(Long id);
    DonDTO getDon(Long id);
    Page<DonDTO> getAllDons(Map<String, String> searchParams, Pageable pageable);
    List<DonDTO> getDonsByDonneur(Long donneurId);
}
