package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.StatusDon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "campagne")
@Entity
public class CampagneEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom_campagne")
    private String nom_campagne    ;
    @Column(name = "description")
    private String description     ;
    @Column(name = "date_debut")
    private Date date_debut;
    @Column(name = "date_fin")
    private Date date_fin;
    private double objectif_quantite_ml;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private RdvEntity agent;
}
