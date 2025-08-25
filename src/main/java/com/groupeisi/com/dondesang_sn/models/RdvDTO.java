package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupeisi.com.dondesang_sn.entity.CentreCollecteEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutRdv;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RdvDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty

    private Date dateRdv;
    private LocalTime heureRdv;
    @Enumerated(EnumType.STRING)
    private StatutRdv statutRdv;
   // private CentreCollecteDTO centreCollecte;
    private Long centreCollecteId;
  //  private CampagneDTO campagne;
    private Long campagneId;
 //   private DonneurDTO donneur;
    private Long donneurId;


}