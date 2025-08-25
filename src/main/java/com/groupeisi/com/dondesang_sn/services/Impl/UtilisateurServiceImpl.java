package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.QUtilisateurEntity;
import com.groupeisi.com.dondesang_sn.mapper.UtilisateurMapper;
import com.groupeisi.com.dondesang_sn.models.UtilisateurDTO;
import com.groupeisi.com.dondesang_sn.repository.UtilisateurRepository;
import com.groupeisi.com.dondesang_sn.services.UtilisateurService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO) {
        var entity = utilisateurMapper.asEntity(utilisateurDTO);
        if (entity.getMdp() != null && !entity.getMdp().isBlank()) {
            entity.setMdp(passwordEncoder.encode(entity.getMdp()));
        }
        var entitySave = utilisateurRepository.save(entity);
        return utilisateurMapper.asDto(entitySave);
    }

    @Override
    public UtilisateurDTO updateUtilisateur(UtilisateurDTO utilisateurDTO)  {
        var entityUpdate = utilisateurMapper.asEntity(utilisateurDTO);
        if (utilisateurDTO.getMdp() != null && !utilisateurDTO.getMdp().isBlank()) {
            entityUpdate.setMdp(passwordEncoder.encode(utilisateurDTO.getMdp()));
        } else {
            // conserver l'ancien mot de passe si non fourni
            utilisateurRepository.findById(utilisateurDTO.getId()).ifPresent(old -> entityUpdate.setMdp(old.getMdp()));
        }
        var updatedEntity = utilisateurRepository.save(entityUpdate);
        return utilisateurMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        if(!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UtilisateurDTO getUtilisateur(Long id) {
        var entity = utilisateurRepository.findById(id);
        return utilisateurMapper.asDto(entity.get());
    }


    @Override
    public Page<UtilisateurDTO> getAllUtilisateurs(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return utilisateurRepository.findAll(booleanBuilder, pageable)
                .map(utilisateurMapper::asDto);
    }
    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QUtilisateurEntity.utilisateurEntity;
            if (searchParams.containsKey("nom"))
                booleanBuilder.and(qEntity.nom.containsIgnoreCase(searchParams.get("nom")));
            if (searchParams.containsKey("prenom"))
                booleanBuilder.and(qEntity.prenom.containsIgnoreCase(searchParams.get("prenom")));



        }
    }


}
