package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class RdvMapper implements EntityMapper<RdvDTO, RdvEntity> {

    @Override
    @Mapping(source = "campagne.id", target = "campagneId")
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    @Mapping(source = "donneur.id", target = "donneurId")

    public abstract RdvDTO asDto(RdvEntity entity);

    @Override
    @Mapping(target = "campagne", ignore = true)
    @Mapping(target = "centreCollecte", ignore = true)
    @Mapping(target = "donneur", ignore = true)
    public abstract RdvEntity asEntity(RdvDTO dto);
}
