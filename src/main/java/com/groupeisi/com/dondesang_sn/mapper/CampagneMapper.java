package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CampagneEntity;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CampagneMapper implements EntityMapper<CampagneDTO, CampagneEntity> {
    @Override
    @Mapping(source = "centreCollecteId", target = "centreCollecte.id")
    @Mapping(target = "cnts", ignore = true)
    public abstract CampagneEntity asEntity(CampagneDTO dto);

    @Override
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    @Mapping(source = "cnts.id", target = "cntsId")
    @Mapping(source = "statusCampagne", target = "statusCampagne")
    public abstract CampagneDTO asDto(CampagneEntity entity);

}
