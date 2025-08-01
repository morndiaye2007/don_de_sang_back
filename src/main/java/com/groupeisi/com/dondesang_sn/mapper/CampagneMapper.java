package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CampagneEntity;
import com.groupeisi.com.dondesang_sn.entity.DonsEntity;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CampagneMapper extends EntityMapper<CampagneDTO, CampagneEntity> {
    @Override
    @Mapping(source = "agentId", target = "agent.id")
    CampagneEntity asEntity(CampagneDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    CampagneDTO asDto(CampagneEntity entity);
}
