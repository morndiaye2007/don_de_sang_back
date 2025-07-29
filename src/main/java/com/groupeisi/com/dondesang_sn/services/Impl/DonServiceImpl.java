package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.mapper.DonMapper;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import com.groupeisi.com.dondesang_sn.repository.DonRepository;
import com.groupeisi.com.dondesang_sn.services.DonService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
//import com.groupeisi.com.dondesang_sn.entity.QDonEntity;

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
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class DonServiceImpl implements DonService {

    private final DonRepository donRepository;
    private final DonMapper donMapper;

    @Override
    public DonDTO createDon(DonDTO donDTO) {
        var entity = donMapper.asEntity(donDTO);
        var entitySave = donRepository.save(entity);
        return donMapper.asDto(entitySave);
    }

    @Override
    public DonDTO updateDon(DonDTO donDTO) {
        var entityUpdate = donMapper.asEntity(donDTO);
        var updatedEntity = donRepository.save(entityUpdate);
        return donMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteDon(Long id) {
        if(!donRepository.existsById(id)) {
            throw new RuntimeException("Mission not found");
        }
        donRepository.deleteById(id);
    }

    @Override
    public DonDTO getDon(Long id) {
        var entity = donRepository.findById(id);
        return donMapper.asDto(entity.get());
    }

    @Override
    public Page<DonDTO> getAllDons(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return donRepository.findAll(booleanBuilder, pageable)
                .map(donMapper::asDto);
    }


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QDonEntity.donEntity;
            if (searchParams.containsKey("titre"))
                booleanBuilder.and(qEntity.titre.containsIgnoreCase(searchParams.get("titre")));

            if (searchParams.containsKey("dateDon")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dateDon"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.dateDon.eq(date));
            }

            String statutDon = searchParams.get("statutDon");
            if (statutDon != null && !statutDon.isEmpty()) {
                booleanBuilder.and(qEntity.statutDon.stringValue().lower().containsIgnoreCase(statutDon.toLowerCase()));
            }
        }
    }

    private void addDatePredicate(Map<String, String> params, String key, BooleanBuilder builder, Function<Date, BooleanExpression> expressionFunction) {
        if (params.containsKey(key)) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(params.get(key));
                builder.and(expressionFunction.apply(date));
            } catch (ParseException e) {
                throw new RuntimeException("Erreur lors du parsing de la date pour la clé: " + key, e);
            }
        }
    }
}
