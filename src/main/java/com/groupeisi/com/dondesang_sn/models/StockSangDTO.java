package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupeisi.com.dondesang_sn.entity.HopitalEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockSangDTO implements Serializable {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty

    private double nombreDeEpoche;
    @Enumerated(EnumType.STRING)
    private TypeGroupeSanguin groupeSanguin;
    @Temporal(TemporalType.DATE)
    private Date dateEntree;
    @Temporal(TemporalType.DATE)
    private Date datePeremption;
    private HopitalDTO hopital;

}
