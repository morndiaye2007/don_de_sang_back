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

public enum StatutRdv {

    EN_ATTENTE("Demande en attente"),
    VALIDEE("Demande validee"),
    ANNULEE("Demande annule");

    @Getter
    @Setter
    private String description;

    StatutRdv(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatutRdv fromValue(Object statutRdv) {
        if (statutRdv instanceof Map) {
            Map<String, Object> mapTypeContrat = (Map<String, Object>) statutRdv;
            if (mapTypeContrat.containsKey("name")) {
                return StatutRdv.valueOf(mapTypeContrat.get("name").toString());
            }
        }
        if (statutRdv instanceof String) {
            return StatutRdv.valueOf(statutRdv.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatutRdv.class, statutRdv, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<StatutRdv> getAllDemandes() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
