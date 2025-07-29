package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.StockSangDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StockSangService {
    StockSangDTO createStockSang(StockSangDTO contratDTO);
    StockSangDTO updateStockSang(StockSangDTO contratDTO);
    void deleteStockSang(Long id);
    StockSangDTO getStockSang(Long id);
    Page<StockSangDTO> getAllStockSangs(Map<String, String> searchParams, Pageable pageable);
}
