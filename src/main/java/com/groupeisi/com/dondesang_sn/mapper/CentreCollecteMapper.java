package com.groupeisi.com.dondesang_sn.mapper;

import com.groupeisi.com.dondesang_sn.entity.CentreCollecteEntity;
import com.groupeisi.com.dondesang_sn.models.CentreCollecteDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public interface CentreCollecteMapper extends EntityMapper<CentreCollecteDTO, CentreCollecteEntity> {
    @Override
    @Mapping(source = "stockSangId", target = "stockSang.id")
    @Mapping(source = "campagneId", target = "campagnes.id")
    @Mapping(source = "donId", target = "dons.id")
    @Mapping(source = "rdvId", target = "rdv.id")
    @Mapping(source = "demandeId", target = "demande.id")

    CentreCollecteEntity asEntity(CentreCollecteDTO dto);

    @Override
    @Mapping(source = "stockSang.id", target = "stockSangId")
    @Mapping(source = "campagnes.id", target = "campagneId")
    @Mapping(source = "dons.id", target = "donId")
    @Mapping(source = "rdv.id", target = "rdvId")
    @Mapping(source = "demande.id", target = "demandeId")

    CentreCollecteDTO asDto(CentreCollecteEntity entity);
}
