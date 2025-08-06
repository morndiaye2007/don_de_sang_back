package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.StatutCentre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Centre_Collecte")
@Entity
public class CentreCollecteEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom_centre")
    private String nom;
    @Column(name = "telephone_centre")
    private String telephone;
    @Column(name = "localisation_centre")
    private String localisation;
    @Enumerated(EnumType.STRING)
    @Column(name = "region_centre")
    private String region;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_centre")
    private StatutCentre statutCentre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockSang_id")
    private StockSangEntity stockSang;

    @OneToMany(mappedBy = "lieuCollectePrincipal", cascade = CascadeType.ALL)
    private List<CampagneEntity> campagnes;
    @OneToMany(mappedBy = "centre")
    private List<DonsEntity> dons;

    @OneToMany(mappedBy = "centre")
    private List<RdvEntity> rdv;

    @OneToMany(mappedBy = "centre")
    private List<DemandeEntity> demande;


}
