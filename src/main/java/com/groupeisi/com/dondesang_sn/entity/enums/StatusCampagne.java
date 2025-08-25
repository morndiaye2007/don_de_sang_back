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

public enum StatusCampagne {
    PLANIFIER("Campagne en planifier"),
    EN_COURS("Campagne en cours"),
    TERMINE("Campagne Terminée");



    @Getter
    @Setter
    private String description;

    StatusCampagne(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatusCampagne fromValue(Object statusCampagne) {
        if (statusCampagne instanceof Map) {
            Map<String, Object> mapStatusCampagne = (Map<String, Object>) statusCampagne;
            if (mapStatusCampagne.containsKey("name")) {
                return StatusCampagne.valueOf(mapStatusCampagne.get("name").toString());
            }
        }
        if (statusCampagne instanceof String) {
            return StatusCampagne.valueOf(statusCampagne.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatusCampagne.class, statusCampagne, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<StatusCampagne> getAllContrat() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
