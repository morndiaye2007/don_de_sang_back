package com.groupeisi.com.dondesang_sn.services.Impl;
import com.groupeisi.com.dondesang_sn.mapper.CampagneMapper;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.repository.CampagneRepository;
import com.groupeisi.com.dondesang_sn.services.CampagneService;
import com.groupeisi.com.dondesang_sn.entity.QCampagneEntity;

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

@Service
@Transactional
@RequiredArgsConstructor
public class CampagneServiceImpl implements CampagneService {

    private final CampagneRepository campagneRepository;
    private final CampagneMapper campagneMapper;


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QCampagneEntity.campagneEntity;
            if (searchParams.containsKey("nom_campagne"))
                booleanBuilder.and(qEntity.nom_campagne.containsIgnoreCase(searchParams.get("nom_campagne")));

            if (searchParams.containsKey("description"))
                booleanBuilder.and(qEntity.description.containsIgnoreCase(searchParams.get("description")));

            if (searchParams.containsKey("date_debut")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("date_debut"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.date_debut.eq(date));
            }
            if (searchParams.containsKey("date_fin")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("date_fin"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.date_fin.eq(date));
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

    @Override
    public CampagneDTO createCampagne(CampagneDTO campagneDTO) {
        var entity = campagneMapper.asEntity(campagneDTO);
        
        // Gérer la relation avec CentreCollecte si l'ID est fourni
        if (campagneDTO.getCentreCollecteId() != null) {
            var centreCollecte = new com.groupeisi.com.dondesang_sn.entity.CentreCollecteEntity();
            centreCollecte.setId(campagneDTO.getCentreCollecteId());
            entity.setCentreCollecte(centreCollecte);
        }
        
        // Ne pas définir CNTS pour éviter l'erreur TransientObjectException
        entity.setCnts(null);
        
        var entitySave = campagneRepository.save(entity);
        return campagneMapper.asDto(entitySave);
    }


    @Override
    public CampagneDTO updateCampagne(CampagneDTO campagneDTO) {
        var entityUpdate = campagneMapper.asEntity(campagneDTO);
        var updatedEntity = campagneRepository.save(entityUpdate);
        return campagneMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteCampagne(Long id) {
        if(!campagneRepository.existsById(id)) {
            throw new RuntimeException("Campagne not found");
        }
        campagneRepository.deleteById(id);
    }


    @Override
    public CampagneDTO getCampagne(Long id) {
        var entity = campagneRepository.findById(id);
        return campagneMapper.asDto(entity.get());
    }

    @Override
    public Page<CampagneDTO> getAllCampagnes(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return campagneRepository.findAll(booleanBuilder, pageable)
                .map(campagneMapper::asDto);    }
}
