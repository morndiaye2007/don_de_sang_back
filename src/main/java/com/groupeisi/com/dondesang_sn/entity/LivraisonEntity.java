package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.StatutLivraison;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "livraisons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_livraison")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLivraison;
    
    @Column(name = "date_prevue")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePrevue;
    
    @Column(name = "quantite_livree")
    private Integer quantiteLivree;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_livraison")
    private StatutLivraison statutLivraison;
    
    @Column(name = "transporteur")
    private String transporteur;
    
    @Column(name = "numero_bon_livraison")
    private String numeroBonLivraison;
    
    @Column(name = "observations")
    private String observations;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private DemandeEntity demande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centre_collecte_id")
    private CentreCollecteEntity centreCollecte;
    
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
        if (datePrevue == null) {
            // Par défaut, livraison prévue dans 24h
            datePrevue = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        }
        if (statutLivraison == null) {
            statutLivraison = StatutLivraison.EN_PREPARATION;
        }
    }
}
