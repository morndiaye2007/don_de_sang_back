package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CNTSEntity;
import com.groupeisi.com.dondesang_sn.models.CNTSDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CNTSMapper implements EntityMapper<CNTSDTO, CNTSEntity> {
    @Override
    public abstract CNTSEntity asEntity(CNTSDTO dto);

    @Override
    public abstract CNTSDTO asDto(CNTSEntity entity);
}
