package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CampagneEntity;
import com.groupeisi.com.dondesang_sn.entity.RoleEntity;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, RoleEntity> {
    @Override
    @Mapping(source = "agentId", target = "agent.id")
    RoleEntity asEntity(RoleDTO dto);

    @Override
    @Mapping(source = "agent.id", target = "agentId")
    RoleDTO asDto(RoleEntity entity);
}
