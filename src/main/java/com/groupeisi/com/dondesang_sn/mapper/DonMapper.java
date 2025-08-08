package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DonsEntity;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class DonMapper implements EntityMapper<DonDTO, DonsEntity> {
    @Override
    @Mapping(source = "campagneId", target = "campagne.id")
    @Mapping(source = "donneurId", target = "donneur.id")

    public abstract DonsEntity asEntity(DonDTO dto);

    @Override
    @Mapping(source = "campagne.id", target = "campagneId")
    @Mapping(source = "donneur.id", target = "donneurId")


    public abstract DonDTO asDto(DonsEntity entity);
}
