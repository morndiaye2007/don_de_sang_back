package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupeisi.com.dondesang_sn.entity.RoleEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.RoleUtilisateur;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilisateurDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty
    private String nom;
    private String prenom;
    private String mdp;
    @Enumerated(EnumType.STRING)
    private RoleUtilisateur roleUtilisateur;
    private RoleDTO roleEntity;
    private Long roleId;

}
