package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutCentre;
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
public class CentreCollecteDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty
    private String nom;
    private String telephone;
    private Date localisation;
    private StatutCentre statutCentre;
    private String region;
    private StockSangDTO stockSang;
    private Long stockSangId;
    private CampagneDTO campagne;
    private Long campagneId;
    private RdvDTO rdv;
    private Long rdvId;
    private DonDTO don;
    private Long donId;
    private DemandeDTO demande;
    private Long demandeId;

}
