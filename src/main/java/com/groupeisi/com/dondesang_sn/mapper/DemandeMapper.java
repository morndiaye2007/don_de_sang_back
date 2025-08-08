package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import com.groupeisi.com.dondesang_sn.models.DemandeDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public abstract class DemandeMapper implements EntityMapper<DemandeDTO, DemandeEntity> {
    @Override
    @Mapping(source = "stockSangId", target = "stockSang.id")
    @Mapping(source = "centreCollecteId", target = "centreCollecte.id")
    @Mapping(source = "hopitalId", target = "hopital.id")


    public abstract  DemandeEntity asEntity(DemandeDTO dto);

    @Override
    @Mapping(source = "hopital.id", target = "hopitalId")
    @Mapping(source = "stockSang.id", target = "stockSangId")
    @Mapping(source = "centreCollecte.id", target = "centreCollecteId")

    public abstract DemandeDTO asDto(DemandeEntity entity);

}