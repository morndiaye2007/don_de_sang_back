package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.mapper.DemandeMapper;
import com.groupeisi.com.dondesang_sn.models.DemandeDTO;
import com.groupeisi.com.dondesang_sn.repository.DemandeRepository;
import com.groupeisi.com.dondesang_sn.services.DemandeService;
import com.querydsl.core.BooleanBuilder;
//import com.groupeisi.com.dondesang_sn.entity.QDemandeEntity;
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
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository demandeRepository;
    private final DemandeMapper demandeMapper;

    @Override
    public DemandeDTO createDemande(DemandeDTO demandeDTO) {
        var entity = demandeMapper.asEntity(demandeDTO);
        var entitySave = demandeRepository.save(entity);
        return demandeMapper.asDto(entitySave);    }

    @Override
    public DemandeDTO updateDemande(DemandeDTO demandeDTO) {
        var entityUpdate = demandeMapper.asEntity(demandeDTO);
        var updatedEntity = demandeRepository.save(entityUpdate);
        return demandeMapper.asDto(updatedEntity);    }

    @Override
    public void deleteDemande(Long id) {
        if(!demandeRepository.existsById(id)) {
            throw new RuntimeException("Contrat not found");
        }
        demandeRepository.deleteById(id);
    }

    @Override
    public DemandeDTO getDemande(Long id) {
        var entity = demandeRepository.findById(id);
        return demandeMapper.asDto(entity.get());    }

    @Override
    public Page<DemandeDTO> getAllDemandes(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return demandeRepository.findAll(booleanBuilder, pageable)
                .map(demandeMapper::asDto);
    }

    @Override
    public Page<DemandeDTO> getAllContratsAgent(Map<String, String> searchParams, Pageable pageable, Long idAgent) {
        var booleanBuilder = new BooleanBuilder();
        var qContrat = QContratEntity.contratEntity;
        booleanBuilder.and(qContrat.agent.id.eq(idAgent));
        buildSearch(searchParams, booleanBuilder);
        return demandeRepository.findAll(booleanBuilder, pageable)
                .map(demandeMapper::asDto);
    }

    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QDemandeEntity.demandeEntity;
            if (searchParams.containsKey("groupeSanguin"))
                booleanBuilder.and(qEntity.groupeSanguin.stringValue().equalsIgnoreCase(searchParams.get("groupeSanguin")));

            if (searchParams.containsKey("statutDemande"))
                booleanBuilder.and(qEntity.statutDemande.stringValue().equalsIgnoreCase(searchParams.get("statutDemande")));


            if (searchParams == null || searchParams.isEmpty()) return;
            if (searchParams.containsKey("dateDemande")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dateDemande"));
                    booleanBuilder.and(qEntity.dateDemande.loe(date)); // less or equal

                } catch (ParseException e) {
                    throw new RuntimeException("Format de date_fin invalide (attendu: yyyy-MM-dd)",e);
                }
            }


        }
    }

}
