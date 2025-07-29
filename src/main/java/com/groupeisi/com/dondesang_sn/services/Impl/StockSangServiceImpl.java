package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.mapper.StockSangMapper;
import com.groupeisi.com.dondesang_sn.models.StockSangDTO;
import com.groupeisi.com.dondesang_sn.repository.StockSangRepository;
import com.groupeisi.com.dondesang_sn.services.StockSangService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockSangServiceImpl implements StockSangService {

    private final StockSangRepository stockSangRepository;
    private final StockSangMapper stockSangMapper;

    @Override
    public StockSangDTO createStockSang(StockSangDTO stockSangDTO) {
        var entity = stockSangMapper.asEntity(stockSangDTO);
        var entitySave = stockSangRepository.save(entity);
        return stockSangMapper.asDto(entitySave);
    }

    @Override
    public StockSangDTO updateStockSang(StockSangDTO stockSangDTO) {
        var entityUpdate = stockSangMapper.asEntity(stockSangDTO);
        var updatedEntity = stockSangRepository.save(entityUpdate);
        return stockSangMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteStockSang(Long id) {
        if (!stockSangRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        stockSangRepository.deleteById(id);
    }

    @Override
    public StockSangDTO getStockSang(Long id) {
        var entity = stockSangRepository.findById(id);
        return stockSangMapper.asDto(entity.get());
    }


    @Override
    public Page<StockSangDTO> getAllStockSangs(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return stockSangRepository.findAll(booleanBuilder, pageable)
                .map(stockSangMapper::asDto);
    }

    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QStockSangEntity.stockSangEntity;
            if (searchParams.containsKey("date_entree")) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("date_entree"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.date_entree.eq(date));
            }
            if (searchParams.containsKey("datePeremption")) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("datePeremption"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.datePeremption.eq(date));
            }
            String groupeSanguin = searchParams.get("groupeSanguin");
            if (groupeSanguin != null && !groupeSanguin.isEmpty()) {
                booleanBuilder.and(qEntity.groupeSanguin.stringValue().lower().containsIgnoreCase(groupeSanguin.toLowerCase()));
            }

        }
    }
}