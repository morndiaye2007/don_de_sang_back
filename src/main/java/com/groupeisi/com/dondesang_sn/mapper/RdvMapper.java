package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RdvMapper extends EntityMapper<RdvDTO, RdvEntity> {
    @Override
    @Mapping(source = "campagneId", target = "campagne.id")
    @Mapping(source = "centreCollecteId", target = "centreCollecte.id")
    @Mapping(source = "donneurId", target = "donneur.id")
    RdvDTO asDto(RdvEntity entity);

    @Override
    @Mapping(source = "campagne.id", target = "campagneId")
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    @Mapping(source = "donneur.id", target = "donneurId")
    RdvEntity asEntity(RdvDTO dto);
}
