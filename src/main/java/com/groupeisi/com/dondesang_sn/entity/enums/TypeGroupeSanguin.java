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
public enum TypeGroupeSanguin {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-"),
    Inconnue("_");
    @Getter
    @Setter
    private String description;

    TypeGroupeSanguin(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeGroupeSanguin fromValue(Object TypeGroupeSanguin) {
        if (TypeGroupeSanguin instanceof Map) {
            Map<String, Object> mapTypeDemande = (Map<String, Object>) TypeGroupeSanguin;
            if (mapTypeDemande.containsKey("name")) {
                return com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin.valueOf(mapTypeDemande.get("name").toString());
            }
        }
        if (TypeGroupeSanguin instanceof String) {
            return com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin.valueOf(TypeGroupeSanguin.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatutDemande.class, TypeGroupeSanguin, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<TypeGroupeSanguin> getAllTypeGroupeSanguin() {
        return stream(values())
                .collect(Collectors.toSet());
    }

}
