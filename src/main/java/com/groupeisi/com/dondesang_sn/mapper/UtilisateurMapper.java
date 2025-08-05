package com.groupeisi.com.dondesang_sn.mapper;
import com.groupeisi.com.dondesang_sn.entity.UtilisateurEntity;
import com.groupeisi.com.dondesang_sn.models.UtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {DonMapper.class})
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, UtilisateurEntity> {
    @Override
    @Mapping(source = "roleId", target = "role.id")
    UtilisateurEntity asEntity(UtilisateurDTO dto);

    @Override
    @Mapping(source = "role.id", target = "roleId")
    UtilisateurDTO asDto(UtilisateurEntity entity);
}
