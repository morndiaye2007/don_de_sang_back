package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.QRdvEntity;
import com.groupeisi.com.dondesang_sn.mapper.RdvMapper;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import com.groupeisi.com.dondesang_sn.repository.RdvRepository;
import com.groupeisi.com.dondesang_sn.services.RdvService;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final RdvMapper rdvMapper;

    @Override
    public RdvDTO createRdv(RdvDTO rdvDTO) {
        var entity = rdvMapper.asEntity(rdvDTO);
        var entitySave = rdvRepository.save(entity);
        return rdvMapper.asDto(entitySave);
    }

    @Override
    public RdvDTO updateRdv(RdvDTO rdvDTO)  {
        var entityUpdate = rdvMapper.asEntity(rdvDTO);
        var updatedEntity = rdvRepository.save(entityUpdate);
        return rdvMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteRdv(Long id) {
        if(!rdvRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        rdvRepository.deleteById(id);
    }

    @Override
    public RdvDTO getRdv(Long id) {
        var entity = rdvRepository.findById(id);
        return rdvMapper.asDto(entity.get());
    }


    @Override
    public Page<RdvDTO> getAllRdvs(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return rdvRepository.findAll(booleanBuilder, pageable)
                .map(rdvMapper::asDto);
    }
    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QRdvEntity.rdvEntity;
            if (searchParams.containsKey("dateRdv")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dateRdv"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.dateRdv.eq(date));
            }
            String statutRdv = searchParams.get("statutRdv");
            if (statutRdv != null && !statutRdv.isEmpty()) {
                booleanBuilder.and(qEntity.statutRdv.stringValue().lower().containsIgnoreCase(statutRdv.toLowerCase()));
            }

        }
    }


}
