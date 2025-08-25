package com.groupeisi.com.dondesang_sn.services.Impl;

//import com.groupeisi.com.dondesang_sn.entity.QRdvEntity;
import com.groupeisi.com.dondesang_sn.mapper.RdvMapper;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import com.groupeisi.com.dondesang_sn.repository.RdvRepository;
import com.groupeisi.com.dondesang_sn.repository.CampagneRepository;
import com.groupeisi.com.dondesang_sn.repository.CentreCollecteRepository;
import com.groupeisi.com.dondesang_sn.repository.DonneurRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final RdvMapper rdvMapper;
    private final CampagneRepository campagneRepository;
    private final CentreCollecteRepository centreCollecteRepository;
    private final DonneurRepository donneurRepository;

    @Override
    public RdvDTO createRdv(RdvDTO rdvDTO) {
        var entity = rdvMapper.asEntity(rdvDTO);
        
        // Récupérer les entités complètes si les IDs sont fournis
        if (rdvDTO.getCampagneId() != null) {
            var campagne = campagneRepository.findById(rdvDTO.getCampagneId())
                .orElseThrow(() -> new RuntimeException("Campagne non trouvée avec l'ID: " + rdvDTO.getCampagneId()));
            entity.setCampagne(campagne);
        }
        
        if (rdvDTO.getCentreCollecteId() != null) {
            var centre = centreCollecteRepository.findById(rdvDTO.getCentreCollecteId())
                .orElseThrow(() -> new RuntimeException("Centre de collecte non trouvé avec l'ID: " + rdvDTO.getCentreCollecteId()));
            entity.setCentreCollecte(centre);
        }
        
        if (rdvDTO.getDonneurId() != null) {
            var donneur = donneurRepository.findById(rdvDTO.getDonneurId())
                .orElseThrow(() -> new RuntimeException("Donneur non trouvé avec l'ID: " + rdvDTO.getDonneurId()));
            entity.setDonneur(donneur);
        }
        
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
        var page = rdvRepository.findAll(pageable);
        return page.map(rdvMapper::asDto);
    }

    @Override
    public List<RdvDTO> getRdvsByDonneur(Long donneurId) {
        var rdvs = rdvRepository.findByDonneurId(donneurId);
        return rdvs.stream().map(rdvMapper::asDto).collect(Collectors.toList());
    }
}
