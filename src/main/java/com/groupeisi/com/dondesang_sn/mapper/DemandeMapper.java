package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import com.groupeisi.com.dondesang_sn.models.DemandeDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public interface DemandeMapper extends EntityMapper<DemandeDTO, DemandeEntity> {
    @Override
    @Mapping(source = "hopitalId", target = "hopital.id")
    @Mapping(source = "stockSangId", target = "stockSang.id")

    DemandeEntity asEntity(DemandeDTO dto);

    @Override
    @Mapping(source = "hopital.id", target = "hopitalId")
    @Mapping(source = "stockSang.id", target = "stockSangId")

    DemandeDTO asDto(DemandeEntity entity);

}