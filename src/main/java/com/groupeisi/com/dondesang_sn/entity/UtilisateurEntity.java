package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.RoleUtilisateur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utilisateur")
@Entity
public class UtilisateurEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utilisateur_nom")
    private String nom;
    @Column(name = "utilisateur_prenom")
    private String prenom;
    @Column(name = "mot_de_passe")
    private String mdp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true)
    private RoleEntity roleEntity;
}
