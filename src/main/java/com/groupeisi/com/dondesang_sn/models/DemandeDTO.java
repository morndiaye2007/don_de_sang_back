package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import com.groupeisi.com.dondesang_sn.entity.HopitalEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutDemande;
import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DemandeDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty
    private Date dateDemande;
    private StatutDemande statutDemande;
    private TypeGroupeSanguin groupeSanguin;
    private double nombreDepoche;
  //  private HopitalDTO hopital;
    private Long hopitalId;
    private Long medecinId;
    private MedecinDTO medecin;
  //  private StockSangDTO stockSang;
    private Long stockSangId;
  //  private CentreCollecteDTO centreCollecte;
    private Long centreCollecteId;




}
