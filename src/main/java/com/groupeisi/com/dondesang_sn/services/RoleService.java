package com.groupeisi.com.dondesang_sn.services;

import com.groupeisi.com.dondesang_sn.models.CampagneDTO;
import com.groupeisi.com.dondesang_sn.models.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(RoleDTO roleDTO);
    void deleteRole(Long id);
    RoleDTO getRole(Long id);
    Page<RoleDTO> getAllRoles(Map<String, String> searchParams, Pageable pageable);
}
