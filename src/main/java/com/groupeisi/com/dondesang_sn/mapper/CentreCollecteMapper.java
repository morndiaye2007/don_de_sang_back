package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CentreCollecteEntity;
import com.groupeisi.com.dondesang_sn.models.CentreCollecteDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public abstract class CentreCollecteMapper implements EntityMapper<CentreCollecteDTO, CentreCollecteEntity> {
    @Override
    @Mapping(source = "cntsId", target = "cnts.id")
    @Mapping(source = "hopitalId", target = "hopital.id")

  public abstract CentreCollecteEntity asEntity(CentreCollecteDTO dto);

    @Override
    @Mapping(source = "cnts.id", target = "cntsId")
    @Mapping(source = "hopital.id", target = "hopitalId")

    public abstract CentreCollecteDTO asDto(CentreCollecteEntity entity);
}
