package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.QDonneurEntity;
import com.groupeisi.com.dondesang_sn.mapper.DonneurMapper;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.repository.DonneurRepository;
import com.groupeisi.com.dondesang_sn.services.DonneurService;
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
public class DonneurServiceImpl implements DonneurService {

    private final DonneurRepository donneurRepository;
    private final DonneurMapper donneurMapper;

    @Override
    public DonneurDTO createDonneur(DonneurDTO donneurDTO) {
        var entity =donneurMapper.asEntity(donneurDTO);
        var entitySave = donneurRepository.save(entity);
        return donneurMapper.asDto(entitySave);
    }

    @Override
    public DonneurDTO updateDonneur(DonneurDTO donneurDTO) {
        var entityUpdate = donneurMapper.asEntity(donneurDTO);
        var updatedEntity = donneurRepository.save(entityUpdate);
        return donneurMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteDonneur(Long id) {
        if (!donneurRepository.existsById(id)) {
            throw new RuntimeException("Departement not found");
        }
        donneurRepository.deleteById(id);
    }

    @Override
    public DonneurDTO getDonneur(Long id) {
        var entity = donneurRepository.findById(id);
        return donneurMapper.asDto(entity.get());
    }


    @Override
    public Page<DonneurDTO> getAllDonneurs(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return donneurRepository.findAll(booleanBuilder, pageable)
                .map(donneurMapper::asDto);
    }
    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QDonneurEntity.donneurEntity;
            if (searchParams.containsKey("addresse"))
                booleanBuilder.and(qEntity.addresse.containsIgnoreCase(searchParams.get("addresse")));
            if (searchParams.containsKey("telephone"))
                booleanBuilder.and(qEntity.telephone.containsIgnoreCase(searchParams.get("telephone")));
            if (searchParams.containsKey("groupeSanguin"))
                booleanBuilder.and(qEntity.groupeSanguin.stringValue().equalsIgnoreCase(searchParams.get("groupeSanguin")));
            if (searchParams.containsKey("sexe"))
                booleanBuilder.and(qEntity.sexe.stringValue().equalsIgnoreCase(searchParams.get("sexe")));
            if (searchParams.containsKey("dni")){
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dni"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.dni.eq(date));
            }

        }
    }

    @Override
    public Response<Object> authenticateDonneur(String telephone, String motDePasse) {
        try {
            // Rechercher le donneur par téléphone
            var donneur = donneurRepository.findByTelephone(telephone);
            
            if (donneur.isEmpty()) {
                return Response.badRequest().setMessage("Numéro de téléphone non trouvé");
            }
            
            var donneurEntity = donneur.get();
            
            // Vérifier le mot de passe
            if (!Objects.equals(donneurEntity.getMdp(), motDePasse)) {
                return Response.badRequest().setMessage("Mot de passe incorrect");
            }
            
            // Retourner les données du donneur connecté
            return Response.ok().setPayload(Map.of(
                "donneur", donneurMapper.asDto(donneurEntity),
                "token", "donneur_" + donneurEntity.getId()
            )).setMessage("Connexion réussie");
            
        } catch (Exception e) {
            return Response.badRequest().setMessage("Erreur lors de la connexion: " + e.getMessage());
        }
    }

    @Override
    public void updateFcmToken(Long donneurId, String fcmToken) {
        var donneur = donneurRepository.findById(donneurId)
            .orElseThrow(() -> new RuntimeException("Donneur non trouvé"));
        
        donneur.setFcmToken(fcmToken);
        donneurRepository.save(donneur);
    }

}
