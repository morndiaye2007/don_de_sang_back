package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CampagneEntity;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CampagneMapper implements EntityMapper<CampagneDTO, CampagneEntity> {
    @Override
    @Mapping(source = "cntsId", target = "cnts.id")

    public abstract CampagneEntity asEntity(CampagneDTO dto);

    @Override
    @Mapping(source = "cnts.id", target = "cntsId")

    public abstract CampagneDTO asDto(CampagneEntity entity);

}
