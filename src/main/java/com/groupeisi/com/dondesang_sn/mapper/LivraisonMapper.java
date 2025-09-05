package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.LivraisonEntity;
import com.groupeisi.com.dondesang_sn.models.LivraisonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DemandeMapper.class, CentreCollecteMapper.class})
public interface LivraisonMapper {
    
    @Mapping(source = "demande.id", target = "demandeId")
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    LivraisonDTO asDto(LivraisonEntity entity);
    
    @Mapping(source = "demandeId", target = "demande.id")
    @Mapping(source = "centreCollecteId", target = "centreCollecte.id")
    @Mapping(target = "dateCreation", ignore = true)
    LivraisonEntity asEntity(LivraisonDTO dto);
}
