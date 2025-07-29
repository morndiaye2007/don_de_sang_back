package com.groupeisi.com.dondesang_sn.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public enum RoleUtilisateur {
    AGENT("Masculin"),
    MEDECIN("Féminin"),
    ADMIN("Féminin");


    @Getter
    @Setter
    private String description;

    RoleUtilisateur(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RoleUtilisateur fromValue(Object roleUtilisateur) {
        if (roleUtilisateur instanceof Map) {
            Map<String, Object> mapRoleUtilisateur = (Map<String, Object>) roleUtilisateur;
            if (mapRoleUtilisateur.containsKey("name")) {
                return RoleUtilisateur.valueOf(mapRoleUtilisateur.get("name").toString());
            }
        }
        if (roleUtilisateur instanceof String) {
            return RoleUtilisateur.valueOf(roleUtilisateur.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", SexType.class, roleUtilisateur, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<RoleUtilisateur> getAllUtilisateur() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
