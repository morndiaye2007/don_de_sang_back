package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.mapper.CampagneMapper;
import com.groupeisi.com.dondesang_sn.mapper.RoleMapper;
import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.RoleDTO;
import com.groupeisi.com.dondesang_sn.repository.CampagneRepository;
import com.groupeisi.com.dondesang_sn.repository.RoleRepository;
import com.groupeisi.com.dondesang_sn.services.CampagneService;
import com.groupeisi.com.dondesang_sn.services.RoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QRoleEntity.RoleEntity;
            if (searchParams.containsKey("nom_role"))
                booleanBuilder.and(qEntity.nom_role.containsIgnoreCase(searchParams.get("nom_role")));


        }
    }

    private void addDatePredicate(Map<String, String> params, String key, BooleanBuilder builder, Function<Date, BooleanExpression> expressionFunction) {
        if (params.containsKey(key)) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(params.get(key));
                builder.and(expressionFunction.apply(date));
            } catch (ParseException e) {
                throw new RuntimeException("Erreur lors du parsing de la date pour la clé: " + key, e);
            }
        }
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        var entity = roleMapper.asEntity(roleDTO);
        var entitySave = roleRepository.save(entity);
        return roleMapper.asDto(entitySave);
    }


    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) {
        var entityUpdate = roleMapper.asEntity(roleDTO);
        var updatedEntity = roleRepository.save(entityUpdate);
        return roleMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteRole(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new RuntimeException("Campagne not found");
        }
        roleRepository.deleteById(id);
    }


    @Override
    public RoleDTO getRole(Long id) {
        var entity = roleRepository.findById(id);
        return roleMapper.asDto(entity.get());
    }

    @Override
    public Page<RoleDTO> getAllRoles(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return roleRepository.findAll(booleanBuilder, pageable)
                .map(roleMapper::asDto);    }
}
