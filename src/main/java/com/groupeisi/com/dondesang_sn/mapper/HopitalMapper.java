package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.HopitalEntity;
import com.groupeisi.com.dondesang_sn.models.HopitalDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring",uses = DonMapper.class)
public abstract class HopitalMapper implements EntityMapper<HopitalDTO, HopitalEntity> {
    @Override
    public abstract HopitalDTO asDto(HopitalEntity entity);

    @Override
    public abstract HopitalEntity asEntity(HopitalDTO dto);
}
