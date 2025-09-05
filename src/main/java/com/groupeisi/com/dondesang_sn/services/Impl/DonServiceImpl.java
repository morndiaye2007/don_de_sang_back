package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.DonsEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatusDon;
import com.groupeisi.com.dondesang_sn.mapper.DonMapper;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import com.groupeisi.com.dondesang_sn.repository.DonRepository;
import com.groupeisi.com.dondesang_sn.repository.RdvRepository;
import com.groupeisi.com.dondesang_sn.services.DonService;

import lombok.RequiredArgsConstructor;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DonServiceImpl implements DonService {

    private final DonRepository donRepository;
    private final DonMapper donMapper;
    private final RdvRepository rdvRepository;

    @Override
    public DonDTO createDon(DonDTO donDTO) {
        var entity = donMapper.asEntity(donDTO);
        var entitySave = donRepository.save(entity);
        return donMapper.asDto(entitySave);
    }

    @Override
    public DonDTO createDonFromRdv(Long rdvId) {
        var rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV non trouvé"));
        
        var don = new DonsEntity();
        don.setDateDon(new Date());
        don.setNombrePoche(1.0); // 1 poche par don standard
        don.setStatutDon(StatusDon.EFFECTUE);
        don.setDonneur(rdv.getDonneur());
        don.setCampagne(rdv.getCampagne());
        
        var savedEntity = donRepository.save(don);
        return donMapper.asDto(savedEntity);
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
        // Utilisation de requêtes JPA standard au lieu de QueryDSL
        return donRepository.findAll(pageable)
                .map(donMapper::asDto);
    }

    @Override
    public List<DonDTO> getDonsByDonneur(Long donneurId) {
        var dons = donRepository.findByDonneurId(donneurId);
        return dons.stream()
            .map(donMapper::asDto)
            .collect(Collectors.toList());
    }
}
