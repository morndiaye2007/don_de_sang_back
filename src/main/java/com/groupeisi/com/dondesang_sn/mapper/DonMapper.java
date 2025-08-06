package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DonsEntity;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DonMapper extends EntityMapper<DonDTO, DonsEntity> {
    @Override
    @org.mapstruct.Mapping(source = "donneurId", target = "donneur.id")
    @org.mapstruct.Mapping(source = "campagneId", target = "campagne.id")

    DonsEntity asEntity(DonDTO dto);

    @Override
    @Mapping(source = "donneur.id", target = "donneurId")
    @Mapping(source = "campagne.id", target = "campagneId")

    DonDTO asDto(DonsEntity entity);
}
