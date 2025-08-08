package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import com.groupeisi.com.dondesang_sn.models.StockSangDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class StockSangMapper implements EntityMapper<StockSangDTO, StockSangEntity> {
    @Override
    @Mapping(source = "hopital.id", target = "hopitalId")
    @Mapping(source = "cnts.id", target = "cntsId")

    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    public abstract StockSangDTO asDto(StockSangEntity entity);

    @Override

    @Mapping(source = "hopitalId", target = "hopital.id")
    @Mapping(source = "cntsId", target = "cnts.id")

    @Mapping(source = "centreCollecteId", target = "centreCollecte.id")

    public abstract StockSangEntity asEntity(StockSangDTO dto);
}
