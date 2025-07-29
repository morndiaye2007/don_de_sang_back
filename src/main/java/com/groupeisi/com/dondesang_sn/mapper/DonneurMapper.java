package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DonneurEntity;
import com.groupeisi.com.dondesang_sn.models.DonneurDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DonneurMapper extends EntityMapper<DonneurDTO, DonneurEntity> {
    @Override
    @Mapping(source = "agentId", target = "agent.id")
    DonneurEntity asEntity(DonneurDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    DonneurDTO asDto(DonneurEntity entity);
}
