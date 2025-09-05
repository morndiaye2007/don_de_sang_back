package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.QCentreCollecteEntity;
import com.groupeisi.com.dondesang_sn.mapper.CentreCollecteMapper;
import com.groupeisi.com.dondesang_sn.models.CentreCollecteDTO;
import com.groupeisi.com.dondesang_sn.repository.CentreCollecteRepository;
import com.groupeisi.com.dondesang_sn.services.CentreCollecteService;
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

public class CentreCollecteServiceImpl implements CentreCollecteService {

    private final CentreCollecteRepository centreCollecteRepository;
    private final CentreCollecteMapper centreCollecteMapper;

    @Override
    public CentreCollecteDTO createCentreCollecte(CentreCollecteDTO centreCollecteDTO) {
        var entity = centreCollecteMapper.asEntity(centreCollecteDTO);
         var entitySave = centreCollecteRepository.save(entity);
        return centreCollecteMapper.asDto(entitySave);
    }

    @Override
    public CentreCollecteDTO updateCentreCollecte(CentreCollecteDTO centreCollecteDTO)  {
        var entityUpdate = centreCollecteMapper.asEntity(centreCollecteDTO);
        var updatedEntity = centreCollecteRepository.save(entityUpdate);
        return centreCollecteMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteCentreCollecte(Long id) {
       if(!centreCollecteRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        centreCollecteRepository.deleteById(id);
    }

    @Override
    public CentreCollecteDTO getCentreCollecte(Long id) {
        var entity = centreCollecteRepository.findById(id);
        return centreCollecteMapper.asDto(entity.get());
    }


    @Override
    public Page<CentreCollecteDTO> getAllCentreCollectes(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return centreCollecteRepository.findAll(booleanBuilder, pageable)
                .map(centreCollecteMapper::asDto);
    }
    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QCentreCollecteEntity.centreCollecteEntity;
            if (searchParams.containsKey("nom"))
                booleanBuilder.and(qEntity.nom.containsIgnoreCase(searchParams.get("nom")));
            if (searchParams.containsKey("telephone"))
                booleanBuilder.and(qEntity.telephone.containsIgnoreCase(searchParams.get("telephone")));
            if (searchParams.containsKey("localisation"))
                booleanBuilder.and(qEntity.localisation.containsIgnoreCase(searchParams.get("localisation")));

            String statutCentre = searchParams.get("statutCentre");
            if (statutCentre != null && !statutCentre.isEmpty()) {
                booleanBuilder.and(qEntity.statutCentre.stringValue().lower().containsIgnoreCase(statutCentre.toLowerCase()));
            }
            String region = searchParams.get("region");
            if (region != null && !region.isEmpty()) {
                booleanBuilder.and(qEntity.region.stringValue().lower().containsIgnoreCase(region.toLowerCase()));
            }
        }
    }


}
