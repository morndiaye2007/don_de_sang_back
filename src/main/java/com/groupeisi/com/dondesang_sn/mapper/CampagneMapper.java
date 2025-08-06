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
    @Mapping(source = "centrecollecteId", target = "centrecollecte.id")
    @Mapping(source = "rdvId", target = "rdv.id")
    @Mapping(source = "donnneurId", target = "donnneur.id")

    CampagneEntity asEntity(CampagneDTO dto);

    @Override
    @Mapping(source = "centrecollecte.id", target = "centrecollecteId")
    @Mapping(source = "rdv.id", target = "rdvId")
    @Mapping(source = "donnneur.id", target = "donnneurId")

    CampagneDTO asDto(CampagneEntity entity);

}
