package com.groupeisi.com.dondesang_sn.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hopital")
@Entity

public class HopitalEntity implements Serializable{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "nom_hopital")
        private String nom;
        @Column(name = "telephone_hopital")
        private String telephone;
        @Column(name = "adresse")
        private String adresse;
        @Column(name = "region_hopital")
        private String region;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "demande_id")
        private DemandeEntity demande;
}
