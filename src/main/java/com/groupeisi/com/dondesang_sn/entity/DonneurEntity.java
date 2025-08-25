package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.SexType;
import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donneur")
public class DonneurEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom_donneur")
    private String nom;
    @Column(name = "prenom_donneur")
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(name = "donneur_sexe")
    private SexType sexe;
    @Enumerated(EnumType.STRING)
    @Column(name = "groupe_sanguin")
    private TypeGroupeSanguin groupeSanguin;
    @Temporal(TemporalType.DATE)
    @Column(name = "donneur_dni")
    private Date dni;
    @Column(name = "donneur_telephone")
    private String telephone;
    @Column(name = "donneur_addresse")
    private String addresse;
    @Column(name = "donneur_mdp")
    private String mdp;
    @Column(name = "date_dernier_don", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dateDernierDon;

    @ManyToOne
    @JoinColumn(name = "campagne_id", nullable = true)
    private CampagneEntity campagne;

    @ManyToOne
    @JoinColumn(name = "centre_id", nullable = true)
    private CentreCollecteEntity centreCollecte;

//    @OneToOne(mappedBy = "donneur", cascade = CascadeType.ALL)
//    private Historique historique;

}
