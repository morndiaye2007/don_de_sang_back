package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DonsEntity;
import com.groupeisi.com.dondesang_sn.models.DonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DonMapper extends EntityMapper<DonDTO, DonsEntity> {
    @Override
    @org.mapstruct.Mapping(source = "agentId", target = "agent.id")
    DonsEntity asEntity(DonDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    DonDTO asDto(DonsEntity entity);
}
