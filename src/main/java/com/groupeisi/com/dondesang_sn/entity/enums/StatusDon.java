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

public enum StatusDon {
    ANNULE("Don en anulle"),
    EN_ATTENTE("Contrat Resilier"),
    TERMINE("Contrat Terminée");



    @Getter
    @Setter
    private String description;

    StatusDon(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatusDon fromValue(Object statusContrat) {
        if (statusContrat instanceof Map) {
            Map<String, Object> mapStatusContrat = (Map<String, Object>) statusContrat;
            if (mapStatusContrat.containsKey("name")) {
                return StatusDon.valueOf(mapStatusContrat.get("name").toString());
            }
        }
        if (statusContrat instanceof String) {
            return StatusDon.valueOf(statusContrat.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatusDon.class, statusContrat, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<StatusDon> getAllContrat() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
