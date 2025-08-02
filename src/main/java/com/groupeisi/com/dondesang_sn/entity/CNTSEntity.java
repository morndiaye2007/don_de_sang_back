package com.groupeisi.com.dondesang_sn.entity;

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
public class CNTSEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom_cnts")
    private String nom_cnts;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "email")
    private String email;


    @ManyToOne
    @JoinColumn(name = "agent_id")
    private RdvEntity agent;
}
