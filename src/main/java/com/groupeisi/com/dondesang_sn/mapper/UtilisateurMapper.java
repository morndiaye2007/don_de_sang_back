package com.groupeisi.com.dondesang_sn.mapper;
import com.groupeisi.com.dondesang_sn.entity.UtilisateurEntity;
import com.groupeisi.com.dondesang_sn.models.UtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public abstract class UtilisateurMapper implements EntityMapper<UtilisateurDTO, UtilisateurEntity> {
    @Override
    @Mapping(source = "roleId", target = "role.id")

    public abstract UtilisateurEntity asEntity(UtilisateurDTO dto);

    @Override
    @Mapping(source = "role.id", target = "roleId")

    public abstract UtilisateurDTO asDto(UtilisateurEntity entity);
}
