package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.MedecinEntity;
import com.groupeisi.com.dondesang_sn.models.MedecinDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HopitalMapper.class})
public interface MedecinMapper {
    
    @Mapping(source = "hopital.id", target = "hopitalId")
    MedecinDTO toDto(MedecinEntity entity);
    
    @Mapping(source = "hopitalId", target = "hopital.id")
    @Mapping(target = "demandes", ignore = true)
    MedecinEntity toEntity(MedecinDTO dto);
}
