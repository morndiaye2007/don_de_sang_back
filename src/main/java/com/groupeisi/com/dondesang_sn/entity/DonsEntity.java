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
@Table(name = "don")
@Entity
public class DonsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_don")
    private Date dateDon;
    @Column(name = "nombre_de_poche")
    private double nombrePoche;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_don")
    private StatusDon statutDon;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private RdvEntity agent;
}
