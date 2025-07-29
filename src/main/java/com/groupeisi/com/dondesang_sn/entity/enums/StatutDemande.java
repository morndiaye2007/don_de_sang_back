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

public enum StatutDemande {
    EN_ATTENTE("Demande en attente"),
    VALIDEE("Demande validee"),
    ANNULEE("Demande annule");

    @Getter
    @Setter
    private String description;

    StatutDemande(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatutDemande fromValue(Object typeContrat) {
        if (typeContrat instanceof Map) {
            Map<String, Object> mapTypeContrat = (Map<String, Object>) typeContrat;
            if (mapTypeContrat.containsKey("name")) {
                return StatutDemande.valueOf(mapTypeContrat.get("name").toString());
            }
        }
        if (typeContrat instanceof String) {
            return StatutDemande.valueOf(typeContrat.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatutDemande.class, typeContrat, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<StatutDemande> getAllDemandes() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
