package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.QHopitalEntity;
import com.groupeisi.com.dondesang_sn.mapper.HopitalMapper;

import com.groupeisi.com.dondesang_sn.models.HopitalDTO;
import com.groupeisi.com.dondesang_sn.repository.HopitalRepository;
import com.groupeisi.com.dondesang_sn.services.HopitalService;
import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class HopitalServiceImpl implements HopitalService {

    private final HopitalRepository hopitalRepository;
    private final HopitalMapper hopitalMapper;

    @Override
    public HopitalDTO createHopital(HopitalDTO hopitalDTO) {
        var entity = hopitalMapper.asEntity(hopitalDTO);
        var entitySave = hopitalRepository.save(entity);
        return hopitalMapper.asDto(entitySave);
    }

    @Override
    public HopitalDTO updateHopital(HopitalDTO hopitalDTO)  {
        var entityUpdate = hopitalMapper.asEntity(hopitalDTO);
        var updatedEntity = hopitalRepository.save(entityUpdate);
        return hopitalMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteHopital(Long id) {
        if(!hopitalRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        hopitalRepository.deleteById(id);
    }

    @Override
    public HopitalDTO getHopital(Long id) {
        var entity = hopitalRepository.findById(id);
        return hopitalMapper.asDto(entity.get());
    }


    @Override
    public Page<HopitalDTO> getAllHopitals(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return hopitalRepository.findAll(booleanBuilder, pageable)
                .map(hopitalMapper::asDto);
    }
    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QHopitalEntity.hopitalEntity;
            if (searchParams.containsKey("nom"))
                booleanBuilder.and(qEntity.nom.containsIgnoreCase(searchParams.get("nom")));
            if (searchParams.containsKey("telephone"))
                booleanBuilder.and(qEntity.telephone.containsIgnoreCase(searchParams.get("telephone")));
            if (searchParams.containsKey("adresse"))
                booleanBuilder.and(qEntity.adresse.containsIgnoreCase(searchParams.get("adresse")));

            String region = searchParams.get("region");
            if (region != null && !region.isEmpty()) {
                booleanBuilder.and(qEntity.region.stringValue().lower().containsIgnoreCase(region.toLowerCase()));
            }

        }
    }


}
