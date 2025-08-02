package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CNTSEntity;
import com.groupeisi.com.dondesang_sn.entity.CampagneEntity;
import com.groupeisi.com.dondesang_sn.models.CNTSDTO;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CNTSMapper extends EntityMapper<CNTSDTO, CNTSEntity> {
    @Override
    @Mapping(source = "agentId", target = "agent.id")
    CNTSEntity asEntity(CNTSDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    CNTSDTO asDto(CNTSEntity entity);
}
