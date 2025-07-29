package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.HopitalEntity;
import com.groupeisi.com.dondesang_sn.models.HopitalDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring",uses = DonMapper.class)
public interface HopitalMapper extends EntityMapper<HopitalDTO, HopitalEntity> {
    @Override
    @Mapping(source = "agent.id",target = "agentId")
    HopitalDTO asDto(HopitalEntity entity);

    @Override
    @Mapping(source = "agentId",target = "agent.id")
    HopitalEntity asEntity(HopitalDTO dto);
}
