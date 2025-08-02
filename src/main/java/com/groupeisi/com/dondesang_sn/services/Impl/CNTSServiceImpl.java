package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.mapper.CNTSMapper;
import com.groupeisi.com.dondesang_sn.mapper.CampagneMapper;
import com.groupeisi.com.dondesang_sn.models.CNTSDTO;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.repository.CNTSRepository;
import com.groupeisi.com.dondesang_sn.repository.CampagneRepository;
import com.groupeisi.com.dondesang_sn.services.CNTSService;
import com.groupeisi.com.dondesang_sn.services.CampagneService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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

@Transactional
@RequiredArgsConstructor
public class CNTSServiceImpl implements CNTSService {

    private final CNTSRepository cntsRepository;
    private final CNTSMapper cntsMapper;


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QCNTSEntity.CntsEntity;

            if (searchParams.containsKey("nom"))
                booleanBuilder.and(qEntity.nom.containsIgnoreCase(searchParams.get("nom")));
            if (searchParams.containsKey("prenom"))
                booleanBuilder.and(qEntity.prenom.containsIgnoreCase(searchParams.get("prenom")));
            if (searchParams.containsKey("mdp"))
                booleanBuilder.and(qEntity.mdp.containsIgnoreCase(searchParams.get("mdp")));

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

    @Override
    public CNTSDTO createCNTS(CNTSDTO campagneDTO) {
        var entity = cntsMapper.asEntity(campagneDTO);
        var entitySave = cntsRepository.save(entity);
        return cntsMapper.asDto(entitySave);
    }


    @Override
    public CNTSDTO updateCNTS(CNTSDTO campagneDTO) {
        var entityUpdate = cntsMapper.asEntity(campagneDTO);
        var updatedEntity = cntsRepository.save(entityUpdate);
        return cntsMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteCNTS(Long id) {
        if(!cntsRepository.existsById(id)) {
            throw new RuntimeException("Campagne not found");
        }
        cntsRepository.deleteById(id);
    }


    @Override
    public CNTSDTO getCNTS(Long id) {
        var entity = cntsRepository.findById(id);
        return cntsMapper.asDto(entity.get());
    }

    @Override
    public Page<CNTSDTO> getAllCNTSs(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return cntsRepository.findAll(booleanBuilder, pageable)
                .map(cntsMapper::asDto);    }
}
