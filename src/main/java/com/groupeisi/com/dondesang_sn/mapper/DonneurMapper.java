package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DonneurEntity;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class DonneurMapper implements EntityMapper<DonneurDTO, DonneurEntity> {
    @Override
    @Mapping(target = "campagne", ignore = true)
    @Mapping(target = "centreCollecte", ignore = true)
    public abstract DonneurEntity asEntity(DonneurDTO dto);

    @Override
    @Mapping(source = "campagne.id", target = "campagneId")
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    public abstract  DonneurDTO asDto(DonneurEntity entity);
}
