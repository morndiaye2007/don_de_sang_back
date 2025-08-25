package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.StatutDemande;
import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "demande")
@Entity
public class DemandeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_demande")
    private Date dateDemande;

    @Column(name = "nombre_de_poche", nullable = false)
    private double nombreDepoche;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_demande")
    private StatutDemande statutDemande;

    @Enumerated(EnumType.STRING)
    @Column(name = "groupe_sanguin")
    private TypeGroupeSanguin groupeSanguin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hopital_id", nullable = true)
    private HopitalEntity hopital;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private StockSangEntity stockSang;

    @ManyToOne
    private CentreCollecteEntity centreCollecte;

}