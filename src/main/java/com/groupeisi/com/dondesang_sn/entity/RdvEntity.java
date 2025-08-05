package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.StatutRdv;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rendez_vous")
@Entity
public class RdvEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_rdv")
    private Date dateRdv;
    @Column(name = "heure_rdv")
    private LocalTime heureRdv;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_rdv")
    private StatutRdv statutRdv;

    @ManyToOne
    @JoinColumn(name = "centreCollecte_id")
    private CentreCollecteEntity centreCollecte;
    @ManyToOne
    @JoinColumn(name = "donneur_id")
    private DonneurEntity donneur;
    @ManyToOne
    @JoinColumn(name = "campagne_id")
    private CampagneEntity campagne;

}
