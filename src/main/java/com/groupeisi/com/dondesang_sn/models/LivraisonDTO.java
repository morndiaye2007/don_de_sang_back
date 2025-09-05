package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutLivraison;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonDTO {
    
    private Long id;
    
    @JsonProperty("date_livraison")
    private Date dateLivraison;
    
    @JsonProperty("date_prevue")
    private Date datePrevue;
    
    @JsonProperty("quantite_livree")
    private Integer quantiteLivree;
    
    @JsonProperty("statut_livraison")
    private StatutLivraison statutLivraison;
    
    private String transporteur;
    
    @JsonProperty("numero_bon_livraison")
    private String numeroBonLivraison;
    
    private String observations;
    
    @JsonProperty("demande_id")
    private Long demandeId;
    
    private DemandeDTO demande;
    
    @JsonProperty("centre_collecte_id")
    private Long centreCollecteId;
    
    @JsonProperty("centre_collecte")
    private CentreCollecteDTO centreCollecte;
    
    @JsonProperty("date_creation")
    private Date dateCreation;
}
