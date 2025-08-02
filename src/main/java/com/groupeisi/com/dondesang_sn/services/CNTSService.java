package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.CNTSDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CNTSService {
    CNTSDTO createCNTS(CNTSDTO cntsdto);
    CNTSDTO updateCNTS(CNTSDTO cntsdto);
    void deleteCNTS(Long id);
    CNTSDTO getCNTS(Long id);
    Page<CNTSDTO> getAllCNTSs(Map<String, String> searchParams, Pageable pageable);
}
