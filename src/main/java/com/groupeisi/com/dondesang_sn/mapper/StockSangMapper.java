package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import com.groupeisi.com.dondesang_sn.models.StockSangDTO;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StockSangMapper extends EntityMapper<StockSangDTO, StockSangEntity> {
}
