package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;
import com.groupeisi.com.dondesang_sn.models.RdvDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RdvMapper extends EntityMapper<RdvDTO, RdvEntity> {
    @Override
    RdvDTO asDto(RdvEntity entity);

    @Override
    RdvEntity asEntity(RdvDTO dto);
}
