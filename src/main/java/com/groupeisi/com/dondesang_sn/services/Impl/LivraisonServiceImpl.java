package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.LivraisonEntity;
import com.groupeisi.com.dondesang_sn.entity.QLivraisonEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutLivraison;
import com.groupeisi.com.dondesang_sn.mapper.LivraisonMapper;
import com.groupeisi.com.dondesang_sn.models.LivraisonDTO;
import com.groupeisi.com.dondesang_sn.repository.DemandeRepository;
import com.groupeisi.com.dondesang_sn.repository.LivraisonRepository;
import com.groupeisi.com.dondesang_sn.services.LivraisonService;
import com.querydsl.core.BooleanBuilder;
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

@Service
@Transactional
@RequiredArgsConstructor
public class LivraisonServiceImpl implements LivraisonService {
    
    private final LivraisonRepository livraisonRepository;
    private final DemandeRepository demandeRepository;
    private final LivraisonMapper livraisonMapper;

    @Override
    public LivraisonDTO createLivraison(LivraisonDTO livraisonDTO) {
        var entity = livraisonMapper.asEntity(livraisonDTO);
        var entitySave = livraisonRepository.save(entity);
        return livraisonMapper.asDto(entitySave);
    }

    @Override
    public LivraisonDTO createLivraisonFromDemande(Long demandeId) {
        var demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        var livraison = new LivraisonEntity();
        livraison.setDemande(demande);
        livraison.setQuantiteLivree(demande.getNombreDepoche());
        livraison.setStatutLivraison(StatutLivraison.EN_PREPARATION);
        livraison.setNumeroBonLivraison(generateBonLivraisonNumber());
        livraison.setObservations("Livraison créée automatiquement suite à validation de la demande #" + demandeId);
        
        var entitySave = livraisonRepository.save(livraison);
        return livraisonMapper.asDto(entitySave);
    }

    @Override
    public LivraisonDTO updateLivraison(LivraisonDTO livraisonDTO) {
        var entityUpdate = livraisonMapper.asEntity(livraisonDTO);
        var updatedEntity = livraisonRepository.save(entityUpdate);
        return livraisonMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteLivraison(Long id) {
        if (!livraisonRepository.existsById(id)) {
            throw new RuntimeException("Livraison non trouvée");
        }
        livraisonRepository.deleteById(id);
    }

    @Override
    public LivraisonDTO getLivraison(Long id) {
        var entity = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));
        return livraisonMapper.asDto(entity);
    }

    @Override
    public Page<LivraisonDTO> getAllLivraisons(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return livraisonRepository.findAll(booleanBuilder, pageable)
                .map(livraisonMapper::asDto);
    }

    @Override
    public List<LivraisonDTO> getLivraisonsByDemande(Long demandeId) {
        return livraisonRepository.findByDemandeId(demandeId)
                .stream()
                .map(livraisonMapper::asDto)
                .toList();
    }

    @Override
    public List<LivraisonDTO> getLivraisonsByHopital(Long hopitalId) {
        return livraisonRepository.findByHopitalId(hopitalId)
                .stream()
                .map(livraisonMapper::asDto)
                .toList();
    }

    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QLivraisonEntity.livraisonEntity;
            
            if (searchParams.containsKey("statutLivraison")) {
                booleanBuilder.and(qEntity.statutLivraison.stringValue().equalsIgnoreCase(searchParams.get("statutLivraison")));
            }
            
            if (searchParams.containsKey("hopitalId")) {
                booleanBuilder.and(qEntity.demande.hopital.id.eq(Long.parseLong(searchParams.get("hopitalId"))));
            }
            
            if (searchParams.containsKey("dateLivraison")) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dateLivraison"));
                    booleanBuilder.and(qEntity.dateLivraison.loe(date));
                } catch (ParseException e) {
                    throw new RuntimeException("Format de date invalide (attendu: yyyy-MM-dd)", e);
                }
            }
        }
    }

    private String generateBonLivraisonNumber() {
        return "BL" + System.currentTimeMillis();
    }
}
