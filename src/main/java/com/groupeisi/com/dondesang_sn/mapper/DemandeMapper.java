package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import com.groupeisi.com.dondesang_sn.models.DemandeDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public interface DemandeMapper extends EntityMapper<DemandeDTO, DemandeEntity> {
    @Override
    @Mapping(source = "agentId", target = "agent.id")
    DemandeEntity asEntity(DemandeDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    DemandeDTO asDto(DemandeEntity entity);

}