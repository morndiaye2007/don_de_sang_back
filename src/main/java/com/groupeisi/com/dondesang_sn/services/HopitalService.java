package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.HopitalDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface HopitalService {
    HopitalDTO createHopital(HopitalDTO hopitalDTO);
    HopitalDTO updateHopital(HopitalDTO hopitalDTO);
    void deleteHopital(Long id);
    HopitalDTO getHopital(Long id);
    Page<HopitalDTO> getAllHopitals(Map<String, String> searchParams, Pageable pageable);
}
