package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CNTSEntity;
import com.groupeisi.com.dondesang_sn.models.CNTSDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CNTSMapper extends EntityMapper<CNTSDTO, CNTSEntity> {
    @Override
    CNTSEntity asEntity(CNTSDTO dto);

    @Override
    CNTSDTO asDto(CNTSEntity entity);
}
