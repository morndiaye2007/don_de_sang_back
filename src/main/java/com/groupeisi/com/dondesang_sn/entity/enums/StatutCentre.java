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

public enum StatutCentre {
    ACTIF("Demande en attente"),
    INACTIF("Demande validee");

    @Getter
    @Setter
    private String description;

    StatutCentre(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatutCentre fromValue(Object statutCentre) {
        if (statutCentre instanceof Map) {
            Map<String, Object> mapStatutCentre = (Map<String, Object>) statutCentre;
            if (mapStatutCentre.containsKey("name")) {
                return StatutCentre.valueOf(mapStatutCentre.get("name").toString());
            }
        }
        if (statutCentre instanceof String) {
            return StatutCentre.valueOf(statutCentre.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatutCentre.class, statutCentre, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<StatutCentre> getAllDemandes() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
