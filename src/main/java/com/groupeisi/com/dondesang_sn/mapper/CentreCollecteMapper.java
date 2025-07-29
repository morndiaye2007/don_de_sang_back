package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CentreCollecteEntity;
import com.groupeisi.com.dondesang_sn.models.CentreCollecteDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public interface CentreCollecteMapper extends EntityMapper<CentreCollecteDTO, CentreCollecteEntity> {
    @Override
    @Mapping(source = "agentId", target = "agent.id")
    CentreCollecteEntity asEntity(CentreCollecteDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    CentreCollecteDTO asDto(CentreCollecteEntity entity);
}
