package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;
import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import com.groupeisi.com.dondesang_sn.models.StockSangDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StockSangMapper extends EntityMapper<StockSangDTO, StockSangEntity> {
    @Override
    @Mapping(source = "hopitalId", target = "hopital.id")
    @Mapping(source = "centreCollecteId", target = "centreCollecte.id")
    StockSangDTO asDto(StockSangEntity entity);

    @Override
    @Mapping(source = "hopital.id", target = "hopitalId")
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")
    StockSangEntity asEntity(StockSangDTO dto);
}
